package com.tus.uploadservice.upload.impl;

import java.io.InputStream;

import com.tus.uploadservice.upload.api.FileInfo;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PartInfo implements FileInfo {
    private String fileName;
    private String fileType;
    private String fileExt;
    private Long partNumber;
    private Long offset;
    private Long length;
    private Long fileSize;
    private String userName;
    private String filePath;
    private InputStream inputStream;
    
}
