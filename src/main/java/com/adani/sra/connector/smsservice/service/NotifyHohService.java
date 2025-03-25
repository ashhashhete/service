package com.adani.sra.connector.smsservice.service;


import com.adani.sra.connector.smsservice.Entity.SMSTrack;
import com.adani.sra.connector.smsservice.configurations.ExampleProperties;
import com.adani.sra.connector.smsservice.models.requests.appointmentModels.AppointmentRequest;
import com.adani.sra.connector.smsservice.models.responses.ApiResponse;
import com.adani.sra.connector.smsservice.models.responses.OTPResponse;
import com.adani.sra.connector.smsservice.utils.Commons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyHohService {

    private final Commons commons;

    private final ExampleProperties exampleProperties;

    private final SmsInfoService smsInfoService;

    private static final Pattern INDIAN_MOBILE_NUMBER_PATTERN = Pattern.compile("^[6-9]\\d{9}$");


    public ApiResponse initNotification(MultipartFile file,String language){
        try {
            Set<String> verifiedNumbers = new HashSet<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] numbers = line.split(",");
                    for (String number : numbers) {
                        number = number.trim();
                        if (INDIAN_MOBILE_NUMBER_PATTERN.matcher(number).matches()) {
                            verifiedNumbers.add(number);
                        }
                    }
                }
            }

            List<String> chunks = new ArrayList<>();
            List<String> currentChunk = new ArrayList<>(200);
            for (String number : verifiedNumbers) {
                currentChunk.add(number);
                if (currentChunk.size() == 200) {
                    chunks.add(String.join(",", currentChunk));
                    currentChunk.clear();
                }
            }
            if (!currentChunk.isEmpty()) {
                chunks.add(String.join(",", currentChunk));
            }

            // Assuming you have a method in notifyHohService to handle the chunks
            return notifyHoh(chunks,language);

        } catch (Exception e) {
            log.error("Error processing file", e);
            return new ApiResponse(new ApiResponse.Status(0, "118", "error processing .csv file"), new ArrayList<>());
        }
    }

    public ApiResponse notifyHoh( List<String> chunks,String language) {

        ApiResponse finalResp = new ApiResponse();
        OTPResponse appointmentMsg = new OTPResponse();
        //appointmentMsg.setReceiver(appointmentRequest.getReceiver());

        for (String chunk : chunks) {
            URIBuilder uriBuilder;
            URI uri = null;
            String message = "";
            try {

            uriBuilder = new URIBuilder(exampleProperties.getBulkBaseUri());

            if (language.equalsIgnoreCase("hindi")) {

                message = commons.decodeUrl(exampleProperties.getNotifyHohHindiTemplateFormat());

                uriBuilder.setParameter("username", exampleProperties.getUsername())
                        .setParameter("password", exampleProperties.getPassword())
                        .setParameter("type", exampleProperties.getSmsType().get(3))
                        .setParameter("sender", exampleProperties.getSender())
                        .setParameter("mobile", chunk)
                        .setParameter("message", message)
                        .setParameter("PEID", exampleProperties.getPeid())
                        .setParameter("HeaderId", exampleProperties.getHeaderId())
                        .setParameter("templateId", exampleProperties.getNotifyHohHindiTemplateId());

            } else if(language.equalsIgnoreCase("marathi") ){

                message = commons.decodeUrl(exampleProperties.getNotifyHohMarathiTemplateFormat());

                uriBuilder.setParameter("username", exampleProperties.getUsername())
                        .setParameter("password", exampleProperties.getPassword())
                        .setParameter("type", exampleProperties.getSmsType().get(3))
                        .setParameter("sender", exampleProperties.getSender())
                        .setParameter("mobile", chunk)
                        .setParameter("message", message)
                        .setParameter("PEID", exampleProperties.getPeid())
                        .setParameter("HeaderId", exampleProperties.getHeaderId())
                        .setParameter("templateId", exampleProperties.getNotifyHohMarathiTemplateId());
            }

            uri = uriBuilder.build();

        } catch (URISyntaxException e) {
                return new ApiResponse(new ApiResponse.Status(0, "118", "error building url for " + language + " template" ), new ArrayList<>());

        }

        finalResp = commons.sendRequest(exampleProperties.getSmsType().get(3), uri);
    }

        return finalResp;

    }





}
