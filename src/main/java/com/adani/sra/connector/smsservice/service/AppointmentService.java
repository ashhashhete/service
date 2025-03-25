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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final Commons commons;

    private final ExampleProperties exampleProperties;

    private final SmsInfoService smsInfoService;

    public ApiResponse sendAppointment(AppointmentRequest appointmentRequest) {

        ApiResponse finalResp = new ApiResponse();
        OTPResponse appointmentMsg = new OTPResponse();
        appointmentMsg.setReceiver(appointmentRequest.getReceiver());

        URIBuilder uriBuilder;
        URI uri = null;
        String message="";
        try {
            uriBuilder = new URIBuilder(exampleProperties.getBaseUri());

            String decodedTemplate = commons.decodeUrl(exampleProperties.getAppointmentTemplateFormat());

            message = decodedTemplate.replace("HOHNAME", appointmentRequest.getHohName())
                    .replace("DATE", commons.convertDateStr(appointmentRequest.getDate()))
                    .replace("TIME", appointmentRequest.getTimeSlot());

            uriBuilder.setParameter("username", exampleProperties.getUsername())
                    .setParameter("password", exampleProperties.getPassword())
                    .setParameter("type", exampleProperties.getSmsType().get(1))
                    .setParameter("sender", exampleProperties.getSender())
                    .setParameter("mobile", appointmentRequest.getReceiver())
                    .setParameter("message", message)
                    .setParameter("PEID", exampleProperties.getPeid())
                    .setParameter("HeaderId", exampleProperties.getHeaderId())
                    .setParameter("templateId", exampleProperties.getAppointmentTemplateId());

            uri = uriBuilder.build();

        } catch (URISyntaxException e) {

        }

        finalResp = commons.sendRequest(exampleProperties.getSmsType().get(1), uri);

        if (finalResp.getStatus().code.equals("121")) {
            log.info("received response content, processing further");

            finalResp = processAppointmentResponse(appointmentRequest, finalResp.getStatus().message, message);
        }

        return finalResp;

    }

    public ApiResponse processAppointmentResponse(AppointmentRequest appointmentRequest, String content,String message) {
        ApiResponse finalResp = new ApiResponse();
        OTPResponse otpResponse = new OTPResponse();
        List<String> resultList = Arrays.stream(content.split("\\|"))
                .map(String::trim)
                .collect(Collectors.toList());


        finalResp.setStatus(new ApiResponse.Status(1, "121", resultList.get(0)));
        otpResponse.setTransactionId(resultList.get(1));
        otpResponse.setReceiver(resultList.get(2));
        finalResp.setData(otpResponse);
        log.info(finalResp.toString());

//        SMSInfo smsInfo = new SMSInfo();
        SMSTrack smsTrack = new SMSTrack();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        Date bookingDate = calendar.getTime();


        UUID uuid = UUID.randomUUID();
        String globalid = "{" + uuid.toString().toUpperCase() + "}";
        log.info("globalid : " + globalid);

//        smsInfo.setReceiver(appointmentRequest.getReceiver());
//        smsInfo.setSmsType("appointment");
//        smsInfo.setReceiver(appointmentRequest.getReceiver());
//        smsInfo.setHohName(appointmentRequest.getHohName());
//        smsInfo.setSurveyId(appointmentRequest.getSurveyId());
//        smsInfo.setBoothId(appointmentRequest.getBoothId());
//        smsInfo.setBoothAddress(appointmentRequest.getBoothAddress());
//        smsInfo.setBookingDate(bookingDate);
//        smsInfo.setCreatedDate(new Date());
//        smsInfo.setTransactionId(otpResponse.getTransactionId());
//        smsInfo.setGlobalId(globalid);

        smsTrack.setSmsSource("appointment");
        smsTrack.setSendTo(appointmentRequest.getReceiver());
        smsTrack.setUnitUniqueId(appointmentRequest.getSurveyId());
        //smsTrack.setCreatedBy(appointmentRequest.getUserId());
        smsTrack.setResponseCode("");
        smsTrack.setResponseContent("");
        smsTrack.setSent(true);
        smsTrack.setSmsContent(message);
        smsTrack.setCreatedDate(new Date());
        smsTrack.setSentDate(new Date());
        smsTrack.setTransactionId(otpResponse.getTransactionId());

        log.info("saving sms Transaction");
//        SMSInfo smsInfo1 = smsInfoService.saveSMSInfo(smsInfo);
        SMSTrack smsInfo1 = smsInfoService.saveSMSInfo(smsTrack);

        if (smsInfo1.toString() != null || smsInfo1.toString() != "") {
            log.info("sms Transaction saved");

        } else {
            log.error("error saving sms transaction");
        }

        return finalResp;

    }

}
