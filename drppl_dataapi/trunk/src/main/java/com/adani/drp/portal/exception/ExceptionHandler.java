package com.adani.drp.portal.exception;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
public class ExceptionHandler {

	private static ApiResponse apiResponse;

	public static ApiResponse handleException(Exception e, PortalProperties portalProperties) {


		log.info(e.getClass().getSimpleName());

		if (e.getCause() instanceof JDBCConnectionException) {
			return handleConnectionTimeout(e, portalProperties);

		} else if (e.getCause() instanceof NoSuchElementException) {
			return handleEmptyResponse(e, portalProperties);

		} else if (e.getClass().getName().contains("NullPointer")
				|| e.getCause().getMessage().contains("ResultSet closed")) {
			return handleEmptyResponse(e, portalProperties);

		} else if (e.getCause() instanceof InvalidDataAccessResourceUsageException) {
			return handleEmptyResponse(e, portalProperties);

		} else {
			return handleGeneralException(e, portalProperties);
		}
	}

	private static ApiResponse handleConnectionTimeout(Exception e, PortalProperties portalProperties) {
		apiResponse = new ApiResponse();
		log.trace("Connection timed out: {} {}", e);
		//log.error("Error Code - 117: {}", portalProperties.getFailureCodes().get(117),e);
		apiResponse = new ApiResponse(new ApiResponse.Status(0, "117", "Connection timed out"), null);

		return apiResponse;
	}

	private static ApiResponse handleEmptyResponse(Exception e, PortalProperties portalProperties) {
		apiResponse = new ApiResponse();
		log.trace("Error Code - 119: ", e);
		apiResponse = new ApiResponse(new ApiResponse.Status(0, "119", "Received empty response"), null);

		return apiResponse;

	}

	private static ApiResponse handleGeneralException(Exception e, PortalProperties portalProperties) {
		apiResponse = new ApiResponse();
		log.trace("Error Code - 118:", e);
		apiResponse = new ApiResponse(new ApiResponse.Status(0, "118", "General exception"), null);
		return apiResponse;

	}
}
