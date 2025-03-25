package com.adani.drp.portal.utils;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.responses.ApiResponse;
import com.adani.drp.portal.services.ExtAuthService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommonMethods {

	private CommonMethods() {
	}

	public static boolean verifyAuthentication(Map<String, String> headers, PortalProperties portalProperties) {
		if (headers == null || headers.isEmpty() || headers.get("access-token") == null
				|| headers.get("access-token").isEmpty()) {
			return false;
		}

		String accessToken = headers.get("access-token").contains("@")
				? headers.get("access-token").substring(0, headers.get("access-token").indexOf('@'))
				: headers.get("access-token");

		String inHouseAuthHeader = (headers.get("inhouse-autho") != null && !headers.get("inhouse-autho").isBlank())
				? headers.get("inhouse-autho").trim()
				: "";

		boolean isAuthorised;

		if (inHouseAuthHeader.equalsIgnoreCase("false")) {
			log.info("Calling odoo re-authentication service");
			ApiResponse authResp = ExtAuthService.callExternalService(accessToken, portalProperties);

			if (authResp.getStatus().getStatus() == 1 && authResp.getData() != null) {
				log.info("Odoo authentication successful: " + authResp.getData());
				isAuthorised = true;
			} else {
				log.error("Odoo authentication failed: " + authResp.getData());
				isAuthorised = false;
			}
		} else {
			log.info("Calling in-house authentication");
			isAuthorised = accessToken.equals(portalProperties.getExpectedAccessToken());
		}

		return isAuthorised;
	}
	
	public static Date cnvStrToDate(String dateString) {

		String dateFormatPattern = "yyyy-MM-dd";
		Date sqlDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);

		try {
			java.util.Date utilDate = dateFormat.parse(dateString);
			sqlDate = new Date(utilDate.getTime());
		} catch (ParseException e) {
			log.error("Error parsing the date: " + e.getMessage());
		}
		return sqlDate;
	}
	
	public static List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }
	
	public static java.util.Date parseDateTime(String dateString) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return formatter.parse(dateString);

		} catch (Exception e) {
			log.info("unable to parse date {} to sql date", dateString);
			return null;
		}
	}
	
	public static String formatDateString(String timestampString) {
	    if (timestampString == null || timestampString.isEmpty()) {
	        return "";
	    }

	    try {
	        long timestamp = Long.parseLong(timestampString);

	        // Convert the timestamp to Instant
	        Instant instant = Instant.ofEpochMilli(timestamp);

	        // Convert Instant to LocalDateTime in UTC timezone
	        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

	        // Define the desired date-time format
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	        // Format LocalDateTime to a string using the defined format
	        String formattedDateTime = dateTime.format(formatter);
	        return formattedDateTime;

	    } catch (Exception e) {
	        // Handle parsing exception
	        log.error("error parsing date timestamp");
	        return "null";
	    }
	}


}
