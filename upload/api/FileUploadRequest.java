package com.tus.uploadservice.upload.api;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
	private String fileName;
    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<Long> getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(List<Long> partNumber2) {
		this.partNumber = partNumber2;
	}
	public Long getUploadOffset() {
		return uploadOffset;
	}
	public void setUploadOffset(Long uploadOffset) {
		this.uploadOffset = uploadOffset;
	}
	public Long getUploadLength() {
		return uploadLength;
	}
	public void setUploadLength(Long uploadLength) {
		this.uploadLength = uploadLength;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	private List<Long> partNumber;
    private Long uploadOffset;
    private Long uploadLength;
    private Long fileSize;
    private String userName;
    private MultipartFile file;
}

