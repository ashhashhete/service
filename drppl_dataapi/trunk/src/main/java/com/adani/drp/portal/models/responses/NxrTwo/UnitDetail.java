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
public class UnitDetail {

    @JsonProperty("unitUniqueId")
    private String unitUniqueId;

    @JsonProperty("golobalid")
    private String globalId;

    @JsonProperty("hohName")
    private String hohName;

    @JsonProperty("usage")
    private String usage;

    private String unitAreaSqft;

    @JsonProperty("residentialAreaSqft")
    private String residentialAreaSqft;

    @JsonProperty("commercialAreaSqft")
    private String commercialAreaSqft;

    @JsonProperty("evaluationRemarks")
    private String evaluationRemarks;

    @JsonProperty("evaluationComments")
    private String evaluationComments;

    @JsonProperty("yearOfElectoral")
    private String yearOfElectoral;

    @JsonProperty("electoralPartNo")
    private String electoralPartNo;

    @JsonProperty("electoralSerialNo")
    private String electoralSerialNo;

    @JsonProperty("electoralHutNo")
    private String electoralHutNo;

    @JsonProperty("hohGlobalid")
    private String hohGlobalid;

    @JsonProperty("unitDocumentDetails")
    private List<DocumentDetails> unitDocumentDetails;

    @JsonProperty("hohDocumentDetails")
    private List<DocumentDetails> hohDocumentDetails;
}
