package com.adani.drp.portal.services;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.QueriesConfig;
import com.adani.drp.portal.entities.core.HOHInfo;
import com.adani.drp.portal.models.responses.core.HoHResponse;
import com.adani.drp.portal.repository.core.HOHRepo;
import com.adani.drp.portal.testUtils.TestCommons;
import com.adani.drp.portal.utils.CommonMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
class HoHServiceImplTest {

    @Mock
    EntityManager entityManager;

    @Mock
    HOHRepo hohRepo;

    @Mock
    PortalProperties portalProperties;

    @Mock
    QueriesConfig queriesConfig;


    @InjectMocks
    HoHServiceImpl hoHServiceImpl;

    private Map<Integer, String> escalatedCodes = new HashMap();

    private Map<Integer, String> successCodes = new HashMap();

    private Map<Integer, String> failureCodes = new HashMap();

    private String hohByRelGolbalIdQry;

    private String hohByGolbalIdQry;



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

        hohByRelGolbalIdQry = "SELECT * FROM sde.hoh_info_evw WHERE rel_globalid = :relGlobalId ORDER BY last_edited_date DESC";

        hohByGolbalIdQry = "SELECT * FROM sde.hoh_info_evw WHERE globalid = :globalId ORDER BY last_edited_date DESC";



    }

//    @Test
//    @DisplayName("getHoHByUnitGlobalId - success")
//    void testGetHoHByUnitGlobalIdSuccess() throws Exception {
//        String relGlobalId = "{45962196-6619-43A6-B239-6F9808B1B943}";
//
//        List<HoHResponse> objList = TestCommons.createSampleHoHResponseList("getHohInfoByUnitGlobalId.json");
//
//        List<HoHResponse> expectedResponse = Collections.singletonList(objList.get(0));
//
//        List<HOHInfo> repoResponse = TestCommons.createSampleHoHRepoList(expectedResponse);
//
//        for (HoHResponse hoHResponse : expectedResponse) {
//           var dob = hoHResponse.getHohDob();
//                hoHResponse.setHohDob(CommonMethods.extractDate(hoHResponse.getHohDob()));
//        }
//
//        when(queriesConfig.getHohByUnitGlobalId()).thenReturn(hohByRelGolbalIdQry);
//
//        Query queryMock = mock(Query.class);
//        when(queryMock.getResultList()).thenReturn(repoResponse);
//        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//
//        when(entityManager.createNativeQuery(anyString(), eq(HOHInfo.class))).thenReturn(queryMock);
//
//        given(hoHServiceImpl.findHohInfoByRelGlobalId(relGlobalId)).willReturn(repoResponse);
//
//        List<HoHResponse> actualResponse = this.hoHServiceImpl.getHoHByUnitGlobalId(relGlobalId);
//
//
//        assertEquals(expectedResponse.get(0), actualResponse.get(0));
//    }
//
//    @Test
//    @DisplayName("getHoHByGlobalId - success")
//    void testGetHoHByGlobalIdSuccess() throws Exception {
//        String globalId = "{774797AC-F0BE-4F8C-BC21-C206F57D40F4}";
//
//        List<HoHResponse> expectedResponse = TestCommons.createSampleHoHResponseList("getHohInfoByGlobalId.json");
//
//        List<HOHInfo> repoResponse = TestCommons.createSampleHoHRepoList(expectedResponse);
//
//        for (HoHResponse hoHResponse : expectedResponse) {
//            var dob = hoHResponse.getHohDob();
//            hoHResponse.setHohDob(CommonMethods.extractDate(hoHResponse.getHohDob()));
//        }
//
//        when(queriesConfig.getHohByGlobalId()).thenReturn(hohByGolbalIdQry);
//
//        Query queryMock = mock(Query.class);
//        when(queryMock.getResultList()).thenReturn(repoResponse);
//        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//
//        when(entityManager.createNativeQuery(anyString(), eq(HOHInfo.class))).thenReturn(queryMock);
//        
//        given(hoHServiceImpl.findHohInfoByGlobalId(globalId)).willReturn(repoResponse);
//
//        List<HoHResponse> actualResponse = this.hoHServiceImpl.getHoHByGlobalId(globalId);
//
//        assertEquals(expectedResponse.get(0), actualResponse.get(0));
//    }
}