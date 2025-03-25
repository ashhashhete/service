package com.adani.sra.connector.smsservice.models.requests.otpModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyOTPRequest {

    private String sender;
    private String transactionId;
    private String hohName;
    private String surveyId;
    private String otp;
    private String relationShipManagerNo;

}
