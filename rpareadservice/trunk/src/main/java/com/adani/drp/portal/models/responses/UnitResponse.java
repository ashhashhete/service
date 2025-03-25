package com.adani.drp.portal.models.responses;

import java.util.ArrayList;
import java.util.List;

import com.adani.drp.portal.models.responses.UnitListElement;
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
public class UnitResponse {
	
	private int totalCount;
	private int receivedDataCount;
	private List<UnitListElement> units = new ArrayList<>();
	

}
