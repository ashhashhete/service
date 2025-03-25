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
import com.adani.drp.portal.models.requests.RPAValidationsRequest;
import com.adani.drp.portal.models.requests.UnitRequest;
import com.adani.drp.portal.models.responses.ApiResponse;
import com.adani.drp.portal.services.RPAServiceImpl;
import com.adani.drp.portal.utils.CommonMethods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rpaValidations")
public class RPAValidationsController {
	
	private ApiResponse apiResponse;
	
	private final PortalProperties portalProperties;
	
	private final RPAServiceImpl rpaServiceImpl;
	
	@GetMapping("/")
    public String home() {
        log.info("Welcome to DRPPL ! ");
        return "Welcome to DRPPL RPA Module";
    }
	
	@PostMapping("/insertRPAData")
	public Object insertRPAData(@RequestHeader Map<String, String> headers, @RequestBody RPAValidationsRequest rpaValidationsRequest) {
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			return new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			return rpaServiceImpl.insertRPAData(rpaValidationsRequest);
		}
	}
	
	@PostMapping("/updateRPAData")
	public Object updateRPAData(@RequestHeader Map<String, String> headers, @RequestBody RPAValidationsRequest rpaValidationsRequest) {
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			return new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			return rpaServiceImpl.updateRPAData(rpaValidationsRequest);
		}
	}
	
	@PostMapping("/deleteRPAData")
	public Object deleteRPAData(@RequestHeader Map<String, String> headers, @RequestBody String jsonString) {
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			return new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			return rpaServiceImpl.deleteRPAData(jsonString);
		}
	}
	
	@PostMapping("/getRPAData")
	public Object getRPAData(@RequestHeader Map<String, String> headers, @RequestBody String jsonString) {
		boolean verified = CommonMethods.verifyAuthentication(headers, portalProperties);

		if (!verified) {
			log.info("Unauthorized request!");
			return new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		} else {
			return rpaServiceImpl.getRPAData(jsonString);
		}
	}
	
	@PostMapping("/getALLRPAData")
	public ApiResponse getALLRPAData(@RequestHeader Map<String, String> headers,
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
			
			return rpaServiceImpl.getALLRPAData(fromDate, toDate);

		}

		return apiResponse;

	}
	
	@GetMapping("/getALLRPAData")
	public ApiResponse getALLRPAData(@RequestHeader Map<String, String> headers,
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
			return rpaServiceImpl.getALLRPAData(fromDate, toDate);

		}

		return apiResponse;

	}
	
	
	
}
