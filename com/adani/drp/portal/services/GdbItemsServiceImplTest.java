package com.adani.drp.portal.services;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.responses.core.domainValues.DomainInfoResponse;
import com.adani.drp.portal.testUtils.TestCommons;
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
import java.util.ArrayList;
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
class GdbItemsServiceImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    PortalProperties portalProperties;



    @InjectMocks
    DomainItemsServiceImpl domainItemsServiceImpl;

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



    @Test
    @DisplayName("getDomainNames- args given - success")
    void testGetDomainNamesSuccess() throws Exception {
        List<String> domainNames = new ArrayList<>();
        domainNames.add("unit_status");
        domainNames.add("domain_structure_year");

        List<DomainInfoResponse> expectedResponse =
                TestCommons.createGetDomainInfoResponse("getDomainInfo.json");

        List<Object[]> objectArrayList = TestCommons.convertJsonDomainInfoToList("getDomainInfo.json");

        Query q = mock(Query.class);
        when(q.getResultList()).thenReturn(expectedResponse);

        Query queryMock = mock(Query.class);
        when(queryMock.getResultList()).thenReturn(expectedResponse);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);


        given(domainItemsServiceImpl.getDomainName(domainNames)).willReturn(objectArrayList);

        List<DomainInfoResponse> actualResponse = this.domainItemsServiceImpl.getDomainInfoByName(domainNames);

        assertEquals(expectedResponse.get(0), actualResponse.get(0));
    }

    @Test
    @DisplayName("getDomainNames- without args - success")
    void testGetDomainNamesWithoutArgsSuccess() throws Exception {
        List<String> domainNames = new ArrayList<>();
        domainNames.add("unit_status");
        domainNames.add("domain_structure_year");

        List<DomainInfoResponse> expectedResponse =
                TestCommons.createGetDomainInfoResponse("getDomainInfo.json");

        List<Object[]> objectArrayList = TestCommons.convertJsonDomainInfoToList("getDomainInfo.json");

        Query q = mock(Query.class);
        when(q.getResultList()).thenReturn(expectedResponse);

        Query queryMock = mock(Query.class);
        when(queryMock.getResultList()).thenReturn(expectedResponse);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);

        when(domainItemsServiceImpl.getAllDomainNames()).thenReturn(objectArrayList);

        List<DomainInfoResponse> actualResponse = domainItemsServiceImpl.getDomainInfoByName(null);

        assertEquals(expectedResponse.get(0), actualResponse.get(0));
    }

}