package com.adani.sra.connector.smsservice.service;


import com.adani.sra.connector.smsservice.Entity.OTPTrack;
import com.adani.sra.connector.smsservice.Entity.SMSTrack;
import com.adani.sra.connector.smsservice.configurations.ExampleProperties;
import com.adani.sra.connector.smsservice.models.requests.otpModels.OTPRequest;
import com.adani.sra.connector.smsservice.models.requests.otpModels.VerifyOTPRequest;
import com.adani.sra.connector.smsservice.models.responses.ApiResponse;
import com.adani.sra.connector.smsservice.models.responses.OTPResponse;
import com.adani.sra.connector.smsservice.repository.OTPTrackRepo;
import com.adani.sra.connector.smsservice.utils.Commons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPService {

    private final OTPTrackRepo otpTrackRepo;

    private final Commons commons;

    private final ExampleProperties exampleProperties;

    private final SmsInfoService smsInfoService;
    private boolean isOTPVarified;

    public ApiResponse sendOtp(OTPRequest otpRequest) {

        ApiResponse finalResp = new ApiResponse();

        OTPResponse otpResponse = new OTPResponse();
        otpResponse.setReceiver(otpRequest.getReceiver());
        finalResp.setData(otpResponse);

        URIBuilder uriBuilder;
        URI uri = null;
        String otp = new String();
        String message="";
        try {
            uriBuilder = new URIBuilder(exampleProperties.getBaseUri());

            otp = commons.generateOtp(exampleProperties.getOtpDigit());

            message = commons.decodeUrl(exampleProperties.getOtpSmsTemplateFormat())
                    .replace("{#var1#}", otpRequest.getHohName())
                    .replace("{#var2#}", otp);
//            String message = commons.decodeUrl(exampleProperties.getOtpSmsTemplateFormat()
//                    .replace("HOHNAME",otpRequest.getHohName())
//                    .replace("OTP", otp);

            uriBuilder.setParameter("username", exampleProperties.getUsername())
                    .setParameter("password", exampleProperties.getPassword())
                    .setParameter("type", exampleProperties.getSmsType().get(1))
                    .setParameter("sender", exampleProperties.getSender())
                    .setParameter("mobile", otpRequest.getReceiver())
                    .setParameter("message", message)
                    .setParameter("PEID", exampleProperties.getPeid())
                    .setParameter("HeaderId", exampleProperties.getHeaderId())
                    .setParameter("templateId", exampleProperties.getOtpSmsTemplateId());

            uri = uriBuilder.build();

        } catch (URISyntaxException e) {

        }

        finalResp = commons.sendRequest(exampleProperties.getSmsType().get(0), uri);

        if (finalResp.getStatus().code.equals("121")) {
            log.info("received response content, processing further");
            finalResp = processOTPResponse(otpRequest, finalResp.getStatus().message, otp, message);
        }

        return finalResp;

    }

    public ApiResponse processOTPResponse(OTPRequest otpRequest, String content, String otp,String message) {

        ApiResponse finalResp = new ApiResponse();
        UUID uuid = UUID.randomUUID();
        String globalid = "{" + uuid.toString().toUpperCase() + "}";

        List<String> resultList = Arrays.stream(content.split("\\|"))
                .map(String::trim)
                .collect(Collectors.toList());

        finalResp.setStatus(new ApiResponse.Status( 1, "121", resultList.get(0)));
        OTPResponse otpResponse = new OTPResponse();
        otpResponse.setTransactionId(resultList.get(1)); // main param
        otpResponse.setReceiver(resultList.get(2));
        finalResp.setData(otpResponse);

        log.info(finalResp.toString());

//        SMSInfo smsInfo = new SMSInfo();
//        smsInfo.setReceiver(otpRequest.getReceiver());
//        smsInfo.setSmsType("OTP");
//        smsInfo.setReceiver(otpRequest.getReceiver());
//        smsInfo.setHohName(otpRequest.getHohName());
//        smsInfo.setSurveyId(otpRequest.getSurveyId());
//        smsInfo.setSentOtp(otp);
//        smsInfo.setRelManagerNo(otpRequest.getRelationShipManagerNo());
//        smsInfo.setCreatedDate(new Date());
//        smsInfo.setTransactionId(otpResponse.getTransactionId());
//        smsInfo.setBoothId(null);
//        smsInfo.setBoothAddress(null);
//        smsInfo.setBookingDate(null);
//        smsInfo.setCreatedDate(new Date());
//        smsInfo.setGlobalId(globalid);
//        log.info(smsInfo.toString());

        SMSTrack smsTrack = new SMSTrack();
        smsTrack.setSmsSource("OTP");
        smsTrack.setSendTo(otpRequest.getReceiver());
        smsTrack.setSmsContent(message);
        smsTrack.setSent(true);
        smsTrack.setSentDate(new Date());
        smsTrack.setTransactionId(otpResponse.getTransactionId());
        smsTrack.setResponseCode("");
        smsTrack.setResponseContent("");
        smsTrack.setUnitUniqueId(otpRequest.getSurveyId());
        smsTrack.setCreatedDate(new Date());
       // smsTrack.setCreatedBy(otpRequest.getUserId());

        log.info("saving sms Transaction");
        try {
//            SMSInfo smsInfo1 = smsInfoService.saveSMSInfo(smsInfo);
            SMSTrack sms = smsInfoService.saveSMSInfo(smsTrack);

            OTPTrack otpTrack = new OTPTrack();
            otpTrack.setOtpSent(otp);
            otpTrack.setOtpDate(new Date());
            otpTrack.setAttemptNo(0);
            otpTrack.setSmsTrackId(sms.getObjectId());

            otpTrackRepo.save(otpTrack);
            log.info("sms Transaction saved");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("error saving sms transaction");
        }


        return finalResp;

    }

    public ApiResponse verifyOTP(VerifyOTPRequest verifyOTPRequest) {
        ApiResponse finalResp = new ApiResponse();
        SMSTrack fetchSMSInfo = smsInfoService.getSMSInfoByTransactionId(verifyOTPRequest.getTransactionId());
        OTPTrack otpData;
        boolean isOTPVarified;
        if (fetchSMSInfo == null) {
            return new ApiResponse(new ApiResponse.Status(0, "122", exampleProperties.getFailureCodes().get(122)), new ArrayList<>());
        } else {
            otpData = otpTrackRepo.findBySmsTrackId(fetchSMSInfo.getObjectId());
            if(otpData.isSuccess()){
                return new ApiResponse(new ApiResponse.Status(0, "126", exampleProperties.getFailureCodes().get(126)), new ArrayList<>());
            }
            Duration duration = Duration.between(otpData.getOtpDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now());
            if (duration.toMinutes() > 10) {
                return new ApiResponse(new ApiResponse.Status(0, "124", exampleProperties.getFailureCodes().get(124)), new ArrayList<>());
            }
            if (otpData.getAttemptNo()  >= 3) {
                return new ApiResponse(new ApiResponse.Status(0, "123", exampleProperties.getFailureCodes().get(123)), new ArrayList<>());
            }
            log.info("fetched smsInfo: {}", fetchSMSInfo);

            String senderNo = verifyOTPRequest.getSender().toString();
            log.info("senderNo: {}", senderNo);
            String receiverNo = fetchSMSInfo.getSendTo().toString();
            log.info("receiverNo: {}", receiverNo);

            String senderOTP = verifyOTPRequest.getOtp().toString();
            log.info("senderOTP: {}", senderOTP);
            String storedOTP = otpData.getOtpSent().toString();
            log.info("storedOTP: {}", storedOTP);


            String msg = "";
            if (Objects.equals(senderNo, receiverNo) &&
                    Objects.equals(senderOTP, storedOTP)) {

                isOTPVarified = true;
                msg = "OTP is valid";
            } else {
                isOTPVarified = false;
                msg = "OTP is invalid, try again";
            }
            OTPTrack otp = new OTPTrack();
//            otpData.setOtpSent(otpData.getOtpSent());
            otpData.setOtpReceived(senderOTP);
            otpData.setSuccess(isOTPVarified);
//            otpData.setOtpDate(otpData.getOtpDate());
            otpData.setAttemptNo(otpData.getAttemptNo()+1);
//            otpData.setSmsTrackId(otpData.getSmsTrackId());
            otpTrackRepo.save(otpData);

//            boolean status = smsInfoService.updateOtpAndVerificationStatus(senderOTP, isOTPVarified, verifyOTPRequest.getTransactionId(), fetchSMSInfo.getAttemptNo());

//            String result = status ? msg : "error updating varification try again";

            return new ApiResponse(new ApiResponse.Status(1, "121", msg), new ArrayList<>());
        }
    }


}
