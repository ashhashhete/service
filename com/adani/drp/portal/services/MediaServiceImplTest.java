package com.adani.drp.portal.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.entities.attachments.MediaInfo;
import com.adani.drp.portal.models.responses.attachments.CategoryCountResponse;
import com.adani.drp.portal.models.responses.attachments.CategoryList;
import com.adani.drp.portal.models.responses.attachments.UnitInfoAttachResponse;
import com.adani.drp.portal.repository.core.MediaInfoRepo;
import com.adani.drp.portal.repository.core.UnitRepo;
import com.adani.drp.portal.testUtils.TestCommons;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
class MediaServiceImplTest {



    @Mock
    MediaInfoRepo mediaInfoRepo;

//    @Mock
//    UnitMediaAttachRepo unitMediaAttachRepo;

//    @Mock
//    HohMediaAttachRepo hohMediaAttachRepo;

//    @Mock
//    MemberMediaAttachRepo memberMediaAttachRepo;

    @Mock
    UnitRepo unitRepo;

    @Mock
    PortalProperties portalProperties;

    @InjectMocks
    MediaServiceImpl mediaServiceImpl;


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


//    @Test
//    @DisplayName("testAttachments-getByUnitGlobalId-Success")
//    void testAttachmentsGetByUnitGlobalId_Success() throws Exception {
//        String globalId = "{4EE2B415-6E76-41A9-88C7-CF086D159289}";
//
//        given(portalProperties.getSuccessCodes()).willReturn(successCodes);
//        given(portalProperties.getImageUrlGetAttachments()).willReturn("https://digitaltwin.oystermap.com/server/rest/services/Adani_SRA/hut_polygons_v4/FeatureServer/4/{media_info_objectid}/attachments/{media_info_attach_attachmentid}");
//
//        CategoryList expectedResponse = TestCommons.sampleAttachmentList("unitAttachments.json");
//
//        List<UnitMediaAttach> unitMediaAttachRepoResponse = TestCommons
//                .ConvertUnitsInfoAttach("unitAttachmentsRepoResponse.json");
//
//        List<MediaInfo> repoResponse = TestCommons.createGetUnitsInfoAttachResponse(
//                "getMediaInfoRepoResponse.json");
//
//        List<UnitInfoAttachResponse> unitInfoAttachResponse = TestCommons.sampleunitInfoAttachResponse("unitInfoAttachResponse.json");
//
//        MediaInfo mediaInfo = repoResponse.get(0);
//
//        given(mediaInfoRepo.findByRelGlobalIdAndParentTableName(globalId,"unit_info")).willReturn(repoResponse);
//
//        given(unitMediaAttachRepo.findByMediaInfoAttachRelGlobalId(mediaInfo.getGlobalId())).willReturn(unitMediaAttachRepoResponse);
//        //given(mediaServiceImpl.getUnitCategoryTypeAttachments(unitInfoAttachResponse)).willReturn(expectedResponse);
//        CategoryList actualResponse = this.mediaServiceImpl.getAttachmentsByUnitGlobalId(globalId);
//        log.info(actualResponse.toString());
//        assertEquals(expectedResponse.getCategories().size(), actualResponse.getCategories().size());
//    }
    
//    @Test
//    @DisplayName("testAttachments-getDocumentCountByUnitGlobalId-Success")
//    void testAttachmentsGetDocumentCountByUnitGlobalId_Success() throws Exception {
//    	String globalId = "{4EE2B415-6E76-41A9-88C7-CF086D159289}";
//        List<Object[]> categoryCountsStub = new ArrayList<>();
//        categoryCountsStub.add(new Object[]{"Any Other Document", BigInteger.valueOf(1)});
//        categoryCountsStub.add(new Object[]{"Distometer", BigInteger.valueOf(1)});
//        List<CategoryCountResponse> expectedResponse = TestCommons.genericToReadJsonFile("getDocumentCountByUnitGlobalId.json",CategoryCountResponse.class);
////    	List<Object[]> expected = expectedResponse != null && !expectedResponse.isEmpty() ?
////                TestCommons.createDropDownListResponse("getStructureYears.json") :
////                new ArrayList<>();
//    	given(unitMediaAttachRepo.findByUnitGlobalIdAndParentTableName(globalId)).willReturn(categoryCountsStub);
//
//    	List<CategoryCountResponse> actualResponse = mediaServiceImpl.getDocumentCountByUnitGlobalId(globalId);
//        assertEquals(expectedResponse, actualResponse);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByHohGlobalId-Success")
//    void testAttachmentsGetDocumentCountByHohGlobalId_Success() throws Exception {
//        String globalId = "{7E473FB0-5CD4-49AE-8F91-C6838D0E6473}";
//        List<Object[]> categoryCountsStub = new ArrayList<>();
//        categoryCountsStub.add(new Object[]{"Pan Card", BigInteger.valueOf(2)});
//
//        List<CategoryCountResponse> expectedResponse = TestCommons.genericToReadJsonFile("getDocumentCountByHohGlobalId.json",CategoryCountResponse.class);
//
//        given(hohMediaAttachRepo.findByHohGlobalIdAndParentTableName(globalId)).willReturn(categoryCountsStub);
//
//        List<CategoryCountResponse> actualResponse = mediaServiceImpl.getDocumentCountByHohGlobalId(globalId);
//        assertEquals(expectedResponse, actualResponse);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByMemberGlobalId-Success")
//    void testAttachmentsGetDocumentCountByMemberGlobalId_Success() throws Exception {
//        String globalId = "{51BD17BA-C8BD-47DB-9502-38E9F5B464F8}";
//        List<Object[]> categoryCountsStub = new ArrayList<>();
//        categoryCountsStub.add(new Object[]{"hoh_member_Aadhar_Card", BigInteger.valueOf(2)});
//        categoryCountsStub.add(new Object[]{"hoh_member_Disease_Proof", BigInteger.valueOf(1)});
//        categoryCountsStub.add(new Object[]{"hoh_member_Pan_Card", BigInteger.valueOf(1)});
//        List<CategoryCountResponse> expectedResponse = TestCommons.genericToReadJsonFile("getDocumentCountByMemberGlobalId.json",CategoryCountResponse.class);
//
//        given(memberMediaAttachRepo.findByMemberGlobalIdAndParentTableName(globalId)).willReturn(categoryCountsStub);
//
//        List<CategoryCountResponse> actualResponse = mediaServiceImpl.getDocumentCountByMemberGlobalId(globalId);
//        assertEquals(expectedResponse, actualResponse);
//    }

//    @Test
//    @DisplayName("testAttachments-getByHohGlobalId-Success")
//    void testAttachmentsGetByHohGlobalId_Success() throws Exception {
//        String globalId = "{7E473FB0-5CD4-49AE-8F91-C6838D0E6473}";
//
//        given(portalProperties.getSuccessCodes()).willReturn(successCodes);
//        given(portalProperties.getImageUrlGetAttachments()).willReturn("https://digitaltwin.oystermap.com/server/rest/services/Adani_SRA/hut_polygons_v4/FeatureServer/4/{media_info_objectid}/attachments/{media_info_attach_attachmentid}");
//
//        CategoryList expectedResponse = TestCommons.sampleAttachmentList("unitAttachments.json");
//
//        List<UnitMediaAttach> unitMediaAttachRepoResponse = TestCommons
//                .ConvertUnitsInfoAttach("unitAttachmentsRepoResponse.json");
//
//        List<MediaInfo> repoResponse = TestCommons.createGetUnitsInfoAttachResponse(
//                "getMediaInfoRepoResponse.json");
//
//        List<UnitInfoAttachResponse> unitInfoAttachResponse = TestCommons.sampleunitInfoAttachResponse("unitInfoAttachResponse.json");
//
//        MediaInfo mediaInfo = repoResponse.get(0);
//
//        given(mediaInfoRepo.findByRelGlobalIdAndParentTableName(globalId,"unit_info")).willReturn(repoResponse);
//
//        given(unitMediaAttachRepo.findByMediaInfoAttachRelGlobalId(mediaInfo.getGlobalId())).willReturn(unitMediaAttachRepoResponse);
//        //given(mediaServiceImpl.getUnitCategoryTypeAttachments(unitInfoAttachResponse)).willReturn(expectedResponse);
//        CategoryList actualResponse = this.mediaServiceImpl.getAttachmentsByUnitGlobalId(globalId);
//        log.info(actualResponse.toString());
//        assertEquals(expectedResponse.getCategories().size(), actualResponse.getCategories().size());
//    }
//
//    @Test
//    @DisplayName("testAttachments-getByMemberGlobalId-Success")
//    void testAttachmentsGetByMemberGlobalId_Success() throws Exception {
//        String globalId = "{51BD17BA-C8BD-47DB-9502-38E9F5B464F8}";
//
//        given(portalProperties.getSuccessCodes()).willReturn(successCodes);
//        given(portalProperties.getImageUrlGetAttachments()).willReturn("https://digitaltwin.oystermap.com/server/rest/services/Adani_SRA/hut_polygons_v4/FeatureServer/4/{media_info_objectid}/attachments/{media_info_attach_attachmentid}");
//
//        CategoryList expectedResponse = TestCommons.sampleAttachmentList("unitAttachments.json");
//
//        List<UnitMediaAttach> unitMediaAttachRepoResponse = TestCommons
//                .ConvertUnitsInfoAttach("unitAttachmentsRepoResponse.json");
//
//        List<MediaInfo> repoResponse = TestCommons.createGetUnitsInfoAttachResponse(
//                "getMediaInfoRepoResponse.json");
//
//        List<UnitInfoAttachResponse> unitInfoAttachResponse = TestCommons.sampleunitInfoAttachResponse("unitInfoAttachResponse.json");
//
//        MediaInfo mediaInfo = repoResponse.get(0);
//
//        given(mediaInfoRepo.findByRelGlobalIdAndParentTableName(globalId,"unit_info")).willReturn(repoResponse);
//
//        given(unitMediaAttachRepo.findByMediaInfoAttachRelGlobalId(mediaInfo.getGlobalId())).willReturn(unitMediaAttachRepoResponse);
//        //given(mediaServiceImpl.getUnitCategoryTypeAttachments(unitInfoAttachResponse)).willReturn(expectedResponse);
//        CategoryList actualResponse = this.mediaServiceImpl.getAttachmentsByUnitGlobalId(globalId);
//        log.info(actualResponse.toString());
//        assertEquals(expectedResponse.getCategories().size(), actualResponse.getCategories().size());
//    }

    
}