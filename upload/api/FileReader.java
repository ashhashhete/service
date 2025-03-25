package com.tus.uploadservice.upload.api;

public interface FileReader {
    public boolean fileExists(String fileName);

    public boolean isComplete(FileInfo fileInfo);
    
    public Long getOffset(FileInfo fileInfo);    
}