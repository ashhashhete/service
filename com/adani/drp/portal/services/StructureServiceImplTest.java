package com.adani.drp.portal.services;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.entities.core.structure.StructureInfo;
import com.adani.drp.portal.models.responses.core.StructureInfoResponse;
import com.adani.drp.portal.repository.core.StructureRepo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@Slf4j
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
class StructureServiceImplTest {

    @Autowired
    TestEntityManager entityManager;
    @Mock
    StructureRepo structureRepo;

    @Mock
    PortalProperties portalProperties;


    @InjectMocks
    StructureServiceImpl structureServiceImpl;

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
    @DisplayName("getUnitByStructureGlobalId - success")
    void testGetUnitByStructureGlobalId() throws Exception {
        String globalId = "{059C35A1-B07D-4134-817C-FDB295A3B46E}";

        List<StructureInfoResponse> expectedResponse =
                TestCommons.createGetUnitsWithCountByStructureGlobalIdResponse("getUnitsWithCountByStructureGlobalId.json");

        List<StructureInfo> repoResponse = TestCommons.createGetUnitsWithCountByStructureGlobalIdRepoResponse("getUnitsWithCountByStructureIdRepoResponse.json");

        given(structureRepo.findByGlobalId(globalId)).willReturn(repoResponse);

        List<StructureInfoResponse> actualResponse = this.structureServiceImpl.getUnitByStructureGlobalId(globalId);

        assertEquals(expectedResponse.get(0), actualResponse.get(0));
    }

}