package com.adani.drp.portal.services;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.entities.nxrtwo.RpaValidations;
import com.adani.drp.portal.entities.nxrtwo.RpaValidationsUploadFailure;
import com.adani.drp.portal.entities.nxrtwo.RpaValidationsUploadStatus;
import com.adani.drp.portal.entities.nxrtwo.UnitAllRemarks;
import com.adani.drp.portal.entities.nxrtwo.UnitInfo;
import com.adani.drp.portal.models.requests.RPAUnitDetailsRequest;
import com.adani.drp.portal.models.requests.RPAValidationsRequest;
import com.adani.drp.portal.models.responses.ApiResponse;
import com.adani.drp.portal.models.responses.RpaValidationsResponse;
import com.adani.drp.portal.models.responses.UnitAllRemarksResponse;
import com.adani.drp.portal.repository.nxrtwo.UnitRepo;
import com.adani.drp.portal.utils.CommonMethods;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adani.drp.portal.repository.nxrtwo.RpaValidationsRepo;
import com.adani.drp.portal.repository.nxrtwo.RpaValidationsUploadFailureRepo;
import com.adani.drp.portal.repository.nxrtwo.RpaValidationsUploadStatusRepo;
import com.adani.drp.portal.repository.nxrtwo.UnitAllRemarksRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RPAServiceImpl {
	
	private final PortalProperties portalProperties;

	private final UnitRepo unitRepo;
	
	private final RpaValidationsRepo rpaValidationsRepo;
	
	private final RpaValidationsUploadStatusRepo RpaUploadStatusRepo;
	
	private final RpaValidationsUploadFailureRepo RpaUploadFailureRepo;
	
	private final UnitAllRemarksRepo unitAllRemarksRepo;
	
	
	public ApiResponse getALLRPAData(String fromDate, String toDate) {

		ApiResponse apiResponse = new ApiResponse();

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			if (fromDate != null || toDate != null) {

				if (fromDate != null && toDate != null) {

					log.info("fromDate: " + fromDate);
					log.info("toDate: " + toDate);

					// Parse the strings into LocalDate
					LocalDate fDate = LocalDate.parse(fromDate, formatter);
					LocalDate tDate = LocalDate.parse(toDate, formatter);

					if (fDate.isBefore(tDate) || fDate.isEqual(tDate)) {
						
						List<Map<String, Object>> unit_Details = rpaValidationsRepo.getRPADataByLastEditedDate(
								CommonMethods.parseDateTime(fromDate + " 00:00:00"), CommonMethods.parseDateTime(toDate + " 23:59:59"));
						
						List<RpaValidationsResponse> unitDetails = new ArrayList<>();

						unitDetails = unit_Details.stream().map((Map<String, Object> unitDetail) -> {
							RpaValidationsResponse response = new RpaValidationsResponse();

							response.setElectoralRollYear((Integer) unitDetail.get("electoral_roll_year"));
							response.setElectoralRollPartNo((String) unitDetail.get("electoral_roll_part_no"));
							response.setElectoralRollSrNo((String) unitDetail.get("electoral_roll_sr_no"));
							response.setElectoralRollHutNo((String) unitDetail.get("electoral_roll_hut_no"));
							response.setRpaStatus((String) unitDetail.get("rpa_status"));
							response.setRpaRemark((String) unitDetail.get("rpa_remark"));
							response.setRpaDate((String) unitDetail.get("rpa_date"));
							response.setIsValidElectoral((Boolean) unitDetail.get("is_valid_electoral"));
							response.setElectoralRemark((String) unitDetail.get("electoral_remark"));
							response.setElectoralDate((String) unitDetail.get("electoral_date"));
							response.setIsValidElectricityBill((Boolean) unitDetail.get("is_valid_electricity_bill"));
							response.setElectricityBillRemark((String) unitDetail.get("electricity_bill_remark"));
							response.setElectricityBillDate((String) unitDetail.get("electricity_bill_date"));
							response.setIsValidSurveyPavti((Boolean) unitDetail.get("is_valid_survey_pavti"));
							response.setSurveyPavtiRemark((String) unitDetail.get("survey_pavti_remark"));
							response.setSurveyPavtiDate((String) unitDetail.get("survey_pavti_date"));
							response.setIsValidGumasta((Boolean) unitDetail.get("is_valid_gumasta"));
							response.setGumastaRemark((String) unitDetail.get("gumasta_remark"));
							response.setGumastaDate((String) unitDetail.get("gumasta_date"));
							response.setUnitUniqueId((String) unitDetail.get("unit_unique_id"));
							response.setIsValidHohName((Boolean) unitDetail.get("is_valid_hoh_name"));
							response.setHohNameRemark((String) unitDetail.get("hoh_name_remark"));
							response.setHohNameDate((String) unitDetail.get("hoh_name_date"));
							response.setCreatedBy((Integer) unitDetail.get("created_by"));
							response.setCreatedDate((String) unitDetail.get("created_date"));
							response.setEditedBy((Integer) unitDetail.get("edited_by"));
							response.setEditedDate((String) unitDetail.get("edited_date"));

							return response;
						}).distinct().collect(Collectors.toList());

						if (unitDetails == null) {
							log.info("unit list not found");
							apiResponse = new ApiResponse(new ApiResponse.Status(0, "404", "No Data Found"),
									unitDetails);
						} else if (unitDetails.isEmpty()) {
							log.info("unit list empty");
							apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"),
									unitDetails);
						} else {
							log.info("unitsList retrieved");
							apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), unitDetails);
						}
					} else {
						apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Range."),
								new ArrayList<>());
					}

				} else {
					apiResponse = new ApiResponse(
							new ApiResponse.Status(1, "404", "From Date and To Date Both are required or Both date will be null."),
							new ArrayList<>());
				}

			} else {
				List<Map<String, Object>> unit_Details = rpaValidationsRepo.getAllRPAData();
				
				List<RpaValidationsResponse> unitDetails = new ArrayList<>();

				unitDetails = unit_Details.stream().map((Map<String, Object> unitDetail) -> {
					RpaValidationsResponse response = new RpaValidationsResponse();

					response.setElectoralRollYear((Integer) unitDetail.get("electoral_roll_year"));
					response.setElectoralRollPartNo((String) unitDetail.get("electoral_roll_part_no"));
					response.setElectoralRollSrNo((String) unitDetail.get("electoral_roll_sr_no"));
					response.setElectoralRollHutNo((String) unitDetail.get("electoral_roll_hut_no"));
					response.setRpaStatus((String) unitDetail.get("rpa_status"));
					response.setRpaRemark((String) unitDetail.get("rpa_remark"));
					response.setRpaDate((String) unitDetail.get("rpa_date"));
					response.setIsValidElectoral((Boolean) unitDetail.get("is_valid_electoral"));
					response.setElectoralRemark((String) unitDetail.get("electoral_remark"));
					response.setElectoralDate((String) unitDetail.get("electoral_date"));
					response.setIsValidElectricityBill((Boolean) unitDetail.get("is_valid_electricity_bill"));
					response.setElectricityBillRemark((String) unitDetail.get("electricity_bill_remark"));
					response.setElectricityBillDate((String) unitDetail.get("electricity_bill_date"));
					response.setIsValidSurveyPavti((Boolean) unitDetail.get("is_valid_survey_pavti"));
					response.setSurveyPavtiRemark((String) unitDetail.get("survey_pavti_remark"));
					response.setSurveyPavtiDate((String) unitDetail.get("survey_pavti_date"));
					response.setIsValidGumasta((Boolean) unitDetail.get("is_valid_gumasta"));
					response.setGumastaRemark((String) unitDetail.get("gumasta_remark"));
					response.setGumastaDate((String) unitDetail.get("gumasta_date"));
					response.setUnitUniqueId((String) unitDetail.get("unit_unique_id"));
					response.setIsValidHohName((Boolean) unitDetail.get("is_valid_hoh_name"));
					response.setHohNameRemark((String) unitDetail.get("hoh_name_remark"));
					response.setHohNameDate((String) unitDetail.get("hoh_name_date"));
					response.setCreatedBy((Integer) unitDetail.get("created_by"));
					response.setCreatedDate((String) unitDetail.get("created_date"));
					response.setEditedBy((Integer) unitDetail.get("edited_by"));
					response.setEditedDate((String) unitDetail.get("edited_date"));

					return response;
				}).distinct().collect(Collectors.toList());

				if (unitDetails == null) {
					log.info("unit list not found");
					apiResponse = new ApiResponse(new ApiResponse.Status(0, "404", "No Data Found"), unitDetails);
				} else if (unitDetails.isEmpty()) {
					log.info("unit list empty");
					apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"), unitDetails);
				} else {
					log.info("unitsList retrieved");
					apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), unitDetails);
				}

			}
		} catch (DateTimeParseException e) {
			apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Format."), new ArrayList<>());
		}

		return apiResponse;
	
	}
	
	
	public ApiResponse getUnitsAllRemarks(String fromDate, String toDate) {

		ApiResponse apiResponse = new ApiResponse();

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			if (fromDate != null || toDate != null) {

				if (fromDate != null && toDate != null) {

					log.info("fromDate: " + fromDate);
					log.info("toDate: " + toDate);

					// Parse the strings into LocalDate
					LocalDate fDate = LocalDate.parse(fromDate, formatter);
					LocalDate tDate = LocalDate.parse(toDate, formatter);

					if (fDate.isBefore(tDate) || fDate.isEqual(tDate)) {
						
						List<Map<String, Object>> unit_Details = unitAllRemarksRepo.getUnitAllRemarksDataByLastEditedDate(
								CommonMethods.parseDateTime(fromDate + " 00:00:00"), CommonMethods.parseDateTime(toDate + " 23:59:59"));
						
						List<UnitAllRemarksResponse> unitDetails = new ArrayList<>();

						unitDetails = unit_Details.stream().map((Map<String, Object> unitDetail) -> {
							UnitAllRemarksResponse response = new UnitAllRemarksResponse();

							response.setUnitUniqueId((String) unitDetail.get("unit_unique_id"));
							response.setSurveyDate((String) unitDetail.get("survey_date"));
							response.setLastEditedDate((String) unitDetail.get("last_edited_date"));
							response.setImUploadStatus((String) unitDetail.get("imupload_status"));
							response.setDrpplEvaluationTeam1((String) unitDetail.get("drppl_evaluation_team1"));
							response.setDrpplEvaluationTeam2((String) unitDetail.get("drppl_evaluation_team2"));
							response.setDrpEvaluationTeam1((String) unitDetail.get("drp_evaluation_team1"));

							return response;
						}).distinct().collect(Collectors.toList());

						if (unitDetails == null) {
							log.info("unit list not found");
							apiResponse = new ApiResponse(new ApiResponse.Status(0, "404", "No Data Found"),
									unitDetails);
						} else if (unitDetails.isEmpty()) {
							log.info("unit list empty");
							apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"),
									unitDetails);
						} else {
							log.info("unitsList retrieved");
							apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), unitDetails);
						}
					} else {
						apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Range."),
								new ArrayList<>());
					}

				} else {
					apiResponse = new ApiResponse(
							new ApiResponse.Status(1, "404", "From Date and To Date Both are required or Both date will be null."),
							new ArrayList<>());
				}

			} else {
				
				List<Map<String, Object>> unit_Details = unitAllRemarksRepo.getAllData();
				
				List<UnitAllRemarksResponse> unitDetails = new ArrayList<>();

				unitDetails = unit_Details.stream().map((Map<String, Object> unitDetail) -> {
					UnitAllRemarksResponse response = new UnitAllRemarksResponse();

					response.setUnitUniqueId((String) unitDetail.get("unit_unique_id"));
					response.setSurveyDate((String) unitDetail.get("survey_date"));
					response.setLastEditedDate((String) unitDetail.get("last_edited_date"));
					response.setImUploadStatus((String) unitDetail.get("imupload_status"));
					response.setDrpplEvaluationTeam1((String) unitDetail.get("drppl_evaluation_team1"));
					response.setDrpplEvaluationTeam2((String) unitDetail.get("drppl_evaluation_team2"));
					response.setDrpEvaluationTeam1((String) unitDetail.get("drp_evaluation_team1"));

					return response;
				}).distinct().collect(Collectors.toList());

				if (unitDetails == null) {
					log.info("unit list not found");
					apiResponse = new ApiResponse(new ApiResponse.Status(0, "404", "No Data Found"), unitDetails);
				} else if (unitDetails.isEmpty()) {
					log.info("unit list empty");
					apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"), unitDetails);
				} else {
					log.info("unitsList retrieved");
					apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), unitDetails);
				}

			}
		} catch (DateTimeParseException e) {
			apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Format."), new ArrayList<>());
		}

		return apiResponse;
	
	}


	public Object generateToken() {
		Map<String, Object> tokenData = new HashMap<>();
        try {
        	String apiUrl = portalProperties.getTokenAuthUrl();
            
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String formData = portalProperties.getTokenFormData(); 

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = formData.getBytes("utf-8");
                os.write(input, 0, input.length);           
            }
            
            int responseCode = connection.getResponseCode();
	        log.info("request responseCode: " + responseCode);
	        
	        BufferedReader in;
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        } else {
	            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	        }

//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(response.toString(), Map.class);
            
            String token = (String) map.get("token");
            
            if(token != null) {
            	tokenData.put("token", map.get("token"));
				tokenData.put("expires (timestamp)", map.get("expires"));
				tokenData.put("expires", CommonMethods.formatDateString(map.get("expires").toString()));
				tokenData.put("ssl", map.get("ssl"));
            	
			} else {
				tokenData = map;
			}
            

        } catch (Exception e) {
            e.printStackTrace();
        }
		return tokenData;
    }
	
	
}
