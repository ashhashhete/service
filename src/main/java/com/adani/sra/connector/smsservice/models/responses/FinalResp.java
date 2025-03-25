package com.adani.sra.connector.smsservice.models.responses;


import lombok.Data;

@Data
public class FinalResp {

    private int status;
    private String message;
    private String transactionId;
    private String receiver;


}
