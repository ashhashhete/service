package com.adani.drp.portal.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.SurveyProperties;
import com.adani.drp.portal.models.requests.UnitRequest;
import com.adani.drp.portal.models.responses.UnitListElement;
import com.adani.drp.portal.models.responses.UnitResponse;
import com.adani.drp.portal.repository.nxrtwo.UnitRepo;
import com.adani.drp.portal.models.responses.ApiResponse;
import com.adani.drp.portal.utils.CommonMethods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnitService {

	private final PortalProperties portalProperties;

	private final SurveyProperties surveyProperties;

	private final UnitRepo unitRepo;

	@PersistenceContext
	private final EntityManager entityManager;	
	
	public static String authenticate(String username, String password, PortalProperties portalProperties) {

	    log.info("UnitService ----> authenticate");
	    String res = "";
	    String authUrl = portalProperties.getAuthUrl();
	    String key = portalProperties.getKey();

	    try {
	        URL url = new URL(authUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	        log.info("ext url: " + authUrl);

	        // Setting request method to POST
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Secret-Key", key);

	        // Enabling input and output streams
	        conn.setDoOutput(true);

	        // Creating the JSON body
	        String jsonInputString = "{\"jsonrpc\": \"2.0\",\"params\": {\"username\": \"" + username + "\","
	                + " \"password\": \"" + DigestUtils.sha512Hex(password) + "\" }}";

	        log.info("input string: " + jsonInputString);

	        // Sending the request
	        try (OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }

	        // Reading the response
	        int responseCode = conn.getResponseCode();
	        log.info("request responseCode: " + responseCode);
	        
	        BufferedReader in;
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        
	        String inputLine;
	        StringBuilder response = new StringBuilder();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();

	        res = response.toString();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return res;
	}

	

}
