package com.adani.sra.connector.smsservice.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adani.sra.connector.smsservice.configurations.ExampleProperties;
import com.adani.sra.connector.smsservice.models.responses.ApiResponse;

import java.util.ArrayList;


@Slf4j
@Service
public class ExtAuthService {

    private ExtAuthService() {
    }

    public static ApiResponse callExternalService(String bearerToken, ExampleProperties exampleProperties) {
        ApiResponse apiResponse = new ApiResponse();
        try {

            // Define the endpoint URL
            String externalServiceUrl = exampleProperties.getExtReauthenticationApi();
            log.info("ext url: " ,externalServiceUrl);
            //"http://11.0.1.148:8089/odoo/api/private/token/reauthentication";

            // Create headers with the necessary information
            HttpHeaders headers = new HttpHeaders();
            headers.add("Secret-Key", exampleProperties.getExtAuthKey());
            //"VMuD8Ej6gT9naycKsgC0eZJeqYXmSgaZ");
            headers.add("Authorization", "Bearer " + bearerToken);

            log.info("ext headers: ",headers.toString());

            // Create an HttpEntity with the headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Make the HTTP POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    externalServiceUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Check if the response is successful
            if (response.getStatusCode().is2xxSuccessful()) {
                // If successful, return the response body
                log.info("unit attachment List retrieved");
                apiResponse = new ApiResponse(new ApiResponse.Status(1, "121", exampleProperties.getSuccessCodes().get(121)), response.getBody());
            }

        } catch (Exception e) {
            log.info("Unauthorized request!");
            apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", exampleProperties.getFailureCodes().get(401)), new ArrayList<>());
        }
        return apiResponse;
    }
}

