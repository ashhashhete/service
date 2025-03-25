package com.adani.sra.connector.smsservice.controllers;

import com.adani.sra.connector.smsservice.configurations.ExampleProperties;
import com.adani.sra.connector.smsservice.models.requests.appointmentModels.AppointmentRequest;
import com.adani.sra.connector.smsservice.models.requests.otpModels.OTPRequest;
import com.adani.sra.connector.smsservice.models.requests.otpModels.VerifyOTPRequest;
import com.adani.sra.connector.smsservice.models.responses.ApiResponse;
import com.adani.sra.connector.smsservice.service.AppointmentService;
import com.adani.sra.connector.smsservice.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class SMSControllerTest {

    @Mock
    private OTPService otpService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private ExampleProperties exampleProperties;

    @InjectMocks
    private SMSController yourController;

    private String expectedAccessToken;
    private Map<Integer, String> escalatedCodes = new HashMap();

    private Map<Integer, String> successCodes = new HashMap();

    private Map<Integer, String> failureCodes = new HashMap();

    private Map<String, String> header = new HashMap<>();
    private OTPRequest otpRequest;
    private VerifyOTPRequest verifyOTPRequest;

    private AppointmentRequest appointmentRequest;

    @BeforeEach
    public void setUp() {

        escalatedCodes.put(114, "socket timeout, could not connect Database, please try again after some time.");
        escalatedCodes.put(115, "technical error! could not complete operation.");
        escalatedCodes.put(118, "An error occurred while executing %s");
        escalatedCodes.put(119, "Received null/empty response from service");


        successCodes.put(111, "request sent successfully.");
        successCodes.put(121, "data received successfully.");

        failureCodes.put(11, "invalid request check the request parmeters.");
        failureCodes.put(113, "msisdn not active - check the receiver number");
        failureCodes.put(117, "connection timeout, could not reach Database, please try again after some time.");
        failureCodes.put(120, "unknown HTTP status received");
        failureCodes.put(401,"Unauthorised Request.");

        expectedAccessToken = "a4ae15a3e2a00fd725236484d6195482cfe221e94b7d8c305a3b7052edc4b6ff8955c91a026d2d543e4b00fcc605a2506513ce1bf2cefc7f6c15dd93f5ec7310";

        String headers = "a4ae15a3e2a00fd725236484d6195482cfe221e94b7d8c305a3b7052edc4b6ff8955c91a026d2d543e4b00fcc605a2506513ce1bf2cefc7f6c15dd93f5ec7310";
        header.put("access-token" ,headers);

        otpRequest = new OTPRequest();

        otpRequest.setReceiver("9123321123133"); // hoh mobile number
        otpRequest.setHohName("Vinod Kabmbli");  //hoh Name
        otpRequest.setSurveyId("DRP/Z0/54625");  // uni_unique Id
        otpRequest.setRelationShipManagerNo("");   //  surveyor/ helpline number -redundunt - keep empty string

        verifyOTPRequest = new VerifyOTPRequest();

        verifyOTPRequest.setHohName("Vinod kambli");
        verifyOTPRequest.setSurveyId("drp/z0/54625");
        verifyOTPRequest.setRelationShipManagerNo("");
        verifyOTPRequest.setSender("7359774149");
        verifyOTPRequest.setTransactionId("086877e1-f60c-cd7e-856b-53e8b68f5ef9");
        verifyOTPRequest.setOtp("074783");

        appointmentRequest = new AppointmentRequest();

        appointmentRequest.setReceiver("37594328758275");
        appointmentRequest.setHohName("Vinod Kabmbli");
        appointmentRequest.setSurveyId("drp/z0/54625");
        appointmentRequest.setBoothId("01");
        appointmentRequest.setBoothAddress("mumbai");
        appointmentRequest.setDate("10-04-2024");
        appointmentRequest.setTimeSlot("02:30PM");

    }


    @Test
    public void testSendOTP_WithInvalidAuthentication() {
        ApiResponse actualResponse = yourController.sendOTP(header, otpRequest);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getStatus().getStatus());
        assertEquals("401", actualResponse.getStatus().getCode());
    }

    @Test
    public void testSendOTP_WithValidAuthentication() {

        when(exampleProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse expectedResponse = new ApiResponse(new ApiResponse.Status(121, "OK", ""), Collections.emptyList());

        when(otpService.sendOtp(otpRequest)).thenReturn(expectedResponse);
        ApiResponse actualResponse = yourController.sendOTP(header, otpRequest);
        verify(otpService).sendOtp(otpRequest);
        assertEquals(expectedResponse, actualResponse);
    }


    @Test
    public void testVerifyOTP_WithInvalidAuthentication() {
        ApiResponse actualResponse = yourController.verifyOTP(header,verifyOTPRequest);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getStatus().getStatus());
        assertEquals("401", actualResponse.getStatus().getCode());
    }


    @Test
    public void testVerifyOTP_WithValidAuthentication() {

        when(exampleProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse expectedResponse = new ApiResponse(new ApiResponse.Status(121, "OK", ""), Collections.emptyList());

        when(otpService.verifyOTP(verifyOTPRequest)).thenReturn(expectedResponse);
        ApiResponse actualResponse = yourController.verifyOTP(header,verifyOTPRequest);
        verify(otpService).verifyOTP(verifyOTPRequest);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testSendAppointment_WithInvalidAuthentication() {

        ApiResponse actualResponse = yourController.sendAppointment(header,appointmentRequest);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getStatus().getStatus());
        assertEquals("401", actualResponse.getStatus().getCode());
    }

    @Test
    public void testSendAppointment_WithValidAuthentication() {

        when(exampleProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse expectedResponse = new ApiResponse(new ApiResponse.Status(121, "OK", ""), Collections.emptyList());

        when(appointmentService.sendAppointment(appointmentRequest)).thenReturn(expectedResponse);
        ApiResponse actualResponse = yourController.sendAppointment(header,appointmentRequest);
        verify(appointmentService).sendAppointment(appointmentRequest);
        assertEquals(expectedResponse, actualResponse);
    }

}
