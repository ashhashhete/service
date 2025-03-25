//package com.adani.drp.portal.services;
//
//import com.adani.drp.portal.configurations.PortalProperties;
//import com.adani.drp.portal.models.responses.core.UnitsPagedResponse;
//import com.adani.drp.portal.repository.core.UnitListRepo;
//import com.adani.drp.portal.repository.core.UnitRepo;
//import com.adani.drp.portal.repository.history.UnitStatusHistoryRepo;
//import com.adani.drp.portal.repository.history.UnitVisitHistoryRepo;
//import com.adani.drp.portal.testUtils.TestCommons;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.persistence.EntityManager;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//@RequiredArgsConstructor
//class UnitServiceImplTest {
//
//    @Mock
//    private EntityManager entityManager;
//
//    @Mock
//    private UnitListRepo unitListRepo;
//
//    @Mock
//    UnitRepo unitRepo;
//
//    @Mock
//    private UnitStatusHistoryRepo unitStatusHistoryRepo;
//
//    @Mock
//    private UnitVisitHistoryRepo unitVisitHistoryRepo;
//
//    @Mock
//    private ServiceExcel serviceExcel;
//
//    @Mock
//    private PortalProperties portalProperties;
//
//
//    @InjectMocks
//    UnitServiceImpl unitServiceImpl;
//
//
//    int page;
//    int size;
//    String unitId;
//    String unitUniqueId;
//    String structureYear;
//    String mobileNo;
//    String surveyDate;
//    String surveyEndDate;
//    String genesysQcStatus;
//    String clientQcStatus;
//    List<String> wardNo = new ArrayList<>();
//    List<String> zoneNo = new ArrayList<>();
//    List<String> sectorNo = new ArrayList<>();
//    List<String> surveyorName;
//    List<String> remarks;
//    List<String> genesysQcBy;
//    List<String> clientQcBy;
//    List<String> unitUsage;
//    String sortField;
//    String sortDirection;
//    Sort.Direction direction;
//
//    private Map<Integer, String> escalatedCodes = new HashMap();
//
//    private Map<Integer, String> successCodes = new HashMap();
//
//    private Map<Integer, String> failureCodes = new HashMap();
//
//    private Pageable pageable;
//
//
//    private ObjectMapper objectMapper;
//
//    private List<String> otpRemarks;
//
//    private List<String> thumbRemarks;
//
//    private List<String> panchnamaRemarks;
//
//    private List<String> annexureRemarks;
//
//    private List<Integer> drpplTeam1Remarks;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//
//
//        objectMapper = new ObjectMapper();
//        page = 1;
//        size = 15;
//        pageable = PageRequest.of(page, size);
//        unitId = null;
//        unitUniqueId = null;
//        wardNo = null;
//        zoneNo = null;
//        sectorNo = null;
//        structureYear = null;
//        mobileNo = null;
//        surveyDate = null;
//        surveyEndDate = null;
//        genesysQcStatus = null;
//        clientQcStatus = null;
//        surveyorName = null;
//        remarks = null;
//        genesysQcBy = null;
//        unitUsage = null;
//        sortField = null;
//        sortDirection = "ASC";
//        direction = Sort.Direction.fromString(sortDirection);
//
//        otpRemarks = null;
//        thumbRemarks = null;
//        panchnamaRemarks = null;
//        annexureRemarks = null;
//        drpplTeam1Remarks = null;
//
//        escalatedCodes.put(114, "socket timeout, could not connect Database, please try again after some time.");
//        escalatedCodes.put(115, "technical error! could not complete operation.");
//        escalatedCodes.put(118, "An error occurred while executing %s");
//        escalatedCodes.put(119, "Received null/empty response from service");
//
//        successCodes.put(111, "request sent successfully.");
//        successCodes.put(121, "data received successfully.");
//
//        failureCodes.put(11, "invalid request check the request parmeters.");
//        failureCodes.put(113, "msisdn not active - check the receiver number");
//        failureCodes.put(117, "connection timeout, could not reach Database, please try again after some time.");
//        failureCodes.put(120, "unknown HTTP status received");
//    }
//
//    @Test
//    @DisplayName("getUnit - success")
//    void testGetUnitsSuccess() throws Exception {
//
//        UnitsPagedResponse expectedResponse =
//                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");
//
//        List<Map<String, Object>> expectedList = TestCommons.createSampleGetUnitPagedResponse("getUnitsListQryResp.json");
//
//        log.info("expectedList {}",expectedList);
//
//        Page<Map<String, Object>> pageMock = mock(Page.class);
//
//        when(pageMock.getContent()).thenReturn(expectedList);
//        when(pageMock.getNumber()).thenReturn(1);
//        when(pageMock.getSize()).thenReturn(expectedList.size());
//        when(pageMock.getTotalElements()).thenReturn((long) 209);
//        when(pageMock.getTotalPages()).thenReturn(15);
//        when(pageMock.isFirst()).thenReturn(true);
//        when(pageMock.isLast()).thenReturn(false);
//
//        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//
//        given(unitListRepo.findAllWithHOHNames(pageable, page, size)).willReturn(pageMock);
//
//        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//                size, unitId, unitUniqueId, structureYear,
//                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//                wardNo, zoneNo, sectorNo, surveyorName,
//                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks, drpplTeam1Remarks);
//
//        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//    }
//}
//////    @Test
//////    @DisplayName("getHoHByUnitGlobalId - success")
//////    void testGetUnitByUnitGlobalIdSuccess() throws Exception {
//////        String globalId = "{90A21C51-EC06-4F6C-960E-A8B7DB9A73FF}";
//////
//////        List<UnitResponse> expectedResponse =
//////                TestCommons.createSampleGetUnitList("getUnitByGlobalId.json");
//////
//////        // Mocking expected response
//////        List<UnitInfo> repoResponse = TestCommons.createGetQCStatusByUnitGlobalIdServiceResponse("getUnitByGlobalId.json");
//////        UnitInfo unitInfo = repoResponse.get(0);
//////        List<UnitInfo> expected = Collections.singletonList(unitInfo);
//////
//////        // Mocking repository calls
//////        given(unitRepo.findByGlobalId(globalId)).willReturn(expected);
//////
//////        // Calling the actual method
//////        List<UnitResponse> actualResponse = this.unitServiceImpl.getUnitByUnitGlobalId(globalId);
//////
//////        // Assertions
//////        assertEquals(expectedResponse, actualResponse);
//////    }
////
////
//////    @Test
//////    void testGetUnitByUnitGlobalIdException() throws Exception {
//////        String globalId = "{90A21C51-EC06-4F6C-960E-A8B7DB9A73FF}";
//////
//////        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//////
//////        given(unitRepo.findByGlobalId(globalId)).willThrow(new NoSuchElementException("") );
//////
//////        ApiResponse expectedExceptionResponse = new ApiResponse(
//////                new ApiResponse.Status(0, "118", "General exception"), null);
//////
//////        when(exceptionHandler.handleException(new Exception(), globalId, eq(portalProperties))).thenReturn(expectedExceptionResponse);
//////
//////        List<UnitResponse> actualResponse = this.unitServiceImpl.getUnitByUnitGlobalId(globalId);
//////
//////        assertEquals(Collections.emptyList(), actualResponse);
//////    }
////
//////    @Test
//////    @DisplayName("getGenQCHistoryByUnitGlobalId - success")
//////    void testGetGenQCHistoryByUnitGlobalIdSuccess() throws Exception {
//////        String globalId = "{90A21C51-EC06-4F6C-960E-A8B7DB9A73FF}";
//////
//////        List<UnitStatusHistoryResponse> expectedResponse =
//////                TestCommons.createGetQCStatusByUnitGlobalIdResponse("getQCStatusByUnitGlobalId.json");
//////
//////        List<UnitStatusHistory> repoResponse = TestCommons.createUnitQCHistoryRepoResponse(expectedResponse);
//////
//////        given(unitStatusHistoryRepo.getUnitStatusHistoryByGlobalId(globalId)).willReturn(repoResponse);
//////
//////        List<UnitStatusHistoryResponse> actualResponse = this.unitServiceImpl.getGenQCHistoryByUnitGlobalId(globalId);
//////        assertEquals(expectedResponse.get(0), actualResponse.get(0));
//////    }
////
//////    @Test
//////    @DisplayName("getSurveyHistoryByUnitGlobalId - success")
//////    void testGetSurveyHistoryByUnitGlobalIdSuccess() throws Exception {
//////        String globalId = "{90A21C51-EC06-4F6C-960E-A8B7DB9A73FF}";
//////
//////        List<UnitVisitHistoryResponse> expectedResponse =
//////                TestCommons.createVisitHistoryByUnitGlobalIdResponse("getSurveyHistoryByUnitGlobalId.json");
//////
//////        List<UnitVisitHistory> repoResponse = TestCommons.createGetQCStatusByUnitGlobalIdRepoResponse(expectedResponse);
//////        given(unitVisitHistoryRepo.getUnitVisitHistoryByGlobalId(globalId)).willReturn(repoResponse);
//////        List<UnitVisitHistoryResponse> actualResponse = this.unitServiceImpl.getSurveyHistoryByUnitGlobalId(globalId);
//////        assertEquals(expectedResponse.get(0), actualResponse.get(0));
//////    }
////
//////    @Test
//////    @DisplayName("getUnit- ward filtered - success")
//////    void testGetUnitsByWardSuccess() throws Exception {
//////
//////        wardNo = new ArrayList<>();
//////        wardNo.add("GN");
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////
//////    @Test
//////    @DisplayName("getUnit- zone filtered - success")
//////    void testGetUnitsByZOneSuccess() throws Exception {
//////
//////        zoneNo = new ArrayList<>();
//////        zoneNo.add("Z05");
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query q = mock(Query.class);
//////        when(q.setParameter(anyString(), any())).thenReturn(q);
//////        when(q.getResultList()).thenReturn(expectedList);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////    @Test
//////    @DisplayName("getUnit- sector filtered - success")
//////    void testGetUnitsBySectorSuccess() throws Exception {
//////
//////        sectorNo = new ArrayList<>();
//////        sectorNo.add("S1");
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////    @Test
//////    @DisplayName("getUnit- surveyorName filtered - success")
//////    void testGetUnitsBySurveyorNameSuccess() throws Exception {
//////
//////        surveyorName = new ArrayList<>();
//////        surveyorName.add("suser1");
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////    @Test
//////    @DisplayName("getUnit- start date filtered - success")
//////    void testGetUnitsByStartDateNameSuccess() throws Exception {
//////
//////        surveyDate = "2023-10-19 22:30:19";
//////
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////    @Test
//////    @DisplayName("getUnit- end date filtered - success")
//////    void testGetUnitsByEndDateNameSuccess() throws Exception {
//////
//////        surveyEndDate = "2024-01-19 22:30:19";
//////
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query q = mock(Query.class);
//////        when(q.setParameter(anyString(), any())).thenReturn(q);
//////        when(q.getResultList()).thenReturn(expectedList);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
////////    @Test
////////    @DisplayName("getUnit- structureSince filtered - success")
////////    void testGetUnitsByStructureSinceNameSuccess() throws Exception {
////////
////////        structureSince = "1995-01-19 22:30:19";
////////
////////        UnitsPagedResponse expectedResponse =
////////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
////////
////////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
////////
////////        Page<UnitListInfo> pageMock = mock(Page.class);
////////        when(pageMock.getContent()).thenReturn(expectedList);
////////        when(pageMock.getNumber()).thenReturn(1);
////////        when(pageMock.getSize()).thenReturn(expectedList.size());
////////        when(pageMock.getTotalElements()).thenReturn((long) 209);
////////        when(pageMock.getTotalPages()).thenReturn(15);
////////        when(pageMock.isFirst()).thenReturn(true);
////////        when(pageMock.isLast()).thenReturn(false);
////////
////////
////////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
////////
////////
////////        Query q = mock(Query.class);
////////        when(q.setParameter(anyString(), any())).thenReturn(q);
////////        when(q.getResultList()).thenReturn(expectedList);
////////
////////        Query queryMock = mock(Query.class);
////////        when(queryMock.getResultList()).thenReturn(expectedList);
////////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
////////
////////        Query countQueryMock = mock(Query.class);
////////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
////////
////////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
////////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
////////
////////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
////////                size,
////////                unitId,
////////                unitUniqueId,
////////                structureYear,
////////                mobileNo,
////////                surveyDate,
////////                surveyEndDate,
////////                genesysQcStatus,
////////                clientQcStatus,
////////                structureSince,
////////                wardNo,
////////                zoneNo,
////////                sectorNo,
////////                surveyorName,
////////                remarks,
////////                sortField,
////////                direction);
////////
////////
////////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
////////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
////////
////////    }
//////
//////    @Test
//////    @DisplayName("getUnit- structureYear filtered - success")
//////    void testGetUnitsByStructureYearNameSuccess() throws Exception {
//////
//////        structureYear = "After 01.01.2000 but before 01.01.2011";
//////
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsListFilltered.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsListFilltered.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query q = mock(Query.class);
//////        when(q.setParameter(anyString(), any())).thenReturn(q);
//////        when(q.getResultList()).thenReturn(expectedList);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////    @Test
//////    @DisplayName("getUnit- genesysQcStatus filtered - success")
//////    void testGetUnitsByGenQcStatusNameSuccess() throws Exception {
//////
//////        genesysQcStatus = "In progress";
//////
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsList.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////    @Test
//////    @DisplayName("getUnit- mobileNo filtered and sorted- success")
//////    void testGetUnitsByMobileNoSortedSuccess() throws Exception {
//////
//////        mobileNo = "9878987987";
//////        sortField = "unitUniqueId";
//////        sortDirection = "DESC";
//////
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsList.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////    @Test
//////    @DisplayName("getUnit- unitUniqueId filtered and sorted- success")
//////    void testGetUnitUniqueIDByMobileNoSortedSuccess() throws Exception {
//////
//////        unitUniqueId = "DRP/S4/GN/Z17/00678";
//////        sortField = "surveyorName";
//////        sortDirection = "DESC";
//////
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsList.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query q = mock(Query.class);
//////        when(q.setParameter(anyString(), any())).thenReturn(q);
//////        when(q.getResultList()).thenReturn(expectedList);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////
//////
//////    @Test
//////    @DisplayName("getUnit- unitId filtered and sorted- success")
//////    void testGetUnitIDByMobileNoSortedSuccess() throws Exception {
//////
//////        unitId = "U_1705995050715";
//////        sortField = "visitCount";
//////        sortDirection = "DESC";
//////
//////        UnitsPagedResponse expectedResponse =
//////                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");
//////
//////        List<UnitListInfo> expectedList  = TestCommons.createSampleGetUnitPagedResponse("getUnitsList.json");
//////
//////        Page<UnitListInfo> pageMock = mock(Page.class);
//////        when(pageMock.getContent()).thenReturn(expectedList);
//////        when(pageMock.getNumber()).thenReturn(1);
//////        when(pageMock.getSize()).thenReturn(expectedList.size());
//////        when(pageMock.getTotalElements()).thenReturn((long) 209);
//////        when(pageMock.getTotalPages()).thenReturn(15);
//////        when(pageMock.isFirst()).thenReturn(true);
//////        when(pageMock.isLast()).thenReturn(false);
//////
//////
//////        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//////
//////
//////        Query q = mock(Query.class);
//////        when(q.setParameter(anyString(), any())).thenReturn(q);
//////        when(q.getResultList()).thenReturn(expectedList);
//////
//////        Query queryMock = mock(Query.class);
//////        when(queryMock.getResultList()).thenReturn(expectedList);
//////        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//////
//////        Query countQueryMock = mock(Query.class);
//////        when(countQueryMock.getSingleResult()).thenReturn((long) expectedList.size());
//////
//////        when(entityManager.createNativeQuery(anyString(), eq(UnitListInfo.class))).thenReturn(queryMock);
//////        when(entityManager.createNativeQuery(anyString())).thenReturn(countQueryMock);
//////
//////        UnitsPagedResponse actualResponse = this.unitServiceImpl.getUnits(page,
//////                size, unitId, unitUniqueId, structureYear,
//////                mobileNo, surveyDate, surveyEndDate, genesysQcStatus, clientQcStatus,
//////                wardNo, zoneNo, sectorNo, surveyorName,
//////                remarks, sortField, direction, genesysQcBy, clientQcBy, unitUsage,
//////                otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);
//////
//////
//////        assertEquals(expectedResponse.getUnits().get(0), actualResponse.getUnits().get(0));
//////        verify(entityManager).createNativeQuery(any(String.class), eq(UnitListInfo.class));
//////
//////    }
//////}
