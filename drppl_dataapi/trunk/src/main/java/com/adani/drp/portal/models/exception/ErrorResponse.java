package com.adani.drp.portal.models.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String message;
    private String transactionId;
    private String receiver;


}
