package com.adani.drp.portal.models.responses;

import java.util.ArrayList;
import java.util.List;

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
public class AttachmentResponse {
	
	private String uniqueSurveyId;
	private List<AttachmentElement> unit = new ArrayList<>();
	private List<AttachmentElement> hoh = new ArrayList<>();
	private List<AttachmentElement> member = new ArrayList<>();

}
