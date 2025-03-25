package com.adani.drp.portal.controllers.nxrtwo;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.requests.LoginRequest;
import com.adani.drp.portal.models.requests.RPAValidationsRequest;
import com.adani.drp.portal.models.requests.UnitRequest;
import com.adani.drp.portal.models.responses.ApiResponse;
import com.adani.drp.portal.services.RPAServiceImpl;
import com.adani.drp.portal.services.UnitService;
import com.adani.drp.portal.utils.CommonMethods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/rpaValidations")
public class RPAValidationsController {
	
	private ApiResponse apiResponse;
	
	private final PortalProperties portalProperties;
	
	private final RPAServiceImpl rpaServiceImpl;
	
	private final UnitService unitService;
	
	@GetMapping("/")
    public String home() {
        log.info("Welcome to DRPPL ! ");
        return "Welcome to DRPPL Data API Module";
    }
	
	@PostMapping("/authenticate")
	public String authenticate(@RequestBody LoginRequest authenticationRequest) {

		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

		String response = unitService.authenticate(username, password, portalProperties);
		return response;
	}
	
	@PostMapping("/getALLRPAData")
	public ApiResponse getALLRPAData(@RequestHeader Map<String, String> headers,
			@RequestBody UnitRequest unitRequest) {

		apiResponse = new ApiResponse();
		log.info("getALLRPAData : POST CALL");

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
			
			return rpaServiceImpl.getALLRPAData(fromDate, toDate);

		}

		return apiResponse;

	}
	
	@GetMapping("/getALLRPAData")
	public ApiResponse getALLRPAData(@RequestHeader Map<String, String> headers,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate) {

		apiResponse = new ApiResponse();
		log.info("getALLRPAData : GET CALL");
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			return rpaServiceImpl.getALLRPAData(fromDate, toDate);

		}

		return apiResponse;

	}
	
	@PostMapping("/getUnitsAllRemarks")
	public ApiResponse getUnitsAllRemarks(@RequestHeader Map<String, String> headers,
			@RequestBody UnitRequest unitRequest) {

		apiResponse = new ApiResponse();
		log.info("getUnitsAllRemarks : POST CALL");

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
			
			return rpaServiceImpl.getUnitsAllRemarks(fromDate, toDate);

		}

		return apiResponse;

	}
	
	@GetMapping("/getUnitsAllRemarks")
	public ApiResponse getUnitsAllRemarks(@RequestHeader Map<String, String> headers,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate) {

		apiResponse = new ApiResponse();
		log.info("getUnitsAllRemarks : GET CALL");
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			return rpaServiceImpl.getUnitsAllRemarks(fromDate, toDate);

		}

		return apiResponse;

	}
	
	@GetMapping("/generateToken")
	public Object generateToken(@RequestHeader Map<String, String> headers) {
		ApiResponse apiResponse = new ApiResponse();
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {

			return rpaServiceImpl.generateToken();
		}
		
		return apiResponse;
	}
	
}
