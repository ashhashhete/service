package com.adani.drp.portal.models.responses.NxrTwo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RpaValidationResponse {


    @JsonProperty(value = "unit_unique_id",defaultValue = "")
    private String unitUniqueId;

    @JsonProperty(value = "electoral_remark",defaultValue = "")
    private String electoralRemark;

    @JsonProperty(value = "electricity_bill_remark",defaultValue = "")
    private String electricityBillRemark;

    @JsonProperty(value = "survey_pavti_remark",defaultValue = "")
    private String surveyPavtiRemark;

    @JsonProperty(value = "gumasta_remark",defaultValue = "")
    private String gumastaRemark;

    @JsonProperty(value = "aadhar_no",defaultValue = "")
    private String aadharNo;

    @JsonProperty(value = "adhaar_verify_date",required = false)
    private String adhaarVerifyDate;

    @JsonProperty(value = "adhaar_verify_remark",defaultValue = "")
    private String adhaarVerifyRemark;

    @JsonProperty(value = "adhaar_verify_status",defaultValue = "")
    private String adhaarVerifyStatus;

    @JsonProperty(value = "hoh_name",defaultValue = "")
    private String hohName;

    @JsonProperty(value = "respondent_hoh_name",defaultValue = "")
    private String respondentHohName;

    // @JsonProperty(value = "electoral_remark_ma",defaultValue = "")
    ////    private String electoralRemarkMa;
    ////
    ////    @JsonProperty(value = "electricity_bill_remark_ma",defaultValue = "")
    ////    private String electricityBillRemarkMa;
    ////
    ////    @JsonProperty(value = "survey_pavti_remark_ma",defaultValue = "")
    ////    private String surveyPavtiRemarkMa;
    ////
    ////    @JsonProperty(value = "gumasta_remark_ma",defaultValue = "")
    ////    private String gumastaRemarkMa;
}
