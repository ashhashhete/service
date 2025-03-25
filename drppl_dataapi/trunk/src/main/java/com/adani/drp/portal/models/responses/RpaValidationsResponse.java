package com.adani.drp.portal.models.responses;

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
public class RpaValidationsResponse {
	
    private Integer electoralRollYear;
    private String electoralRollPartNo;
    private String electoralRollSrNo;
    private String electoralRollHutNo;
    private String rpaStatus;
    private String rpaRemark;
    private String rpaDate;
    private Boolean isValidElectoral;
    private String electoralRemark;
    private String electoralDate;
    private Boolean isValidElectricityBill;
    private String electricityBillRemark;
    private String electricityBillDate;
    private Boolean isValidSurveyPavti;
    private String surveyPavtiRemark;
    private String surveyPavtiDate;
    private Boolean isValidGumasta;
    private String gumastaRemark;
    private String gumastaDate;
    private String unitUniqueId;
    private Boolean isValidHohName;
    private String hohNameRemark;
    private String hohNameDate;
    private Integer createdBy;
	private String createdDate;
	private Integer editedBy;
	private String editedDate;

}
