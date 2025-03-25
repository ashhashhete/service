package com.adani.drp.portal.services;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.QueriesConfig;
import com.adani.drp.portal.entities.core.MemberInfo;
import com.adani.drp.portal.models.responses.core.MemberResponse;
import com.adani.drp.portal.repository.core.MemberRepo;
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
class MemberServiceImplTest {



    @Mock
    EntityManager entityManager;

    @Mock
    MemberRepo memberRepo;

    @Mock
    PortalProperties portalProperties;

    @Mock
    QueriesConfig queriesConfig;

    @InjectMocks
    MemberServiceImpl memberServiceImpl;


    private Map<Integer, String> escalatedCodes = new HashMap();

    private Map<Integer, String> successCodes = new HashMap();

    private Map<Integer, String> failureCodes = new HashMap();

    private String memberByRelGlobalIdQry;

    private String memberByGlobalIdQry;

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

        memberByRelGlobalIdQry = "SELECT * FROM sde.member_info_evw WHERE rel_globalid = :relGlobalId";

        memberByGlobalIdQry = "SELECT * FROM sde.member_info_evw WHERE globalid = :globalId";


    }

    @Test
    @DisplayName("getMembersByHohId - success")
    void testGetMembersByHohIdSuccess() throws Exception {
        String globalId = "{95EDF48A-D488-4C91-B88E-5703F352ABFE}";

        List<MemberResponse> expectedResponse =
                TestCommons.createSampleMembersResponseList("getMembersByHohGlobalId.json");

        List<MemberInfo> repoResponse = TestCommons.createSampleMembersRepoResponseList(expectedResponse);

        for (MemberResponse memberResponse : expectedResponse) {
            memberResponse.setMemberDob(CommonMethods.extractDate(memberResponse.getMemberDob()));
        }

        when(queriesConfig.getMemberByRelGlobalId()).thenReturn(memberByRelGlobalIdQry);

        Query queryMock = mock(Query.class);
        when(queryMock.getResultList()).thenReturn(repoResponse);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);

        when(entityManager.createNativeQuery(anyString(), eq(MemberInfo.class))).thenReturn(queryMock);

        given(memberServiceImpl.findByRelGlobalId(globalId)).willReturn(repoResponse);

        List<MemberResponse> actualResponse = this.memberServiceImpl.getMembersByHohId(globalId);

        assertEquals(expectedResponse.get(0), actualResponse.get(0));
    }

//    @Test
//    @DisplayName("getMembersByGlobalId - success")
//    void testGetMembersByGlobalIdSuccess() throws Exception {
//        String globalId = "{2E0A1D6E-A79A-467E-BEF0-61BCDE50A19D}";
//
//        List<MemberResponse> expectedResponse =
//                TestCommons.createSampleMembersResponseList("getMembersByGlobalId.json");
//
//        List<MemberInfo> repoResponse = TestCommons.createSampleMembersRepoResponseList(expectedResponse);
//
//        for (MemberResponse memberResponse : expectedResponse) {
//            memberResponse.setMemberDob(CommonMethods.extractDate(memberResponse.getMemberDob()));
//        }
//
//        when(queriesConfig.getMemberByGlobalId()).thenReturn(memberByGlobalIdQry);
//
//        Query queryMock = mock(Query.class);
//        when(queryMock.getResultList()).thenReturn(repoResponse);
//        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
//
//        when(entityManager.createNativeQuery(anyString(), eq(MemberInfo.class))).thenReturn(queryMock);
//
//        given(memberServiceImpl.findByGlobalId(globalId)).willReturn(repoResponse);
//
//        List<MemberResponse> actualResponse = this.memberServiceImpl.getMembersByGlobalId(globalId);
//
//        assertEquals(expectedResponse.get(0), actualResponse.get(0));
//    }

}