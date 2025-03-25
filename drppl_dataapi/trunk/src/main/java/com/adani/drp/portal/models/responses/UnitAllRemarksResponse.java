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
public class UnitAllRemarksResponse {
	
	private String unitUniqueId;
	private String surveyDate;
	private String lastEditedDate;
    private String imUploadStatus;
    private String drpplEvaluationTeam1;
    private String drpplEvaluationTeam2;
    private String drpEvaluationTeam1;

}
