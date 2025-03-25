package com.adani.drp.portal.controllers.attachments;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.responses.attachments.CategoryCountResponse;
import com.adani.drp.portal.models.responses.attachments.CategoryList;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.services.MediaServiceImpl;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class AttachmentsControllerTest {

    @Mock
    private MediaServiceImpl mediaServiceImpl;

    @Mock
    private PortalProperties portalProperties;

    @InjectMocks
    private AttachmentsController attachmentsController;
    private TestCommons testCommons = new TestCommons();
    private ObjectMapper objectMapper;

    private Map<Integer, String> escalatedCodes = new HashMap();

    private Map<Integer, String> successCodes = new HashMap();

    private Map<Integer, String> failureCodes = new HashMap();

    private Map<String, String> header = new HashMap<>();

    private String expectedAccessToken ;


    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

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

//    @Test
//    @DisplayName("testAttachments-getByUnitGlobalId-Success")
//    void testAttachmentsGetByUnitGlobalId_Success() {
//        String globalId = "{CAFDF495-3150-4399-B125-742F78E9336B}";
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//
//        CategoryList expectedResponse = TestCommons.sampleAttachmentList("unitAttachments.json");
//
//        when(mediaServiceImpl.getAttachmentsByUnitGlobalId(globalId)).thenReturn(expectedResponse);
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByUnitGlobalId(globalId);
//
//        CategoryList actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
//        });
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    @DisplayName("testAttachments-getByUnitGlobalId-empty")
//    void testAttachmentsGetByUnitGlobalId_null() {
//        String globalId = "{CAFDF495-3150-4399-B125-742F78E9336B}";
//
////        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//
//        var expectedResponse = new CategoryList();
//
//        when(mediaServiceImpl.getAttachmentsByUnitGlobalId(globalId)).thenReturn(new CategoryList());
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByUnitGlobalId(globalId);
//
//        CategoryList actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
//        });
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    @DisplayName("testAttachments-getByUnitGlobalId-null")
//    void testAttachmentsGetByUnitGlobalId_empty() {
//        String globalId = "{CAFDF495-3150-4399-B125-742F78E9336B}";
//
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//
//        var expectedResponse = new ArrayList<>();
//
//        when(mediaServiceImpl.getAttachmentsByUnitGlobalId(globalId)).thenReturn(null);
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByUnitGlobalId(globalId);
//
////        CategoryList actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
////        });
//
//        assertEquals(expectedResponse, apiResponse.getData());
//    }


//    @Test
//    @DisplayName("testAttachments-getByHohGlobalId-Success")
//    void testAttachmentsGetByHohGlobalId_Success() {
//        String globalId = "{4E0D1698-CA1D-43C6-86CB-E56A747EEA69}";
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        CategoryList expectedResponse = TestCommons.sampleHohAttachmentList("hohAttachments.json");
//
//        when(mediaServiceImpl.getAttachmentsByHohGlobalId(globalId)).thenReturn(expectedResponse);
//
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByHohGlobalId(header,globalId);
//
//        CategoryList actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
//        });
//
//        assertEquals(expectedResponse, actualResponse);
//
//    }

//    @Test
//    @DisplayName("testAttachments-getByHohGlobalId-null")
//    void testAttachmentsGetByHohGlobalId_null() {
//        String globalId = "{4E0D1698-CA1D-43C6-86CB-E56A747EEA69}";
//
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        var expectedResponse = new ArrayList();
//
//        when(mediaServiceImpl.getAttachmentsByHohGlobalId(globalId)).thenReturn(null);
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByHohGlobalId(header,globalId);
//
////        CategoryList actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
////        });
//
//        assertEquals(expectedResponse, apiResponse.getData());
//    }

//    @Test
//    @DisplayName("testAttachments-getByHohGlobalId-empty")
//    void testAttachmentsGetByHohGlobalId_empty() {
//        String globalId = "{4E0D1698-CA1D-43C6-86CB-E56A747EEA69}";
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
////        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//
//        CategoryList expectedResponse = new CategoryList();
//
//        when(mediaServiceImpl.getAttachmentsByHohGlobalId(globalId)).thenReturn(new CategoryList());
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByHohGlobalId(header,globalId);
//
//        CategoryList actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
//        });
//
//        assertEquals(expectedResponse, actualResponse);
//    }


//    @Test
//    @DisplayName("testAttachments-getByMemberGlobalId-Success")
//    void testAttachmentsGetByMemberGlobalId_Success() {
//        String globalId = "{338AAF72-B459-465E-857E-3536B9115E2C}";
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        CategoryList expected = TestCommons.sampleMemberAttachmentList("memberAttachments.json");
//
//        when(mediaServiceImpl.getAttachmentsByMemberGlobalId(globalId)).thenReturn(expected);
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByMemberGlobalId(header,globalId);
//
//        CategoryList actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
//        });
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    @DisplayName("testAttachments-getByMemberGlobalId-null")
//    void testAttachmentsGetByMemberGlobalId_null() {
//        String globalId = "{338AAF72-B459-465E-857E-3536B9115E2C}";
//
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        var expected = new ArrayList();
//
//        when(mediaServiceImpl.getAttachmentsByMemberGlobalId(globalId)).thenReturn(null);
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByMemberGlobalId(header,globalId);
////
////        CategoryList actualResponse = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
////        });
//
//        assertEquals(expected, apiResponse.getData());
//    }

//    @Test
//    @DisplayName("testAttachments-getByMemberGlobalId-empty")
//    void testAttachmentsGetByMemberGlobalId_empty() {
//        String globalId = "{338AAF72-B459-465E-857E-3536B9115E2C}";
//
////        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        CategoryList expected = new CategoryList();
//
//        when(mediaServiceImpl.getAttachmentsByMemberGlobalId(globalId)).thenReturn(new CategoryList());
//        ApiResponse apiResponse = attachmentsController.getAttachmentsByMemberGlobalId(header,globalId);
//
//        CategoryList actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<CategoryList>() {
//        });
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByUnitGlobalId-Success")
//    void testAttachmentsGetDocumentCountByUnitGlobalId_Success() {
//        String globalId = "{4EE2B415-6E76-41A9-88C7-CF086D159289}";
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        List<CategoryCountResponse> expected = TestCommons.genericToReadJsonFile("unitDocumentCount.json",CategoryCountResponse.class);
//
//        when(mediaServiceImpl.getDocumentCountByUnitGlobalId(globalId)).thenReturn(expected);
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByUnitGlobalId(header,globalId);
//
//        List<CategoryCountResponse> actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<CategoryCountResponse>>() {
//        });
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByUnitGlobalId-null")
//    void testAttachmentsGetDocumentCountByUnitGlobalId_null() {
//        String globalId = "{4EE2B415-6E76-41A9-88C7-CF086D159289}";
//
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        var expected = new ArrayList();
//
//        when(mediaServiceImpl.getDocumentCountByUnitGlobalId(globalId)).thenReturn(null);
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByUnitGlobalId(header,globalId);
//
//        assertEquals(expected, apiResponse.getData());
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByUnitGlobalId-empty")
//    void testAttachmentsGetDocumentCountByUnitGlobalId_empty() {
//        String globalId = "{4EE2B415-6E76-41A9-88C7-CF086D159289}";
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        List<CategoryCountResponse> expected = new ArrayList<>();
//
//        when(mediaServiceImpl.getDocumentCountByUnitGlobalId(globalId)).thenReturn(new ArrayList<CategoryCountResponse>());
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByUnitGlobalId(header,globalId);
//
//        List<CategoryCountResponse> actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<CategoryCountResponse>>() {
//        });
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByHohGlobalId-Success")
//    void testAttachmentsGetDocumentCountByHohGlobalId_Success() {
//        String globalId = "{7E473FB0-5CD4-49AE-8F91-C6838D0E6473}";
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        List<CategoryCountResponse> expected = TestCommons.genericToReadJsonFile("hohDocumentCount.json",CategoryCountResponse.class);
//
//        when(mediaServiceImpl.getDocumentCountByHohGlobalId(globalId)).thenReturn(expected);
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByHohGlobalId(header,globalId);
//
//        List<CategoryCountResponse> actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<CategoryCountResponse>>() {
//        });
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByHohGlobalId-null")
//    void testAttachmentsGetDocumentCountByHohGlobalId_null() {
//        String globalId = "{7E473FB0-5CD4-49AE-8F91-C6838D0E6473}";
//
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        var expected = new ArrayList();
//
//        when(mediaServiceImpl.getDocumentCountByHohGlobalId(globalId)).thenReturn(null);
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByHohGlobalId(header,globalId);
//
//        assertEquals(expected, apiResponse.getData());
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByHohGlobalId-empty")
//    void testAttachmentsGetDocumentCountByHohGlobalId_empty() {
//        String globalId = "{7E473FB0-5CD4-49AE-8F91-C6838D0E6473}";
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        List<CategoryCountResponse> expected = new ArrayList<>();
//
//        when(mediaServiceImpl.getDocumentCountByHohGlobalId(globalId)).thenReturn(new ArrayList<CategoryCountResponse>());
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByHohGlobalId(header,globalId);
//
//        List<CategoryCountResponse> actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<CategoryCountResponse>>() {
//        });
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByMemberGlobalId-Success")
//    void testAttachmentsGetDocumentCountByMemberGlobalId_Success() {
//        String globalId = "{AA8CB042-A1D3-4C6F-B883-D12F91B70700}";
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        List<CategoryCountResponse> expected = TestCommons.genericToReadJsonFile("memberDocumentCount.json",CategoryCountResponse.class);
//
//        when(mediaServiceImpl.getDocumentCountByMemberGlobalId(globalId)).thenReturn(expected);
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByMemberGlobalId(header,globalId);
//
//        List<CategoryCountResponse> actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<CategoryCountResponse>>() {
//        });
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByMemberGlobalId-null")
//    void testAttachmentsGetDocumentCountByMemberGlobalId_null() {
//        String globalId = "{AA8CB042-A1D3-4C6F-B883-D12F91B70700}";
//
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        var expectedResponse = new ArrayList();
//
//        when(mediaServiceImpl.getDocumentCountByMemberGlobalId(globalId)).thenReturn(null);
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByMemberGlobalId(header,globalId);
//
//        assertEquals(expectedResponse, apiResponse.getData());
//    }

//    @Test
//    @DisplayName("testAttachments-getDocumentCountByMemberGlobalId-empty")
//    void testAttachmentsGetDocumentCountByMemberGlobalId_empty() {
//        String globalId = "{AA8CB042-A1D3-4C6F-B883-D12F91B70700}";
//        when(portalProperties.getEscalatedCodes()).thenReturn(escalatedCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//        List<CategoryCountResponse> expected = new ArrayList<>();
//
//        when(mediaServiceImpl.getDocumentCountByMemberGlobalId(globalId)).thenReturn(new ArrayList<CategoryCountResponse>());
//        ApiResponse apiResponse = attachmentsController.getDocumentCountByMemberGlobalId(header,globalId);
//
//        List<CategoryCountResponse> actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<CategoryCountResponse>>() {
//        });
//
//        assertEquals(expected, actual);
//    }

    //im test cases

//    @Test
//    @DisplayName("test IM Attachments-updateAttachment-Success")
//    void testUpdateAttachment_Success() throws IOException {
//        String globalId = "{AA8CB042-A1D3-4C6F-B883-D12F91B70700}";
//
//        String fileName = "renamed_keyur";
//        String fileExt = ".xlsx";
//        int fileSize = 58344;
//        int updatedBy = 1;
//        String remarks = "new_remarks";
//
//        Attachment attachment =new Attachment();
//
//        attachment.setObjectId(98l);
//        attachment.setDocumentSource("unit");
//        attachment.setDocumentCategory("Proof of payment receipt from SRA");
//        attachment.setDocumentType("Payment Receipt of transfer of tenancy from SRA (Rs. 60,000 for Commercial)");
//        attachment.setRelGlobalId("{688F7171-F25A-466C-809E-25F4A08B207C}");
//        attachment.setCreatedBy(1);
//        attachment.setCreatedDate(CommonMethods.cnvStrToTimestamp("2024-04-30 00:34:48.118"));
//        attachment.setEditedBy(0);
//        attachment.setEditedDate(CommonMethods.cnvStrToTimestamp("2024-04-30 00:34:48.118"));
//
//        AttachmentDetails attachmentDetails = new AttachmentDetails();
//        attachmentDetails.setAttachmentId(98l);
//        attachmentDetails.setObjectId(193l);
//        attachmentDetails.setFilePath("s_90a5a8de-863c-4ffc-9c4d-7420d12277a2/u_drp-s6-gn-z02-00225/proof_of_payment_receipt_from_sra/payment_receipt_of_transfer_of_tenancy_from_sra_(rs._60,000_for_commercial)_1714417487883.txt");
//        attachmentDetails.setFileName("payment_receipt_of_transfer_of_tenancy_from_sra_(rs._60,000_for_commercial)_1714417487883");
//        attachmentDetails.setFileExt(".txt");
//        attachmentDetails.setFileSource("external");
//        attachmentDetails.setFileOrder(1);
//        attachmentDetails.setDataSize(4096);
//        attachmentDetails.setRemarks("keyur_testing_test");
//        attachmentDetails.setCreatedBy(1);
//        attachmentDetails.setCreatedDate(CommonMethods.cnvStrToTimestamp("2024-04-30 00:34:48.344"));
//        attachmentDetails.setEditedBy(0);
//        attachmentDetails.setEditedDate(CommonMethods.cnvStrToTimestamp("2024-04-30 00:34:48.344"));
//
//        attachment.setAttachmentDetails(Collections.singletonList(attachmentDetails));
//
//        when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
//        when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//        AttachmentResponse expected = TestCommons.genericToReadJsonFile("memberDocumentCount.json",.class);
//
//        when(mediaServiceImpl.updateUploadFile(98l, 193l, fileName, fileExt, fileSize, updatedBy, remarks,any(InputStream.class)))
//                .thenReturn(expected);
//        ApiResponse apiResponse = attachmentsController.updateUploadFile(header,
//                fileName,
//                fileExt,
//                remarks,
//                fileSize,
//                updatedBy,
//                attObjectid,
//                attDetailsObjectid, inputStream);
//
//        List<CategoryCountResponse> actual = objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<CategoryCountResponse>>() {
//        });
//
//        assertEquals(expected, actual);
//    }

}