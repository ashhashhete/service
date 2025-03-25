package com.adani.drp.portal.models.requests;

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
public class LoginRequest {
	
    private String username;

    private String password;

}
