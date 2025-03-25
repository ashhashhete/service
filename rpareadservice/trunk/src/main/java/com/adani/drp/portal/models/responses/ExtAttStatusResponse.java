package com.adani.drp.portal.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class ExtAttStatusResponse {

    private Boolean isUploaded;

    private String unitUniqueId;



}
