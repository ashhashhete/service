package com.adani.drp.portal.configurations;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Component
@Data
@ConfigurationProperties("adani-drp-portal")
public class PortalProperties {

	 @NotBlank
	    private String customDevPath;

	    @NotBlank
	    private String queryConfigPath;

	    @NotNull
	    private int unitsPaginationPages;

	    @NotNull
	    private int unitsPaginationNoOfEntries;

	    @NotBlank
	    private String imageUrlGetAttachments;

	    @NotBlank
	    private Map<Integer, String> escalatedCodes;

	    @NotBlank
	    private Map<Integer, String> successCodes;

	    @NotBlank
	    private Map<Integer, String> failureCodes;



	    @NotBlank
	    private String imExternalUrl;

	    @NotBlank
	    private String imInternalUrl;

	    @NotBlank
	    private String expectedAccessToken;

	    @NotBlank
	    private String extReauthenticationApi;

	    @NotBlank
	    private String extAuthKey;

	    @NotBlank
	    private List<String> unitAttCategoryExclusionList;

	    @NotBlank
	    private String baseDirectoryInternal;

	    @NotBlank
	    private String baseDirectoryExternal;

	    @NotBlank
	    private String backupDirectory;

	    @NotBlank
	    private List<String> imExternalCategoryExclusionList;

	    @NotBlank
	    private String gisAuthUrl;

	    @NotBlank
	    private String gisAuthUsername;

	    @NotBlank
	    private String gisAuthPassword;

	    @NotBlank
	    private String odooUserUserUrl;

	    @NotBlank
	    private List<String> nxrTwoCategoryExclusionList;

	    @NotBlank
	    private String translateUrl;

	    @NotBlank
	    private String translateKey;

	    @NotBlank
	    private String translateRegion;

	    @NotBlank
	    private String transliterateUrl;

	    @NotBlank
	    private String transliterateKey;

	    @NotBlank
	    private String transliterateRegion;

	    @NotBlank
	    private String marathiValueRC;

	    @NotBlank
	    private String targetLanguage;

	    @NotBlank
	    private String fromScript;

	    @NotBlank
	    private String toScript;
	    
	    @NotBlank
	    private String authUrl;
	    
	    @NotBlank
	    private String key;
	    
	    @NotBlank
	    private String tokenAuthUrl;
	    
	    @NotBlank
	    private String tokenFormData;

}
