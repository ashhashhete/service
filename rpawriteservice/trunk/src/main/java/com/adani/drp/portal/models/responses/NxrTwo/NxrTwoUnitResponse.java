package com.adani.drp.portal.models.responses.NxrTwo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NxrTwoUnitResponse {


    @JsonProperty("nagarName")
    private String nagarName;

    @JsonProperty("unitDetails")
    private List<UnitDetail> unitDetails;
}
