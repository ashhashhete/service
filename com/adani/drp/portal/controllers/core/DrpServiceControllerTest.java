package com.adani.drp.portal.controllers.core;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.history.UnitStatusHistoryResponse;
import com.adani.drp.portal.models.history.UnitVisitHistoryResponse;
import com.adani.drp.portal.models.responses.core.*;
import com.adani.drp.portal.models.responses.core.domainValues.DomainInfoResponse;
import com.adani.drp.portal.models.responses.core.qcStatus.UnitStatusResponse;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.services.*;
import com.adani.drp.portal.testUtils.TestCommons;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class DrpServiceControllerTest {

    @Mock
    private HoHServiceImpl hohServiceImpl;

    @Mock
    private UnitServiceImpl unitServiceImpl;

    @Mock
    private MemberServiceImpl memberServiceImpl;

    @Mock
    private StructureServiceImpl structureServiceImpl;

    @Mock
    private DomainItemsServiceImpl domainItemsServiceImpl;

    @Mock
    private PortalProperties portalProperties;

    @InjectMocks
    private DrpServiceController drpServiceController;

    private ObjectMapper objectMapper;

    int page;
    int size;
    String unitId;
    String unitUniqueId;
    String structureYear;
    String mobileNo;
    String surveyDate;
    String surveyEndDate;
    String genesysQcStatus;

    String clientQcStatus;
    List<String> wardNo;
    List<String> zoneNo;
    List<String> sectorNo;
    List<String> surveyorName;
    List<String> remarks;

    List<String> genesysQcBy;

    List<String> clientQcBy;

    List<String> unitUsage;
    List<String> otpRemarks;
    List<String> thumbRemarks;
    List<String> panchnamaRemarks;
    List<String> annexureRemarks;
    List<Integer> drpplTeam1Remarks;
    String sortField;
    String sortDirection;
    Sort.Direction direction;

    private Map<Integer, String> escalatedCodes = new HashMap();

    private Map<Integer, String> successCodes = new HashMap();

    private Map<Integer, String> failureCodes = new HashMap();

    private Map<String, String> header = new HashMap<>();

    private String expectedAccessToken ;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        page = 1;
        size = 15;
        unitId = null;
        unitUniqueId = null;
        wardNo = null;
        zoneNo = null;
        sectorNo = null;
        structureYear = null;
        mobileNo = null;
        surveyDate = null;
        surveyEndDate = null;
        genesysQcStatus = null;
        clientQcStatus = null;
        surveyorName = null;
        remarks = null;
        genesysQcBy = null;
        unitUsage = null;
        otpRemarks = null;
        thumbRemarks= null;
        panchnamaRemarks=null;
        annexureRemarks= null;
        sortField = null;
        sortDirection = "ASC";
        direction = Sort.Direction.fromString(sortDirection);

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
    @DisplayName("testGetHohInfoByRelGlobalId_Success")
    void testGetHohInfoByRelGlobalId_Success() {
        String relGlobalId = "{FD8C59C0-496C-4126-B4B7-20C3A29D10E7}";

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);

        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        List<HoHResponse> expectedResponse = TestCommons.createSampleHoHResponseList("getHohInfoByUnitGlobalId.json");

        when(hohServiceImpl.getHoHByUnitGlobalId(relGlobalId)).thenReturn(expectedResponse);

        ApiResponse apiResponse = drpServiceController.getHohInfoByRelGlobalId(header,relGlobalId);
        List<HoHResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<HoHResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }


    @Test
    @DisplayName("testGetHohInfoByRelGlobalId_empty")
    void testGetHohInfoByRelGlobalId_empty() {
        String relGlobalId = "{FD8C59C0-496C-4126-B4B7-20C3A29D10E7}";
        List<HoHResponse> expectedResponse = new ArrayList<>();

//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

//        when(hohServiceImpl.getHoHByUnitGlobalId(relGlobalId)).thenReturn(expectedResponse);

        ApiResponse apiResponse = drpServiceController.getHohInfoByRelGlobalId(header,relGlobalId);
        List<HoHResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<HoHResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("testGetHohInfoByRelGlobalId_null")
    void testGetHohInfoByRelGlobalId_null() {
        String relGlobalId = "{FD8C59C0-496C-4126-B4B7-20C3A29D10E7}";
//        String headers = "a4ae15a3e2a00fd725236484d6195482cfe221e94b7d8c305a3b7052edc4b6ff8955c91a026d2d543e4b00fcc605a2506513ce1bf2cefc7f6c15dd93f5ec7310";
        List<HoHResponse> expectedResponse = null;

//        header.put("access-token" ,headers);

        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        when(hohServiceImpl.getHoHByUnitGlobalId(relGlobalId)).thenReturn(null);

        ApiResponse apiResponse = drpServiceController.getHohInfoByRelGlobalId(header,relGlobalId);
        List<HoHResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<HoHResponse>>() {
        });

        assertEquals(new ArrayList<>(), actualResponse);
    }

    @Test
    @DisplayName("testGetMembersByHohGlobalId_Success")
    void testGetMembersByHohGlobalId_Success() {
//        String headers = "a4ae15a3e2a00fd725236484d6195482cfe221e94b7d8c305a3b7052edc4b6ff8955c91a026d2d543e4b00fcc605a2506513ce1bf2cefc7f6c15dd93f5ec7310";
//        Map<String, String> header = new HashMap<>();
//        header.put("access-token" ,headers);

        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        String relGlobalId = "{B65C065A-3FD5-4462-B48D-712FE7D045F1}";
        List<MemberResponse> expectedResponse =
                TestCommons.createSampleMembersResponseList("getMembersByHohGlobalId.json");

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(memberServiceImpl.getMembersByHohId(relGlobalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getMembers(header,relGlobalId);
        List<MemberResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<MemberResponse>>() {
        });
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("testGetMembersByHohGlobalId_empty")
    void testGetMembersByHohGlobalId_empty() {
        String relGlobalId = "{B65C065A-3FD5-4462-B48D-712FE7D045F1}";
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        List<MemberResponse> expectedResponse = new ArrayList<>();
        when(memberServiceImpl.getMembersByHohId(relGlobalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getMembers(header,relGlobalId);
        List<MemberResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<MemberResponse>>() {
        });
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("testGetMembersByHohGlobalId_null")
    void testGetMembersByHohGlobalId_null() {
        String relGlobalId = "{B65C065A-3FD5-4462-B48D-712FE7D045F1}";
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        //        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(memberServiceImpl.getMembersByHohId(relGlobalId)).thenReturn(null);
        ApiResponse apiResponse = drpServiceController.getMembers(header,relGlobalId);
        List<MemberResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<MemberResponse>>() {
        });
        assertEquals(new ArrayList<>() , actualResponse);
    }


    @Test
    @DisplayName("testGetUnitByGlobalId_Success")
    void testGetUnitByGlobalId_Success() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        String relGlobalId = "{FD8C59C0-496C-4126-B4B7-20C3A29D10E7}";
        List<UnitResponse> expectedResponse =
                TestCommons.createSampleGetUnitList("getUnitByGlobalId.json");

        when(unitServiceImpl.getUnitByUnitGlobalId(relGlobalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getUnitByGlobalId(header,relGlobalId);
        List<UnitResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("testGetUnitByGlobalId_null")
    void testGetUnitByGlobalId_null() {
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        String relGlobalId = "{FD8C59C0-496C-4126-B4B7-20C3A29D10E7}";
        List<UnitResponse> expectedResponse = null;
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        when(unitServiceImpl.getUnitByUnitGlobalId(relGlobalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getUnitByGlobalId(header,relGlobalId);
        List<UnitResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitResponse>>() {
        });

        assertEquals(new ArrayList<>(), actualResponse);
    }

    @Test
    @DisplayName("testGetUnitByGlobalId_empty")
    void testGetUnitByGlobalId_empty() {
        String relGlobalId = "{FD8C59C0-496C-4126-B4B7-20C3A29D10E7}";
        List<UnitResponse> expectedResponse = new ArrayList<>();
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(unitServiceImpl.getUnitByUnitGlobalId(relGlobalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getUnitByGlobalId(header,relGlobalId);
        List<UnitResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("testGetUnits_Success")
    void testGetUnits_Success() {
        UnitsPagedResponse expectedResponse =
                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        when(unitServiceImpl.getUnits(page - 1, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,
                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                sortField,
                direction, genesysQcBy,clientQcBy, unitUsage, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks
        ))
                .thenReturn(expectedResponse);

        ApiResponse actualResponse = drpServiceController.getUnits(header,
                page, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,

                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                 clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                genesysQcBy,
                clientQcBy,
                unitUsage,
                sortField,
                sortDirection, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);

        UnitsPagedResponse unitsPagedResponse = objectMapper.convertValue(actualResponse.getData(), UnitsPagedResponse.class);

        assertEquals(expectedResponse.getUnits(), unitsPagedResponse.getUnits());
    }

    @Test
    @DisplayName("testGetUnits_Success_sorted")
    void testGetUnits_Success_sorted() {
        UnitsPagedResponse expectedResponse =
                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");

        sortField = "visitCount";
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        when(unitServiceImpl.getUnits(page - 1, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,
                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                sortField,
                direction,genesysQcBy,clientQcBy,unitUsage, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks
        ))
                .thenReturn(expectedResponse);

        ApiResponse actualResponse = drpServiceController.getUnits(header,
                page, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,
                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                genesysQcBy,
                clientQcBy,
                unitUsage,
                sortField,
                sortDirection, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks,drpplTeam1Remarks);

        UnitsPagedResponse unitsPagedResponse = objectMapper.convertValue(actualResponse.getData(), UnitsPagedResponse.class);

        assertEquals(expectedResponse.getUnits(), unitsPagedResponse.getUnits());
    }

    @Test
    @DisplayName("testGetUnits_null")
    void testGetUnits_null() {
        UnitsPagedResponse expectedResponse = new UnitsPagedResponse();
        expectedResponse.setUnits(null);
//                TestCommons.createSampleUnitsResponseList("getUnitsPages.json", "getUnitsList.json");
//        expectedResponse.setUnits(new ArrayList<>());

//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(unitServiceImpl.getUnits(page - 1, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,
                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                sortField,
                direction,genesysQcBy,clientQcBy,unitUsage, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks ,drpplTeam1Remarks
        ))
                .thenReturn(expectedResponse);

        ApiResponse actualResponse = drpServiceController.getUnits(header,
                page, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,
                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                genesysQcBy,
                clientQcBy,
                unitUsage,
                sortField,
                sortDirection, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks ,drpplTeam1Remarks);

        UnitsPagedResponse unitsPagedResponse = objectMapper.convertValue(actualResponse.getData(), UnitsPagedResponse.class);

        assertEquals(new ArrayList<>(), unitsPagedResponse.getUnits());
    }

    @Test
    @DisplayName("testGetUnits_empty")
    void testGetUnits_empty() {
        UnitsPagedResponse expectedResponse = new UnitsPagedResponse();
        expectedResponse.setUnits(new ArrayList());
        //        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(unitServiceImpl.getUnits(page - 1, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,
                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                sortField,
                direction,genesysQcBy,clientQcBy,unitUsage, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks ,drpplTeam1Remarks
        ))
                .thenReturn(expectedResponse);

        ApiResponse actualResponse = drpServiceController.getUnits(header,
                page, size, unitId,
                unitUniqueId,
                structureYear,
                mobileNo,
                surveyDate,
                surveyEndDate,
                genesysQcStatus,
                clientQcStatus,
                wardNo,
                zoneNo,
                sectorNo,
                surveyorName,
                remarks,
                genesysQcBy,
                clientQcBy,
                unitUsage,
                sortField,
                sortDirection, otpRemarks, thumbRemarks, panchnamaRemarks, annexureRemarks , drpplTeam1Remarks);

        UnitsPagedResponse unitsPagedResponse = objectMapper.convertValue(actualResponse.getData(), UnitsPagedResponse.class);

        assertEquals(new ArrayList<>(), unitsPagedResponse.getUnits());
    }



    @Test
    @DisplayName("testGetUnitsWithCountByStructureGlobalId_Success")
    void testGetUnitsWithCountByStructureGlobalId_Success() {
        String globalId = "{059C35A1-B07D-4134-817C-FDB295A3B46E}";

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<StructureInfoResponse> expectedResponse =
                TestCommons.createGetUnitsWithCountByStructureGlobalIdResponse("getUnitsWithCountByStructureGlobalId.json");

        when(structureServiceImpl.getUnitByStructureGlobalId(globalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getStructureByUnit(globalId);
        List<StructureInfoResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<StructureInfoResponse>>() {
        });
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("testGetUnitsWithCountByStructureGlobalId_Empty")
    void testGetUnitsWithCountByStructureGlobalId_Empty() {
        String globalId = "{059C35A1-B07D-4134-817C-FDB295A3B46E}";
        List<StructureInfoResponse> expectedResponse = new ArrayList<>();
        //        TestCommons.createGetUnitsWithCountByStructureGlobalIdResponse("getUnitsWithCountByStructureGlobalId.json");

        //        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(structureServiceImpl.getUnitByStructureGlobalId(globalId)).thenReturn(new ArrayList<>());
        ApiResponse apiResponse = drpServiceController.getStructureByUnit(globalId);
        List<StructureInfoResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<StructureInfoResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("testGetUnitsWithCountByStructureGlobalId_null")
    void testGetUnitsWithCountByStructureGlobalId_null() {
        String globalId = "{059C35A1-B07D-4134-817C-FDB295A3B46E}";
        List<StructureInfoResponse> expectedResponse = new ArrayList<>();
        //        TestCommons.createGetUnitsWithCountByStructureGlobalIdResponse("getUnitsWithCountByStructureGlobalId.json");

        //        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(structureServiceImpl.getUnitByStructureGlobalId(globalId)).thenReturn(null);
        ApiResponse apiResponse = drpServiceController.getStructureByUnit(globalId);

        List<StructureInfoResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<StructureInfoResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("getDomainInfo_Success")
    void testGetDomainInfo_Success() {

        List<String> domainNames = new ArrayList<>();
        domainNames.add("unit_status");
        domainNames.add("domain_structure_year");

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);


        List<DomainInfoResponse> expectedResponse =
                TestCommons.createGetDomainInfoResponse("getDomainInfo.json");

        when(domainItemsServiceImpl.getDomainInfoByName(domainNames)).thenReturn(expectedResponse);
        ApiResponse actualResponse = drpServiceController.getDomainInfo(domainNames);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getDomainInfo_null")
    void testGetDomainInfo_null() {

        List<String> domainNames = new ArrayList<>();
        domainNames.add("unit_status");
        domainNames.add("domain_structure_year");
       // when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);


        List<DomainInfoResponse> expectedResponse = new ArrayList<>();
        when(domainItemsServiceImpl.getDomainInfoByName(domainNames)).thenReturn(null);
        ApiResponse actualResponse = drpServiceController.getDomainInfo(domainNames);
        assertEquals(expectedResponse, actualResponse.getData());
    }


    @Test
    @DisplayName("getDomainInfo_Empty")
    void testGetDomainInfo_Empty() {

        List<String> domainNames = new ArrayList<>();
        domainNames.add("unit_status");
        domainNames.add("domain_structure_year");

        //when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);


        List<DomainInfoResponse> expectedResponse = new ArrayList<>();
        when(domainItemsServiceImpl.getDomainInfoByName(domainNames)).thenReturn(new ArrayList<>());
        ApiResponse actualResponse = drpServiceController.getDomainInfo(domainNames);
        assertEquals(expectedResponse, actualResponse.getData());
    }

    @Test
    @DisplayName("getGenQCHistory_success")
    void testGetQCStatusByUnitGlobalId_Success() {

        String globalId = "{4DAFCD11-F912-4A60-A69C-BC3303825F54}";
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<UnitStatusHistoryResponse> expectedResponse =
                TestCommons.createGetQCStatusByUnitGlobalIdResponse("getQCStatusByUnitGlobalId.json");

        when(unitServiceImpl.getGenQCHistoryByUnitGlobalId(globalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getGenQCHistory(header,globalId);
        List<UnitStatusHistoryResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitStatusHistoryResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("getSurveyHistory_empty")
    void testGetQCStatusByUnitGlobalId_empty() {

        String globalId = "{4DAFCD11-F912-4A60-A69C-BC3303825F54}";

        //when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        List<UnitStatusResponse> expectedResponse = new ArrayList<>();

        when(unitServiceImpl.getGenQCHistoryByUnitGlobalId(globalId)).thenReturn(new ArrayList());
        ApiResponse apiResponse = drpServiceController.getGenQCHistory(header,globalId);
        List<UnitStatusResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitStatusResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("getGenQCHistory_null")
    void testGetQCStatusByUnitGlobalId_null() {

        String globalId = "{4DAFCD11-F912-4A60-A69C-BC3303825F54}";

        //when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        List<UnitStatusResponse> expectedResponse = new ArrayList<>();

        when(unitServiceImpl.getGenQCHistoryByUnitGlobalId(globalId)).thenReturn(null);
        ApiResponse apiResponse = drpServiceController.getGenQCHistory(header,globalId);
        List<UnitStatusResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitStatusResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

//    @Test
//    @DisplayName("testGetHohInfoByGlobalId_Success")
//    void testGetHohInfoByGlobalId_Success() {
//        String globalId = "{774797AC-F0BE-4F8C-BC21-C206F57D40F4}";
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//
//        List<HoHResponse> expectedResponse = TestCommons.createSampleHoHResponseList("getHohInfoByUnitGlobalId.json");
//        // HoHListResponse hoHListResponse = new HoHListResponse();
//        // hoHListResponse.setHohList(expectedResponse);
//
//        when(hohServiceImpl.getHoHByGlobalId(globalId)).thenReturn(expectedResponse);
//
//        ApiResponse apiResponse = drpServiceController.getHohInfoByGlobalId(header,globalId);
//        List<HoHResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<HoHResponse>>() {
//        });
//
//        assertEquals(expectedResponse, actualResponse);
//    }

//    @Test
//    @DisplayName("testGetHohInfoByGlobalId_null")
//    void testGetHohInfoByGlobalId_null() {
//        String globalId = "{774797AC-F0BE-4F8C-BC21-C206F57D40F4}";
//
//       // when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        List<HoHResponse> expectedResponse = new ArrayList();
//                //TestCommons.createSampleHoHResponseList("getHohInfoByUnitGlobalId.json");
//        // HoHListResponse hoHListResponse = new HoHListResponse();
//        // hoHListResponse.setHohList(expectedResponse);
//
//        when(hohServiceImpl.getHoHByGlobalId(globalId)).thenReturn(null);
//
//        ApiResponse apiResponse = drpServiceController.getHohInfoByGlobalId(header,globalId);
//        List<HoHResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<HoHResponse>>() {
//        });
//
//        assertEquals(expectedResponse, actualResponse);
//    }

//    @Test
//    @DisplayName("testGetHohInfoByGlobalId_empty")
//    void testGetHohInfoByGlobalId_empty() {
//        String globalId = "{774797AC-F0BE-4F8C-BC21-C206F57D40F4}";
//
//        // when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        List<HoHResponse> expectedResponse = new ArrayList();
//        //TestCommons.createSampleHoHResponseList("getHohInfoByUnitGlobalId.json");
//        // HoHListResponse hoHListResponse = new HoHListResponse();
//        // hoHListResponse.setHohList(expectedResponse);
//
//        when(hohServiceImpl.getHoHByGlobalId(globalId)).thenReturn(expectedResponse);
//
//        ApiResponse apiResponse = drpServiceController.getHohInfoByGlobalId(header,globalId);
//        List<HoHResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<HoHResponse>>() {
//        });
//
//        assertEquals(expectedResponse, actualResponse);
//    }

//    @Test
//    @DisplayName("testGetMembersByGlobalId_Success")
//    void testGetMembersByGlobalId_Success() {
//        String relGlobalId = "{2E0A1D6E-A79A-467E-BEF0-61BCDE50A19D}";
//        List<MemberResponse> expectedResponse =
//                TestCommons.createSampleMembersResponseList("getMembersByHohGlobalId.json");
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        when(memberServiceImpl.getMembersByGlobalId(relGlobalId)).thenReturn(expectedResponse);
//        ApiResponse apiResponse = drpServiceController.getMembersByGlobalId(header,relGlobalId);
//        List<MemberResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<MemberResponse>>() {
//        });
//        assertEquals(expectedResponse, actualResponse);
//    }

//    @Test
//    @DisplayName("testGetMembersByGlobalId_empty")
//    void testGetMembersByGlobalId_empty() {
//        String relGlobalId = "{2E0A1D6E-A79A-467E-BEF0-61BCDE50A19D}";
//
//        //        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        List<MemberResponse> expectedResponse = new ArrayList<>();
//        when(memberServiceImpl.getMembersByGlobalId(relGlobalId)).thenReturn(expectedResponse);
//        ApiResponse apiResponse = drpServiceController.getMembersByGlobalId(header,relGlobalId);
//        List<MemberResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<MemberResponse>>() {
//        });
//        assertEquals(expectedResponse, actualResponse);
//
//    }

//    @Test
//    @DisplayName("testGetMembersByGlobalId_null")
//    void testGetMembersByGlobalId_null() {
//        String globalId = "{2E0A1D6E-A79A-467E-BEF0-61BCDE50A19D}";
//
//        //        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        when(memberServiceImpl.getMembersByGlobalId(globalId)).thenReturn(null);
//        ApiResponse apiResponse = drpServiceController.getMembersByGlobalId(header,globalId);
//        List<MemberResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<MemberResponse>>() {
//        });
//        assertEquals(new ArrayList<>() , actualResponse);
//    }

    @Test
    @DisplayName("getSurveyHistory_success")
    void testSurveyHistoryGlobalId_Success() {

        String globalId = "{4DAFCD11-F912-4A60-A69C-BC3303825F54}";

        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        //when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);

        List<UnitVisitHistoryResponse> expectedResponse =
                TestCommons.createGetVisitHistoryByUnitGlobalIdResponse("getSurveyHistoryByUnitGlobalId.json");

        when(unitServiceImpl.getSurveyHistoryByUnitGlobalId(globalId)).thenReturn(expectedResponse);
        ApiResponse apiResponse = drpServiceController.getSurveyHistory(header,globalId);
        List<UnitVisitHistoryResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitVisitHistoryResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("getSurveyHistory_empty")
    void testGetSurveyHistoryGlobalId_empty() {

        String globalId = "{4DAFCD11-F912-4A60-A69C-BC3303825F54}";

        //when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        List<UnitStatusResponse> expectedResponse = new ArrayList<>();

        when(unitServiceImpl.getSurveyHistoryByUnitGlobalId(globalId)).thenReturn(new ArrayList());
        ApiResponse apiResponse = drpServiceController.getSurveyHistory(header,globalId);
        List<UnitStatusResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitStatusResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("getSurveyHistory_null")
    void testGetSurveyHistoryGlobalId_null() {

        String globalId = "{4DAFCD11-F912-4A60-A69C-BC3303825F54}";

        //when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
        List<UnitStatusResponse> expectedResponse = new ArrayList<>();

        when(unitServiceImpl.getSurveyHistoryByUnitGlobalId(globalId)).thenReturn(null);
        ApiResponse apiResponse = drpServiceController.getSurveyHistory(header,globalId);
        List<UnitStatusResponse> actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<UnitStatusResponse>>() {
        });

        assertEquals(expectedResponse, actualResponse);
    }

}
