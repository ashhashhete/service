package com.tus.uploadservice.util;

import java.io.IOException;

public class IncompleteUploadException extends IOException {
    private final String customJson;
    private final int customStatus;

    public IncompleteUploadException(String message, String customJson, int customStatus) {
        super(message);
        this.customJson = customJson;
        this.customStatus = customStatus;
    }

    public String getCustomJson() {
        return customJson;
    }

    public int getCustomStatus() {
        return customStatus;
    }



}
