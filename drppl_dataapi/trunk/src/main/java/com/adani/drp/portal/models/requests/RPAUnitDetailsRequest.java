package com.adani.drp.portal.models.requests;

import java.util.Optional;

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
public class RPAUnitDetailsRequest {
	
	private String uniqueSurveyId;
	private Optional<Integer> electoralRollYear = Optional.empty();
	private String electoralRollPartNo;
	private String electoralRollSrNo;
	private String electoralRollHutNo;
	private String rpaStatus;
	private String rpaRemarks;
	private Optional<String> rpaDate = Optional.empty();
	private Optional<Boolean> isValidHohName = Optional.empty();
	private String hohNameRemark;
	private Optional<String> hohNameValidDate = Optional.empty();
	private Optional<Boolean> isValidElectoral = Optional.empty();
	private String electoralRemark;
	private Optional<String> electoralDate = Optional.empty();
	private Optional<Boolean> isValidElectricityBill = Optional.empty();
	private String electricityBillRemark;
	private Optional<String> electricityBillDate = Optional.empty();
	private Optional<Boolean> isValidSurveyPavti = Optional.empty();
	private String surveyPavtiRemark;
	private Optional<String> surveyPavtiDate = Optional.empty();
	private Optional<Boolean> isValidGumasta = Optional.empty();
	private String gumastaRemark;
	private Optional<String> gumastaDate = Optional.empty();

}
