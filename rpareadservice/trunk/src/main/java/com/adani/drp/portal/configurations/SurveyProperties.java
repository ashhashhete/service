package com.adani.drp.portal.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("adani-id")
public class SurveyProperties {


	private String boothBasePath;
	private String boothPdfUrl;
	private String env;
	private String fileSize;

	
	public String getBoothBasePath() {
		return boothBasePath;
	}

	public void setBoothBasePath(String boothBasePath) {
		this.boothBasePath = boothBasePath;
	}

	public String getBoothPdfUrl() {
		return boothPdfUrl;
	}

	public void setBoothPdfUrl(String boothPdfUrl) {
		this.boothPdfUrl = boothPdfUrl;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}


}
