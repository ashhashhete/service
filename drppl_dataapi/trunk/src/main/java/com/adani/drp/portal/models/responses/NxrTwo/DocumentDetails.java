package com.adani.drp.portal.models.responses.NxrTwo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class DocumentDetails {
    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("number")
    private String number;
}
