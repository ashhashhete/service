package com.tus.uploadservice.upload.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.tus.uploadservice.upload.api.FileInfo;
import com.tus.uploadservice.upload.api.FileReader;
import com.tus.uploadservice.util.PathFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadFileReader implements FileReader {

    public boolean fileExists(String fileName) {
        return PathFactory.createDirectoryPath(fileName).toFile().exists();
    }

    public boolean isComplete(FileInfo partInfo) {
        return partInfo.getOffset().equals(partInfo.getLength());
    }


    public Long getOffset(FileInfo partInfo) {
          Path partPath = PathFactory.createPartPath((PartInfo) partInfo);
          String filePath = partPath.toString();
          log.info("Retrieving pointer for file part, " + filePath);
          Long currentOffset = 0L;

          if (!Files.exists(partPath)) {
              log.warn("File does not exist at path: " + filePath);
              return currentOffset; 
          }

          try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
              currentOffset = raf.length();
          } catch (IOException e) {
              log.error("Error attempting to get offset for file, " + filePath);
              throw new RuntimeException(e);
          }

          return currentOffset;
      }
}
