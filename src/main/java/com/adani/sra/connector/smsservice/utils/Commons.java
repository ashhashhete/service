package com.adani.sra.connector.smsservice.utils;


import com.adani.sra.connector.smsservice.configurations.ExampleProperties;
import com.adani.sra.connector.smsservice.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Commons {

    private final ExampleProperties exampleProperties;

    public ApiResponse sendRequest(String identifier, URI uri) {
//        FinalResp finalResp = new FinalResp();
        ApiResponse finalResp = new ApiResponse();
        Integer statusCode;

        Integer conn = exampleProperties.getConnectionTimeout();
        Integer soc = exampleProperties.getReadTimeout();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(conn)
                .setSocketTimeout(soc)
                .build();


        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("Content-type", "application/json;charset=utf-8");

            log.info("Params to be sent to Analytix mantra: Headers " + Arrays.toString(httpGet.getAllHeaders()));
            log.info("Params to be sent to Analytix mantra: request params " + httpGet);

            try (CloseableHttpResponse apiResponse = httpClient.execute(httpGet);
                 BufferedReader rd = new BufferedReader(
                         new InputStreamReader(apiResponse.getEntity().getContent()))) {
                String httpStatusCode = String.valueOf(apiResponse.getStatusLine().getStatusCode());

                String content = rd.lines().collect(Collectors.joining()).trim();

                if (httpStatusCode.equals(String.valueOf(HttpStatus.SC_OK))) {

                    log.info(" SUCCESS: SENT request to service, ");

                    if (content.isEmpty() ) {

//                        finalResp.getStatus().setStatus(119);
//                        finalResp.getStatus().setMessage(exampleProperties.getEscalatedCodes().get(119) +" " + identifier + "  Escalating");
                        finalResp.setStatus(new ApiResponse.Status(0, "119",   identifier + ": "+ exampleProperties.getEscalatedCodes().get(119)  + " ,Escalating"));
                        log.error(finalResp.toString());
                        return finalResp;

                    } else if (!content.contains("SUBMIT_SUCCESS") ) {

                        finalResp.setStatus(new ApiResponse.Status(0, "125",  identifier + ": " +exampleProperties.getEscalatedCodes().get(125) + " " + content +  " Escalating"));
                        log.error(finalResp.toString());
                        return finalResp;

                    } else {
                        log.info("received response for serviceName {}: {}", identifier, content);

//                        finalResp.getStatus().setStatus(1);
//                        finalResp.getStatus().setMessage("121");
//                        finalResp.getStatus().setMessage(content);
                        finalResp.setStatus(new ApiResponse.Status(1, "121",identifier + ": " + content));
                        return finalResp;

                    }
                } else {
                    log.error("The HTTP Status returned is different from success, for serviceName " + identifier + " HTTP Status is: "
                            + httpStatusCode + " with response " + content);

                    statusCode = 111;
                    //exampleProperties.getEscalatedStatus();

//                    finalResp.getStatus().setStatus(statusCode);
//                    finalResp.getStatus().setMessage("The HTTP Status returned differs from success status for serviceName:: returned status " + httpStatusCode);
                    finalResp.setStatus(new ApiResponse.Status(0, "111", "The HTTP Status returned differs from success status for serviceName:: returned status " + httpStatusCode));
                    log.error(finalResp.toString());
                }
                return finalResp;

            }

        } catch (ConnectTimeoutException ctEx) {

            log.error(ctEx.getClass().getSimpleName() + " Connection timeout encountered while pushing for serviceName: " + " " + ctEx.getMessage());
//            finalResp.setStatus();
//            finalResp.getStatus().setStatus(117);
//            finalResp.getStatus().setMessage(exampleProperties.getFailureCodes().get(117));
            finalResp.setStatus(new ApiResponse.Status(0, "" +
                    "117", exampleProperties.getFailureCodes().get(117)));
            log.error(finalResp.toString());
            return finalResp;

        } catch (SocketTimeoutException stEx) {

            log.error(stEx.getClass().getSimpleName() + " socket timeout encountered while pushing serviceName: " + " " + stEx.getMessage());
//            finalResp.getStatus().setStatus(114);
//            finalResp.getStatus().setMessage(exampleProperties.getEscalatedCodes().get(114));
            finalResp.setStatus(new ApiResponse.Status(0, "114", exampleProperties.getEscalatedCodes().get(114)));
            log.error(finalResp.toString());
            return finalResp;

        } catch (Exception e) {

            log.error(e.getClass().getSimpleName() + "An error occurred while pushing serviceName: " + identifier + e.getMessage());
            if (finalResp.getStatus().getMessage() == null) {
//                finalResp.getStatus().setStatus(118);
//                finalResp.getStatus().setMessage(exampleProperties.getEscalatedCodes().get(118) + " " + identifier);
                finalResp.setStatus(new ApiResponse.Status(0, "118", exampleProperties.getEscalatedCodes().get(118) + " " + identifier));
            }
            log.error(finalResp.toString());
            return finalResp;

        }

    }


    public String generateOtp(int len) {
        log.info("Generating OTP using random() : ");
        log.info("You OTP is : ");

        String numbers = "0123456789";
        Random rndm = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] =
                    numbers.charAt(rndm.nextInt(numbers.length()));
        }

        String result = new String(otp);
        log.info("otp is: {}", result);
        return result;


    }

    public static String encodeUrl(String input) {
        try {
            var encodedMsg = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
            log.info("encodedMsg: {}", encodedMsg);

            return encodedMsg;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding URL", e);
        }
    }

    public static String decodeUrl(String input) {
        try {
            var decodedTemplate = URLDecoder.decode(input, StandardCharsets.UTF_8.toString());
            log.info("decodedTemplate: {}", decodedTemplate);

            return decodedTemplate;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error decoding URL", e);
        }
    }


    public static boolean verifyAuthentication(Map<String, String> headers, ExampleProperties exampleProperties) {
        if (headers == null || headers.isEmpty() || headers.get("access-token") == null || headers.get("access-token").isEmpty()) {
            return false;
        }

        String accessToken = headers.get("access-token").contains("@") ?
                headers.get("access-token").substring(0, headers.get("access-token").indexOf('@')) :
                headers.get("access-token");

        String inHouseAuthHeader = (headers.get("inhouse-autho") != null && !headers.get("inhouse-autho").isBlank()) ?
                headers.get("inhouse-autho").trim() : "";

        boolean isAuthorised;

        if (inHouseAuthHeader.equalsIgnoreCase("false")) {
            log.info("Calling odoo re-authentication service");
            ApiResponse authResp = ExtAuthService.callExternalService(accessToken, exampleProperties);

            if (authResp.getStatus().getStatus() == 1 && authResp.getData() != null) {
                log.info("Odoo authentication successful: " + authResp.getData());
                isAuthorised = true;
            } else {
                log.error("Odoo authentication failed: " + authResp.getData());
                isAuthorised = false;
            }
        } else {
            log.info("Calling in-house authentication");
            isAuthorised = accessToken.equals(exampleProperties.getExpectedAccessToken());
        }

        return isAuthorised;
    }

    public String convertDateStr(String inputDate) {
        // Parse input date string
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);

        // Format the date as "Month Day, Year"
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM d'" + getDaySuffix(date.getDayOfMonth()) + "' yyyy");
        return date.format(outputFormatter);
    }

    public String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }


}


