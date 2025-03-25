package com.tus.uploadservice.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tus.uploadservice.upload.api.FileInfo;
import com.tus.uploadservice.upload.api.UploadService;
import com.tus.uploadservice.upload.impl.PartInfo;
import com.tus.uploadservice.util.Constants;
import com.tus.uploadservice.util.PathFactory;



//@CrossOrigin(origins = "http://localhost:80", exposedHeaders = { "fileDir", "fileName", "uploadOffset", "partNumber", "reason"})
@Controller
@Component
@RequestMapping("/upload")
public class UploadController {
	

    @Autowired
    UploadService uploadService;
    
    @Value("${base.directory}")
    private String customPath;
    
    

	 @GetMapping("/get")
	    public String welcome() {
	        return "tus upload upload service";
	    }
    
    @PostMapping("/new/files")
    public ResponseEntity createUploadFile(@RequestHeader(name="fileName") String fileName,
    		@RequestHeader(name="filePath") String filePath,
    		 @RequestHeader(name="partNumbers") List<Long> partNumbers,
                                           @RequestHeader(name = "partNumber") Long partNumber,
                                           @RequestHeader(name = "uploadOffset") Long uploadOffset,
                                           @RequestHeader(name = "uploadLength") Long uploadLength,
                                           @RequestHeader(name = "fileSize") Long fileSize,
                                           @RequestHeader(name = "userName") String userName,
                                           @RequestHeader(name = "fileExt") String fileExt,
                                           InputStream inputStream) {
    	
    	
    	
        
 	String paths=customPath+filePath;

    File file = new File(paths);
    file.setExecutable(true, false);
    file.setWritable(true, false);
    file.setReadable(true, false);
    	Constants.setTmpDir(paths);
    	System.out.println("File path: " + paths);
    	if (uploadOffset == 1) {
    	    File fileToDelete = new File(paths);
    	    try {
    	        	FileUtils.deleteDirectory(fileToDelete);   	        
    	        	}
    	  catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	Path path = PathFactory.createDirectoryPath(fileName);

         if (Files.exists(path) && Files.isDirectory(path)) {
             System.out.println("Directory already exists, " + path.toString());
             List<FileInfo> partInfoList = partNumbers.stream()
	                    .map(num -> PartInfo.builder().fileName(fileName).partNumber(num).build())
	                    .collect(Collectors.toList());
             try {
	                ResponseEntity offsetResponse = onExists(uploadService.getCurrentOffsets(fileName, partInfoList));

	                if (offsetResponse.getStatusCode().is2xxSuccessful()) {
	                    System.out.println("Upload is successful!");
	                    PartInfo partInfo = PartInfo.builder()
	                            .fileSize(fileSize)
	                            .fileName(fileName)
	                            .fileExt(fileExt)
//	                            .partNumber(partNumber)
	                            .offset(uploadOffset)
	                            .length(uploadLength)
	                            .userName(userName)
	                            .inputStream(inputStream)
	                            .build();

	                    try {
	                        return onComplete(uploadService.write(partInfo,paths+fileName));
	                    } catch (IOException e) {
	                        return onInterrupt(partInfo, e.getMessage());
	                    }

	                    
	                } else {
	                    System.out.println("Upload failed.");
	                }

	                return offsetResponse;
	            } catch (IOException e) {
	                return onNotExist(fileName);
	            }
             
         }
		 try {
		        ResponseEntity response = onCreateDir(uploadService.getDirectoryPath(fileName));

		        if (response.getStatusCode().is2xxSuccessful()) {
		            System.out.println("File or directory created successfully!");
		            List<FileInfo> partInfoList = partNumbers.stream()
		                    .map(num -> PartInfo.builder().fileName(fileName).build())
		                    .collect(Collectors.toList());

		            try {
		                ResponseEntity offsetResponse = onExists(uploadService.getCurrentOffsets(fileName, partInfoList));

		                if (offsetResponse.getStatusCode().is2xxSuccessful()) {
		                    System.out.println("Upload is successful!");
		                    PartInfo partInfo = PartInfo.builder()
		                            .fileSize(fileSize)
		                            .fileName(fileName)
		                            .fileExt(fileExt)
//		                            .partNumber(partNumber)
		                            .offset(uploadOffset)
		                            .length(uploadLength)
		                            .userName(userName)
		                            .inputStream(inputStream)
		                            .build();

		                    try {
		                        return onComplete(uploadService.write(partInfo,paths+fileName));
		                    } catch (IOException e) {
		                        return onInterrupt(partInfo, e.getMessage());
		                    }

		                    
		                } else {
		                    System.out.println("Upload failed.");
		                }

		                return offsetResponse;
		            } catch (IOException e) {
		                return onNotExist(fileName);
		            }
		        } else {
		            System.out.println("Failed to create file or directory.");
		        }

		        return response;
		    } catch (IOException e) {
		        return onFailedCreateDir(fileName, e.getMessage());
		    }
		}
    
    
    //end new code 

    @GetMapping("/file/{id}")
    public ResponseEntity readUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumbers") List<Long> partNumbers
    ) {
        List<FileInfo> partInfoList = partNumbers.stream()
                .map(num -> PartInfo.builder().fileName(fileName).partNumber(num).build())
                .collect(Collectors.toList());

        try {
            return onExists(uploadService.getCurrentOffsets(fileName, partInfoList));
        } catch (IOException e) {
            return onNotExist(fileName);
        }
    }

    @PostMapping("/files")
    public ResponseEntity createUpload(
            @RequestHeader(name="fileName") String fileName
    ) {
        try {
            return onCreateDir(uploadService.getDirectoryPath(fileName));
        } catch (IOException e) {
            return onFailedCreateDir(fileName, e.getMessage());
        }
    }

//    @PatchMapping("/file/{id}")
//    public ResponseEntity updateUpload(
//            @RequestHeader(name = "fileName") String fileName,
//            @RequestHeader(name = "partNumber") Long partNumber,
//            @RequestHeader(name = "uploadOffset") Long uploadOffset,
//            @RequestHeader(name = "uploadLength") Long uploadLength,
//            @RequestHeader(name = "fileSize") Long fileSize,
//            @RequestHeader(name = "userName") String userName,
//            @RequestParam("file") MultipartFile file
//    ) throws IOException {
//        PartInfo partInfo = PartInfo.builder()
//                .fileSize(fileSize)
//                .fileName(fileName)
//                .partNumber(partNumber)
//                .offset(uploadOffset)
//                .length(uploadLength)
//                .userName(userName)
//                .inputStream(file.getInputStream())
//                .build();
//
//        try {
//            return onComplete(uploadService.write(partInfo));
//        } catch (IOException e) {
//            return onInterrupt(partInfo, e.getMessage());
//        }
//    }


    @PostMapping("/files/complete")
    public ResponseEntity completeUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="fileExt") String fileExt,
            @RequestHeader(name="partNumbers") List<Long> partNumbers,
            @RequestHeader(name="fileSize") Long fileSize
    ) {
        List<FileInfo> partInfoList = partNumbers.stream()
                .map(partNumber -> { 
                    return PartInfo.builder()
                            .fileName(fileName)
                            .fileExt(fileExt)
                            .partNumber(partNumber)
                            .fileSize(fileSize).build();
                })
                .collect(Collectors.toList());

        try {
            return onConcatenate(uploadService.concat(partInfoList));
        } catch (IOException e) {
            return onFailedConcatenate(fileName, e.getMessage());
        }
    }

    @RequestMapping(value="/destroy", method=RequestMethod.DELETE)
    public String destroyUpload() {
        return "Destroying upload.";
    }

    private ResponseEntity onConcatenate(Long totalBytesUploaded) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("totalBytesUploaded", totalBytesUploaded.toString())
                .build();
    }

    private ResponseEntity onFailedConcatenate(String fileName, String errorMessage) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("fileName", fileName)
                .body(errorMessage);
    }

    private ResponseEntity onCreateDir(String fileDir) {
	  
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("fileDir", fileDir)
                .build();
    }

    private ResponseEntity onExists(List<Long> currentOffsetList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(currentOffsetList);
    }

    private ResponseEntity onNotExist(String fileName) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("fileName", fileName)
                .body("No upload has been created for file, " + fileName + ", yet.");
    }

    private ResponseEntity onComplete(PartInfo partInfo) {
    	
    	String successMessage = "Successfully processed";
        int statusCode = HttpStatus.OK.value();

        String jsonResponse = "{ \"message\": \"" + successMessage + "\", \"status code\": \"" + 200 + "\" }";

    	    return ResponseEntity.status(HttpStatus.OK)
    	            .header("newOffset", partInfo.getOffset().toString())
    	            .body(jsonResponse);
    }

    private ResponseEntity onInterrupt(PartInfo partInfo, String errorMessage) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("partNumber", partInfo.getPartNumber().toString())
                .body(errorMessage);
    }

    private ResponseEntity onFailedCreateDir(String fileName, String errorMessage) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("fileName", fileName)
                .body(errorMessage);
    }
}
