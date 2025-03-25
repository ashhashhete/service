package com.tus.uploadservice.upload.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.uploadservice.upload.api.FileInfo;
import com.tus.uploadservice.upload.api.FileReader;
import com.tus.uploadservice.upload.api.UploadService;



import java.io.File;
import java.util.List;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.enums.X264_PROFILE;
import ws.schild.jave.info.VideoSize;

@Service
public class UploadServiceImpl implements UploadService {

    private FileReader uploadFileReader;

    private UploadFileWriter uploadFileWriter;
    
    @Autowired
    public UploadServiceImpl(
    		UploadFileReader uploadFileReader,
    		UploadFileWriter uploadFileWriter
    		) {
    		this.uploadFileReader = uploadFileReader;
    		this.uploadFileWriter = uploadFileWriter;
    }

    public List<Long> getCurrentOffsets(String fileName, List<FileInfo> partInfoList) throws IOException {
        if (!uploadFileReader.fileExists(fileName)) {
            throw new IOException("No directory for file, " + fileName);
        }

        return partInfoList.stream()
                .map(uploadFileReader::getOffset)
                .collect(Collectors.toList());
    }

    public String getDirectoryPath(String fileName) throws IOException {
        return uploadFileWriter.createDirectory(fileName);
    }

    public PartInfo write(FileInfo partInfo,String paths) throws IOException {
    	  // Write the file and handle the result
        PartInfo result = Optional.of(partInfo)
                .map(uploadFileWriter::write)
                .orElseThrow(this::onIncomplete);

        // Encode the video after the write operation is completed
        this.EncodeVideo(paths);

        return result;
    }
//    public PartInfo write(FileInfo partInfo) throws IOException {
//        return Optional.of(partInfo)
//                .map(uploadFileWriter::write)
//                .filter(uploadFileReader::isComplete)
//                .orElseThrow(this::onIncomplete);
//    }

    public Long concat(List<FileInfo> partInfoList) throws IOException {
        return uploadFileWriter.concat(partInfoList);
    }

    private IOException onIncomplete() {
        return new IOException("Upload incomplete.");
    }
    

	public void EncodeVideo(String filePath) {
		 
        //new code for encoding start
        
        /* Step 1. Declaring source file and Target file */
        File source = new File(filePath +".mp4");
        File target = new File(filePath +"_converted.mp4");
        MultimediaObject multimediaObject = new MultimediaObject(source);
        /* Step 2. Set Audio Attrributes for conversion*/
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
// here 64kbit/s is 64000
        audio.setBitRate(64000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        /* Step 3. Set Video Attributes for conversion*/
        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setX264Profile(X264_PROFILE.BASELINE);
// Here 160 kbps video is 160000
        video.setBitRate(160000);
// More the frames more quality and size, but keep it low based on devices like mobile
        video.setFrameRate(15);
        video.setSize(new VideoSize(400, 300));

        /* Step 4. Set Encoding Attributes*/
        EncodingAttributes attrs = new EncodingAttributes();
//        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        /* Step 5. Do the Encoding*/
        try {
            Encoder encoder = new Encoder();
            encoder.encode(multimediaObject, target, attrs);
//            encoder.encode( source, target, attrs);
            
        } catch (Exception e) {
            /*Handle here the video failure*/
            e.printStackTrace();
        }

    
        
      //new code for encoding End
}
}
