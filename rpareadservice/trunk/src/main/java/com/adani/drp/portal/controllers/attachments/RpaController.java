package com.adani.drp.portal.controllers.attachments;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.requests.AttachmentRequest;
import com.adani.drp.portal.models.requests.LoginRequest;
import com.adani.drp.portal.models.requests.UnitRequest;
import com.adani.drp.portal.models.responses.UnitResponse;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.services.UnitService;
import com.adani.drp.portal.utils.CommonMethods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RpaController {

	private ApiResponse apiResponse;

	private final UnitService unitService;

	private final PortalProperties portalProperties;

	@GetMapping("/")
	public String home() {
		log.info("Welcome to DRPPL ! ");
		return "Welcome to DRPPL...";
	}

	@PostMapping("/getUnitsBySurveyDate")
	public ApiResponse getUnitsBySurveyDate(@RequestHeader Map<String, String> headers,
			@RequestBody UnitRequest unitRequest) {

		apiResponse = new ApiResponse();

		String fromDate = unitRequest.getFromDate();
		String toDate = unitRequest.getToDate();
		int offset = unitRequest.getOffset();
		int limit = unitRequest.getLimit();

		log.info("fromDate: " + fromDate);
		log.info("toDate: " + toDate);
		log.info("offset: " + offset);
		log.info("limit: " + limit);

		if (limit > 100) {
			limit = 100;
		}

		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			try {
				// Parse the strings into LocalDate
				LocalDate fDate = LocalDate.parse(fromDate, formatter);
				LocalDate tDate = LocalDate.parse(toDate, formatter);

				// Check if fromDate is before toDate
				if (fDate.isBefore(tDate) || fDate.isEqual(tDate)) {
					UnitResponse unitResponse = unitService.getUnitsBySurveyDate(fromDate, toDate, offset, limit);

					if (unitResponse.getUnits() == null) {
						log.info("unit list not found");
						unitResponse.setUnits(new ArrayList<>());
						apiResponse = new ApiResponse(new ApiResponse.Status(0, "404", "No Data Found"), unitResponse);

					} else if (unitResponse.getUnits().isEmpty()) {
						log.info("unit list empty");
						unitResponse.setUnits(new ArrayList<>());
						apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"), unitResponse);

					} else {
						log.info("unitsList retrieved");
						apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), unitResponse);

					}

				} else {
					apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Range."),
							new ArrayList<>());
				}
			} catch (DateTimeParseException e) {
				apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Format."),
						new ArrayList<>());
			}

		}

		return apiResponse;

	}

	@PostMapping("/getAttachmentsBySurveyID")
	public ApiResponse getAttachmentsBySurveyID(@RequestHeader Map<String, String> headers,
			@RequestBody AttachmentRequest attRequest) throws IOException {

		apiResponse = new ApiResponse();

		String uniqueSurveyId = attRequest.getUniqueSurveyId();
		System.out.println("Unique Survey ID: " + uniqueSurveyId);

		// Fetching the optional documentSource
		String documentSource = attRequest.getDocumentSource().orElse(null);
		System.out.println("Document Source: " + documentSource);

		// Fetching the optional documentCategory
		String documentCategory = attRequest.getDocumentCategory().orElse(null);
		System.out.println("Document Category: " + documentCategory);

		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {

			Map<String, Object> attResponse = unitService.getAttachmentsBySurveyID(uniqueSurveyId, documentSource,
					documentCategory);

			if (attResponse.isEmpty()) {
				apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"), attResponse);

			} else {
				apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), attResponse);
			}
		}

		return apiResponse;

	}
	
	@PostMapping("/getAttBySurveyID")
	public ApiResponse getAttBySurveyID(@RequestHeader Map<String, String> headers,
			@RequestBody AttachmentRequest attRequest) throws IOException {

		apiResponse = new ApiResponse();

		String uniqueSurveyId = attRequest.getUniqueSurveyId();
		System.out.println("Unique Survey ID: " + uniqueSurveyId);

		// Fetching the optional documentSource
		String documentSource = attRequest.getDocumentSource().orElse(null);
		System.out.println("Document Source: " + documentSource);

		// Fetching the optional documentCategory
		String documentCategory = attRequest.getDocumentCategory().orElse(null);
		System.out.println("Document Category: " + documentCategory);

		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {

			Map<String, Object> attResponse = unitService.getAttBySurveyID(uniqueSurveyId, documentSource,
					documentCategory);

			if (attResponse.isEmpty()) {
				apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"), attResponse);

			} else {
				apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), attResponse);
			}
		}

		return apiResponse;

	}

	@GetMapping("/getPasswordEncrypted")
	public String getPasswordEncrypted(@RequestParam String password) {

		// Encrypt the password using SHA-512
		String sha512hex = DigestUtils.sha512Hex(password);

		// Return the encrypted password
		return sha512hex;
	}

	@PostMapping("/authenticate")
	public String authenticate(@RequestBody LoginRequest authenticationRequest) {

		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

		String response = unitService.authenticate(username, password, portalProperties);
		return response;
	}
	
	@GetMapping("/getReprocessIDs")
	public ApiResponse getReprocessIDs(@RequestHeader Map<String, String> headers,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate) {

		apiResponse = new ApiResponse();
		log.info("getReprocessIDs : GET CALL");
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			return unitService.getReprocessIDs(fromDate, toDate);

		}

		return apiResponse;

	}
	
	@PostMapping("/getReprocessIDs")
	public ApiResponse getReprocessIDs(@RequestHeader Map<String, String> headers,
			@RequestBody UnitRequest unitRequest) {

		apiResponse = new ApiResponse();
		log.info("getReprocessIDs : POST CALL");

		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			
			String fromDate = unitRequest.getFromDate();
			String toDate = unitRequest.getToDate();

			log.info("fromDate: " + fromDate);
			log.info("toDate: " + toDate);
			
			return unitService.getReprocessIDs(fromDate, toDate);

		}

		return apiResponse;

	}

}
