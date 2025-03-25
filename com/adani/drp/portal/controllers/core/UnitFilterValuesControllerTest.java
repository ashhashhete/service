package com.adani.drp.portal.controllers.core;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.services.UnitFilterValueServiceImpl;
import com.adani.drp.portal.testUtils.TestCommons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UnitFilterValuesControllerTest {

    @Mock
    UnitFilterValueServiceImpl unitFilterValueServiceImpl;

    @InjectMocks
    private UnitFilterValuesController unitFilterValuesController;

    @Mock
    private PortalProperties portalProperties;


    private Map<Integer, String> escalatedCodes = new HashMap();

    private Map<Integer, String> successCodes = new HashMap();

    private Map<Integer, String> failureCodes = new HashMap();

    private Map<String, String> header = new HashMap<>();

    private String expectedAccessToken ;




    @BeforeEach
    void setUp() {

        expectedAccessToken = "a4ae15a3e2a00fd725236484d6195482cfe221e94b7d8c305a3b7052edc4b6ff8955c91a026d2d543e4b00fcc605a2506513ce1bf2cefc7f6c15dd93f5ec7310";

        String headers = "a4ae15a3e2a00fd725236484d6195482cfe221e94b7d8c305a3b7052edc4b6ff8955c91a026d2d543e4b00fcc605a2506513ce1bf2cefc7f6c15dd93f5ec7310";
        header.put("access-token" ,headers);

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


    }

    @Test
    @DisplayName("getWardNos_Success")
    void testGetWardNos_Success() {

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("wardNosList.json");

        when(unitFilterValueServiceImpl.getWardNos()).thenReturn(expectedResponse);

       ApiResponse actualResponse = unitFilterValuesController.getWardNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getWardNos_Empty")
    void testGetWardNos_Empty() {

        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse = new ArrayList();
        when(unitFilterValueServiceImpl.getWardNos()).thenReturn(new ArrayList());
        ApiResponse actualResponse = unitFilterValuesController.getWardNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getWardNos_null")
    void testGetWardNos_null() {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse = new ArrayList();
        when(unitFilterValueServiceImpl.getWardNos()).thenReturn(null);
        ApiResponse actualResponse = unitFilterValuesController.getWardNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }


    @Test
    @DisplayName("getZoneNos_Success")
    void getZoneNos_Success() {

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getZoneNoList.json");
        when(unitFilterValueServiceImpl.getZoneNos()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getZoneNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getZoneNos_Empty")
    void testGetZoneNos_Empty() {

        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        List<String> expectedResponse = new ArrayList();
        when(unitFilterValueServiceImpl.getZoneNos()).thenReturn(new ArrayList());
        ApiResponse actualResponse = unitFilterValuesController.getZoneNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getZoneNos_null")
    void testGetZoneNos_null() {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse = new ArrayList();
        when(unitFilterValueServiceImpl.getZoneNos()).thenReturn(null);
        ApiResponse actualResponse = unitFilterValuesController.getZoneNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getSectorNos_Success")
    void getSectorNos_Success() {

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("sectorNoList.json");
        when(unitFilterValueServiceImpl.getSectorNos()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getSectorNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getSectorNos_Empty")
    void testGetSectorNos_Empty() {

        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse = new ArrayList();
        when(unitFilterValueServiceImpl.getSectorNos()).thenReturn(new ArrayList());
        ApiResponse actualResponse = unitFilterValuesController.getSectorNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getSectorNos_null")
    void testGetSectorNos_null() {

        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse = new ArrayList();
        when(unitFilterValueServiceImpl.getSectorNos()).thenReturn(null);
        ApiResponse actualResponse = unitFilterValuesController.getSectorNos(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getSurveyor_names_Success")
    void testGetSurveyor_names_Success() {

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("surveyorNames.json");
        when(unitFilterValueServiceImpl.getSurveyorNames()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getSurveyorNames(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getSurveyor_names_null")
    void testGetSurveyor_names_null() {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        when(unitFilterValueServiceImpl.getSurveyorNames()).thenReturn(null);
        ApiResponse actualResponse = unitFilterValuesController.getSurveyorNames(header);
        assertEquals(new ArrayList(), actualResponse.getData());
    }

    @Test
    @DisplayName("getSurveyor_names_empty")
    void testGetSurveyor_names_empty() {

        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        when(unitFilterValueServiceImpl.getSurveyorNames()).thenReturn(new ArrayList());
        ApiResponse actualResponse = unitFilterValuesController.getSurveyorNames(header);
        assertEquals(new ArrayList(), actualResponse.getData());
    }



    @Test
    @DisplayName("getStructure_years_success")
    void testGetStructure_years_success() {

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getStructureYears.json");
        when(unitFilterValueServiceImpl.getStructureYears()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getStructureYears(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @ParameterizedTest
    @DisplayName("Test getStructureYears with null response")
    @MethodSource("provideResponses")
    void testGetStructureYears_combined(List<String> expectedResponse) {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse actualResponse = unitFilterValuesController.getStructureYears(header);
        assertEquals(expectedResponse == null ? new ArrayList<>() : expectedResponse, actualResponse.getData());
    }

    private static Stream<List<String>> provideResponses() {
        return Stream.of(
                null,
                new ArrayList<>()
        );
    }


    @Test
    @DisplayName("getGen_qc_remarks_success")
    void testGetGen_qc_remarks_success() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getGenQCRemarks.json");
        when(unitFilterValueServiceImpl.getGenQcRemarks()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getGenQcRemarks(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getAdani_qc_remarks_success")
    void testGetAdani_qc_remarks_success() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getAdaniQCRemarks.json");
        when(unitFilterValueServiceImpl.getGenQcRemarks()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getGenQcRemarks(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @ParameterizedTest
    @DisplayName("Test getGen_qc_remarks with null response")
    @MethodSource("provideResponses")
    void testGetGen_qc_remarks__combined(List<String> expectedResponse) {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse actualResponse = unitFilterValuesController.getGenQcRemarks(header);
        assertEquals(expectedResponse == null ? new ArrayList<>() : expectedResponse, actualResponse.getData());
    }


    @Test
    @DisplayName("getAnnexure_remarks_success")
    void testGetAnnexure_remarks_success() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getAnnexureRemarks.json");
        when(unitFilterValueServiceImpl.getAnnexureRemarks()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getAnnexureRemarks(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @ParameterizedTest
    @DisplayName("Test getAnnexure_remarks with null response")
    @MethodSource("provideResponses")
    void testGetAnnexure_remarks_combined(List<String> expectedResponse) {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse actualResponse = unitFilterValuesController.getAnnexureRemarks(header);
        assertEquals(expectedResponse == null ? new ArrayList<>() : expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getOTP_remarks_success")
    void testGetOTP_remarks_success() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getOTPRemarks.json");
        when(unitFilterValueServiceImpl.getOTPRemarks()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getOTPRemarks(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @ParameterizedTest
    @DisplayName("Test getOTP_remarks with null response")
    @MethodSource("provideResponses")
    void testGetOTP_remarks_combined(List<String> expectedResponse) {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse actualResponse = unitFilterValuesController.getOTPRemarks(header);
        assertEquals(expectedResponse == null ? new ArrayList<>() : expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getPanchnama_remarks_success")
    void testGetPanchnama_remarks_success() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getPanchnamaRemarks.json");
        when(unitFilterValueServiceImpl.getPanchnamaRemarks()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getPanchnamaRemarks(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @ParameterizedTest
    @DisplayName("Test getPanchnama_remarks with null response")
    @MethodSource("provideResponses")
    void testGetPanchnama_remarks_combined(List<String> expectedResponse) {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse actualResponse = unitFilterValuesController.getPanchnamaRemarks(header);
        assertEquals(expectedResponse == null ? new ArrayList<>() : expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getThumb_remarks_success")
    void testGetThumb_remarks_success() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<String> expectedResponse =
                TestCommons.createDropDownListResponse("getThumbRemarks.json");
        when(unitFilterValueServiceImpl.getThumbImpressionRemarks()).thenReturn(expectedResponse);
        ApiResponse actualResponse = unitFilterValuesController.getThumbRemarks(header);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @ParameterizedTest
    @DisplayName("Test getThumb_remarks with null response")
    @MethodSource("provideResponses")
    void testGetThumb_remarks_combined(List<String> expectedResponse) {
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        ApiResponse actualResponse = unitFilterValuesController.getThumbRemarks(header);
        assertEquals(expectedResponse == null ? new ArrayList<>() : expectedResponse, actualResponse.getData());
    }

}