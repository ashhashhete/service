package com.adani.drp.portal.services;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


@Slf4j
@Service
public class ExtAuthService {

    private ExtAuthService() {
    }

    public static ApiResponse callExternalService(String bearerToken, PortalProperties portalProperties) {
        ApiResponse apiResponse = new ApiResponse();
        try {

            String externalServiceUrl = portalProperties.getExtReauthenticationApi();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Secret-Key", portalProperties.getExtAuthKey());
            headers.add("Authorization", "Bearer " + bearerToken);

            log.info("ext url: {}" ,externalServiceUrl);
            log.info("ext headers: {}",headers.toString().replace(portalProperties.getExtAuthKey(),"XXXX"));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(
                    externalServiceUrl,
                    HttpMethod.POST,
                    entity,
                    String.class

            );

            log.info("request sent successfully {}" ,entity.toString().replace(portalProperties.getExtAuthKey(),"XXXX"));

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("ext token retrieved");
                apiResponse = new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)), response.getBody());
            }

        } catch (Exception e) {
            log.trace("Unauthorized request!", e);
            apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)), new ArrayList<>());
        }
        return apiResponse;
    }
}

