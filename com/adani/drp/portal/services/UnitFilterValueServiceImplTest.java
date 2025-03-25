package com.adani.drp.portal.services;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.exception.ExceptionHandler;
import com.adani.drp.portal.repository.core.UnitFilterValueRepo;
import com.adani.drp.portal.repository.survey.SurveyorNameRepo;
import com.adani.drp.portal.testUtils.TestCommons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
class UnitFilterValueServiceImplTest {

    @Autowired
    TestEntityManager entityManager;
    @Mock
    DomainItemsServiceImpl domainItemsServiceImpl;

    @Mock
    SurveyorNameRepo surveyorNameRepo;

    @Mock
    UnitFilterValueRepo unitFilterValueRepo;

    @Mock
    private PortalProperties portalProperties;

    @Mock
    ExceptionHandler exceptionHandler;

    @InjectMocks
    UnitFilterValueServiceImpl unitFilterValueServiceImpl;

    private Map<Integer, String> escalatedCodes = new HashMap();

    private Map<Integer, String> successCodes = new HashMap();

    private Map<Integer, String> failureCodes = new HashMap();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

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

    @ParameterizedTest
    @MethodSource("provideWardNoResponses")
    @DisplayName("testGetWardNos")
    void testGetWardNos(List<String> expectedResponse) {
        when(unitFilterValueRepo.findDistinctWardNos()).thenReturn(expectedResponse);
        List<String> actualResponse = unitFilterValueServiceImpl.getWardNos();
        assertEquals(expectedResponse, actualResponse);
    }

    private static Stream<List<String>> provideWardNoResponses() {
        return Stream.of(
                TestCommons.createDropDownListResponse("wardNosList.json"),
                null,
                Collections.emptyList(),
                new ArrayList<>()
        );
    }

//    @Test
//    @DisplayName("testGetWardNos_null")
//    void testGetWardNos_null() throws Exception{
//
//        when(unitFilterValueRepo.findDistinctWardNos()).thenThrow(NoSuchElementException.class);
//
//        Exception exception = assertThrows(NullPointerException.class, () -> unitFilterValueServiceImpl.getWardNos());
//
//        when(exceptionHandler.handleException(exception,  portalProperties)).thenCallRealMethod();
//
//        assertEquals(null,exception.getMessage());
//
//    }

    @ParameterizedTest
    @MethodSource("provideZoneNoResponses")
    @DisplayName("testGetZoneNos")
    void testGetZoneNos(List<String> expectedResponse) {
        when(unitFilterValueRepo.findDistinctZoneNos()).thenReturn(expectedResponse);
        List<String> actualResponse = unitFilterValueServiceImpl.getZoneNos();
        assertEquals(expectedResponse, actualResponse);
    }

    private static Stream<List<String>> provideZoneNoResponses() {
        return Stream.of(
                TestCommons.createDropDownListResponse("getZoneNoList.json"),
                null,
                Collections.emptyList(),
                new ArrayList<>()
        );
    }

//    @Test
//    @DisplayName("testGetZoneNos_null")
//    void testGetZoneNos_null() throws Exception{
//
//        when(unitFilterValueRepo.findDistinctZoneNos()).thenThrow(NoSuchElementException.class);
//        Exception exception = assertThrows(NullPointerException.class, () -> unitFilterValueServiceImpl.getZoneNos());
//        when(exceptionHandler.handleException(exception,  portalProperties)).thenCallRealMethod();
//        assertEquals(null,exception.getMessage());
//
//    }

    @ParameterizedTest
    @MethodSource("provideSectorNoResponses")
    @DisplayName("testGetSectorNos")
    void testGetSectorNos(List<String> expectedResponse) {
        when(unitFilterValueRepo.findDistinctSectorNos()).thenReturn(expectedResponse);
        List<String> actualResponse = unitFilterValueServiceImpl.getSectorNos();
        assertEquals(expectedResponse, actualResponse);
    }

    private static Stream<List<String>> provideSectorNoResponses() {
        return Stream.of(
                TestCommons.createDropDownListResponse("sectorNoList.json"),
                null,
                Collections.emptyList(),
                new ArrayList<>()
        );
    }

    @ParameterizedTest
    @MethodSource("provideSurveyorNamesResponses")
    @DisplayName("testGetSurveyorNames")
    void testGetSurveyorNames(List<String> expectedResponse) {

        when(unitFilterValueRepo.findDistinctSurveyorNames()).thenReturn(expectedResponse);
        List<String> actualResponse = unitFilterValueServiceImpl.getSurveyorNames();
        assertEquals(expectedResponse, actualResponse);
    }

    private static Stream<List<String>> provideSurveyorNamesResponses() {
        return Stream.of(
                TestCommons.createDropDownListResponse("surveyorNames.json"),
                null,
                Collections.emptyList(),
                new ArrayList<>()
        );
    }

//    @Test
//    @DisplayName("getSurveyorNames_null")
//    void testGetSurveyorNames_null() throws Exception{
//
//        when(unitFilterValueRepo.findDistinctSurveyorNames()).thenThrow(NoSuchElementException.class);
//        Exception exception = assertThrows(NullPointerException.class, () -> unitFilterValueServiceImpl.getSurveyorNames());
//        when(exceptionHandler.handleException(exception,  portalProperties)).thenCallRealMethod();
//        assertEquals(null,exception.getMessage());
//
//    }



    @ParameterizedTest
    @MethodSource("provideGenQCRemarksResponses")
    @DisplayName("testGetGenQCRemarks")
    void testGetGenQCRemarks(List<String> expectedResponse) {

//        List<String> stub = expectedResponse != null && !expectedResponse.isEmpty() ?
//                TestCommons.createDropDownListResponse("getGenQCRemarks.json") :
//                new ArrayList<>();


        when(unitFilterValueRepo.findDistinctGenQcRemarks()).thenReturn(expectedResponse);
        List<String> actualResponse = unitFilterValueServiceImpl.getGenQcRemarks();
        assertIterableEquals(expectedResponse, actualResponse);
    }

    private static Stream<List<String>> provideGenQCRemarksResponses() {
//        List<String> objectArrayList = TestCommons.createDropDownListResponse("getGenQCRemarks.json");
//        log.info("objectArrayList: {}", objectArrayList);

        return Stream.of(TestCommons.createDropDownListResponse("getGenQCRemarks.json"),
                null,
                Collections.emptyList(),
                new ArrayList<>()
        );
    }

//    @Test
//    @DisplayName("testGetGenQCRemarks_exception")
//    void testGetGenQCRemarksException(List<Object[]> expectedResponse) {
//
////        List<String> stub = expectedResponse != null && !expectedResponse.isEmpty() ?
////                TestCommons.createDropDownListResponse("getGenQCRemarks.json") :
//        List<String> stub =         new ArrayList<>();
//
//        List<String> domainName = Collections.singletonList("unit_status");
//        when(gdbItemsRepo.getDomainName(domainName)).thenThrow(NullPointerException.class);
//        List<String> actualResponse = unitFilterValueServiceImpl.getGenQcRemarks();
//        assertIterableEquals(stub, actualResponse);
//    }

    @ParameterizedTest
    @MethodSource("provideStructureYearResponses")
    @DisplayName("testGetStructureYearResponses")
    void testGetStructureSinceResponses(List<Object[]> expectedResponse) {

        List<String> stub = expectedResponse != null && !expectedResponse.isEmpty() ?
                TestCommons.createDropDownListResponse("getStructureYears.json") :
                new ArrayList<>();

        List<String> domainName = Collections.singletonList("domain_structure_year");
        when(domainItemsServiceImpl.getDomainName(domainName)).thenReturn(expectedResponse);
        List<String> actualResponse = unitFilterValueServiceImpl.getStructureYears();
        assertIterableEquals(stub, actualResponse);
    }

    private static Stream<List<Object[]>> provideStructureYearResponses() {
        List<Object[]> objectArrayList = TestCommons.convertJsonDomainInfoCode("getStructuctureSinceDomainInfo.json");
        log.info("objectArrayList: {}", objectArrayList);

        return Stream.of(
                objectArrayList,
                null,
                Collections.emptyList()
        );
    }

    @ParameterizedTest
    @MethodSource("provideAdaniQCRemarksResponses")
    @DisplayName("testGetAdaniQCRemarks")
    void testGetAdaniQCRemarks(List<String> expectedResponse) {

//        List<String> stub = expectedResponse != null && !expectedResponse.isEmpty() ?
//                TestCommons.sampleSurveyNames("getGenQCRemarks.json") :
//                new ArrayList<>();


        when(unitFilterValueRepo.findDistinctAdaniQcRemarks()).thenReturn(expectedResponse);
        List<String> actualResponse = unitFilterValueServiceImpl.getAdaniQcRemarks();
        assertIterableEquals(expectedResponse, actualResponse);
    }

    private static Stream<List<String>> provideAdaniQCRemarksResponses() {
//        List<String> objectArrayList = TestCommons.createDropDownListResponse("getGenQCRemarks.json");
//        log.info("objectArrayList: {}", objectArrayList);

        return Stream.of(TestCommons.createDropDownListResponse("getAdaniQCRemarks.json"),
                null
        );
    }


}