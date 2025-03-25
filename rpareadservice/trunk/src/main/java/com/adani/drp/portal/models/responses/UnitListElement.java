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
public class UnitListElement {
	
	private String unitUniqueId;
	private String surveyDate;
	private String unitUsage;
	private String structureYear;
	private String hohName;
	private String genQcStatus;
	private String unitStatus;
	private String remarks;

}
