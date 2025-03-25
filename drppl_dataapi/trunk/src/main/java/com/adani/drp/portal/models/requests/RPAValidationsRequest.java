package com.adani.drp.portal.models.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@ToString
public class RPAValidationsRequest {
	
	private int userId;
	private List<RPAUnitDetailsRequest> unitDetails;
	private List<String> unit_Details;

}
