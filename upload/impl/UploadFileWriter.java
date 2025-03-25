package com.tus.uploadservice.upload.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.ToLongFunction;


import org.springframework.stereotype.Service;

import com.tus.uploadservice.upload.api.FileInfo;
import com.tus.uploadservice.upload.api.FileWriter;
import com.tus.uploadservice.util.PathFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadFileWriter implements FileWriter {

    public String createDirectory(String fileName) throws IOException {
        if (fileName == null) {
            throw new IOException("Cannot create directory for a null filename!");
        }
        System.out.println( Path.of(fileName));
        Path path = PathFactory.createDirectoryPath(fileName);
        
        
        Files.createDirectories(path);
        log.info("Created file directory, " + path.toString());
        return path.toString();
    }

    public PartInfo write(FileInfo partInfo) {
        String filePath = PathFactory.createPartPath((PartInfo) partInfo).toString();
        Long bytesTransferred = 0L;

        try (
            RandomAccessFile raf = new RandomAccessFile(filePath + partInfo.getFileExt(), "rw");
            ReadableByteChannel is = Channels.newChannel(partInfo.getInputStream());
            FileChannel os = raf.getChannel();
        ) {
            log.info("Opening channels for file part, " + filePath);
            log.info("Writing file part, " + filePath);
            Long bytesToTransfer = partInfo.getLength() - partInfo.getOffset();

            if (bytesToTransfer > 0) {
                bytesTransferred = os.transferFrom(is, os.size(), bytesToTransfer);
            }

            if (bytesToTransfer.equals(bytesTransferred)) {
                log.info("Done writing file part, " + filePath + partInfo.getFileExt());
            }
        } catch (IOException e) {
            log.error("Error writing file part, " + filePath);
            throw new RuntimeException(e);
        }

        // Set file permissions to 0777
        File file = new File(filePath);
        file.setExecutable(true, false);
        file.setWritable(true, false);
        file.setReadable(true, false);
        

        return PartInfo.builder()
            .offset(partInfo.getOffset() + bytesTransferred)
            .length(partInfo.getLength())
            .fileName(partInfo.getFileName())
            .partNumber(((PartInfo) partInfo).getPartNumber())
            .build();
    }
    public Long concat(List<FileInfo> partInfoList) throws IOException {
        String fileName = partInfoList.get(0).getFileName();
        String fileExt = partInfoList.get(0).getFileExt();
        String finalPath = PathFactory.createFinalPath(fileName, fileExt).toString();
        log.info(finalPath);
        Long totalBytesTransferred = 0L;

        try (
            RandomAccessFile raf = new RandomAccessFile(finalPath, "rw");
            FileChannel outputStream = raf.getChannel();
        ) {
            log.info("Concatenating file, " + fileName);
            totalBytesTransferred = partInfoList.stream()
                .mapToLong(toBytesTransferred(outputStream))
                .sum();
        } catch (IOException e) {
            log.error("Error attempting to concatenate parts for file, " + fileName);
            throw new IOException(e);
        }

        // Set file permissions to 0777
        File file = new File(finalPath);
        file.setExecutable(true, false);
        file.setWritable(true, false);
        file.setReadable(true, false);

        return totalBytesTransferred;
    }

    private ToLongFunction<FileInfo> toBytesTransferred(FileChannel outputStream) {
        return partInfo -> {
            Long bytesTransferred = 0L;
            try {
                Path partPath = PathFactory.createPartPath((PartInfo) partInfo);
                InputStream is = Files.newInputStream(partPath);

                bytesTransferred = outputStream.transferFrom(
                    Channels.newChannel(is),
                    outputStream.size(),
                    partInfo.getFileSize()
                );
            } catch (IOException e) {
                log.error("Error during file concatenation for file, " + partInfo.getFileName());
                throw new RuntimeException(e);
            }
            return bytesTransferred;
        };
    }}

