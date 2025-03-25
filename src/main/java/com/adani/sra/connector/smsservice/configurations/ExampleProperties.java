package com.adani.sra.connector.smsservice.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@ConfigurationProperties("sms-connector-service")
public class ExampleProperties {

    @NotBlank
    private String baseUri;

    @NotBlank
    private String callbackURL;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String sender;


    @NotNull
    private Integer connectionTimeout;

    @NotNull
    private Integer readTimeout;

    @NotBlank
    private Map<Integer, String> smsType;

    @NotBlank
    private String peid;

    @NotBlank
    private String headerId;

    @NotBlank
    private String txtSmsTemplateId;

    @NotBlank
    private String txtSmsTemplateFormat;

    @NotBlank
    private String txtSmsTemplateTwoId;

    @NotBlank
    private String txtSmsTemplateFormatTwo;

    @NotBlank
    private String otpSmsTemplateId;

    @NotBlank
    private String otpSmsTemplateFormat;

    @NotBlank
    private String appointmentTemplateId;

    @NotBlank
    private String appointmentTemplateFormat;


    @NotNull
    private int otpDigit;

    @NotBlank
    private Map<Integer, String> escalatedCodes;

    @NotBlank
    private Map<Integer, String> successCodes;

    @NotBlank
    private Map<Integer, String> failureCodes;

    @NotBlank
    private String expectedAccessToken;

    @NotBlank
    private String extReauthenticationApi;

    @NotBlank
    private String extAuthKey;

    @NotBlank
    private String notifyHohHindiTemplateId;

    @NotBlank
    private String notifyHohHindiTemplateFormat;

    @NotBlank
    private String notifyHohMarathiTemplateId;

    @NotBlank
    private String notifyHohMarathiTemplateFormat;

    @NotBlank
    private String bulkBaseUri;


}
