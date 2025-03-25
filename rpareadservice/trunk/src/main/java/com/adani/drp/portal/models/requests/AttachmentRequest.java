package com.adani.drp.portal.models.requests;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@ToString
@RequiredArgsConstructor
public class AttachmentRequest {

    private String uniqueSurveyId;
    private Optional<String> documentSource = Optional.empty();
    private Optional<String> documentCategory = Optional.empty();
	
}
