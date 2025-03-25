package com.adani.sra.connector.smsservice.controllers;

import com.adani.sra.connector.smsservice.configurations.ExampleProperties;
import com.adani.sra.connector.smsservice.models.requests.appointmentModels.AppointmentRequest;
import com.adani.sra.connector.smsservice.models.requests.otpModels.OTPRequest;
import com.adani.sra.connector.smsservice.models.requests.otpModels.VerifyOTPRequest;
import com.adani.sra.connector.smsservice.models.responses.ApiResponse;
import com.adani.sra.connector.smsservice.service.AppointmentService;
import com.adani.sra.connector.smsservice.service.NotifyHohService;
import com.adani.sra.connector.smsservice.service.OTPService;
import com.adani.sra.connector.smsservice.utils.Commons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SMSController {

    private final OTPService otpService;

    private final AppointmentService appointmentService;

    private final ExampleProperties exampleProperties;

    private final NotifyHohService notifyHohService;

    @PostMapping("/sendOTP") // api used on mobile apk to send unique OTP
    public ApiResponse sendOTP(@RequestHeader Map<String, String> headers, @RequestBody OTPRequest otpRequest) {

        boolean verified = Commons.verifyAuthentication(headers, exampleProperties);

        if (!verified) {
            log.info("Unauthorized request!");
            return new ApiResponse(new ApiResponse.Status(0, "401", exampleProperties.getFailureCodes().get(401)), new ArrayList<>());
        } else {
            return otpService.sendOtp(otpRequest);
        }

    }

    @PostMapping("/verifyOTP")   // api to compare and verify the user entered OTP with the set one
    public ApiResponse verifyOTP(@RequestHeader Map<String, String> headers, @RequestBody VerifyOTPRequest verifyOTPRequest) {

        boolean verified = Commons.verifyAuthentication(headers, exampleProperties);

        if (!verified) {
            log.info("Unauthorized request!");
            return new ApiResponse(new ApiResponse.Status(0, "401", exampleProperties.getFailureCodes().get(401)), new ArrayList<>());
        } else {
            return otpService.verifyOTP(verifyOTPRequest);
        }

    }

    @PostMapping("/sendAppointment") // to send booth visit appointment notification
    public ApiResponse sendAppointment(@RequestHeader Map<String, String> headers, @RequestBody AppointmentRequest appointmentRequest) {

        boolean verified = Commons.verifyAuthentication(headers, exampleProperties);

        if (!verified) {
            log.info("Unauthorized request!");
            return new ApiResponse(new ApiResponse.Status(0, "401", exampleProperties.getFailureCodes().get(401)), new ArrayList<>());
        } else {
            return appointmentService.sendAppointment(appointmentRequest);
        }

    }

    @PostMapping("/notifyHoh/{language}") // api to notify the hoh to keep douments ready before survey in selected language
    public ApiResponse notifyHoh(@RequestHeader Map<String, String> headers,@PathVariable(required = true) String language, @RequestParam("file") MultipartFile file) {

        boolean verified = Commons.verifyAuthentication(headers, exampleProperties);

        if (!verified) {
            log.info("Unauthorized request!");
            return new ApiResponse(new ApiResponse.Status(0, "401", exampleProperties.getFailureCodes().get(401)), new ArrayList<>());
        } else {
            return notifyHohService.initNotification(file,language);
        }

    }

}
