package com.adani.drp.portal.controllers.survey;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.SurveyProperties;
import com.adani.drp.portal.models.requests.survey.AddUserRequest;
import com.adani.drp.portal.models.requests.survey.UpdateUserRequest;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.services.PDFService;
import com.adani.drp.portal.services.SurveyorInfoServiceImpl;
import com.adani.drp.portal.testUtils.TestCommons;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class SurveyorControllerTest {

	@Mock
	private SurveyorInfoServiceImpl surveyorInfoService;

	@Mock
	private PDFService pdfService;

	@InjectMocks
	private SurveyorController surveyorController;

	@Mock
	private SurveyProperties surveyProperties;

	@Mock
	private PortalProperties portalProperties;

	private MockMvc mockMvc;

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
	@DisplayName("testGetAllUsername")
	void testGetAllUsername() {

		when(portalProperties.getSuccessCodes()).thenReturn(successCodes);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
		List<String> usernameList = surveyorInfoService.getAllUsername();
		List<String> expectedResponse = TestCommons.createDropDownListResponse("userNames.json");
		when(usernameList).thenReturn(expectedResponse);
		ApiResponse actualResponse = surveyorController.getAllUsername(header);
		Map<String, Object> responseData = (Map<String, Object>) actualResponse.getData();
		List<String> returnedUsernames = (List<String>) responseData.get("usernameList");

		assertEquals(expectedResponse, returnedUsernames);
	}

	@Test
	@DisplayName("testGetAllDesignations_Success")
	void testGetAllDesignations_Success() {
		// Mocking
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
		List<String> expectedDesignations = new ArrayList<>();
		expectedDesignations.add("Surveyor");
		expectedDesignations.add("Sr Surveyor");
		when(surveyorInfoService.getAllDesignations()).thenReturn(expectedDesignations);

		// Execution
		Object result = surveyorController.getAllDesignations(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertEquals(expectedDesignations, result);
	}

	@Test
	@DisplayName("testGetAllDesignations_Empty")
	void testGetAllDesignations_Empty() {
		// Mocking
		when(surveyorInfoService.getAllDesignations()).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllDesignations(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetAllDesignations_Null")
	void testGetAllDesignations_Null() {
		// Mocking
		when(surveyorInfoService.getAllDesignations()).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllDesignations(header);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetAllProjects_Success")
	void testGetAllProjects_Success() {
		// Mocking
		List<String> expectedProjects = new ArrayList<>();
		expectedProjects.add("Project A");
		expectedProjects.add("Project B");
		when(surveyorInfoService.getAllProjects()).thenReturn(expectedProjects);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllProjects(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertEquals(expectedProjects, result);
	}

	@Test
	@DisplayName("testGetAllProjects_Empty")
	void testGetAllProjects_Empty() {
		// Mocking
		when(surveyorInfoService.getAllProjects()).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllProjects(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetAllProjects_Null")
	void testGetAllProjects_Null() {
		// Mocking
		when(surveyorInfoService.getAllProjects()).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllProjects(header);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetAllStatus_Success")
	void testGetAllStatus_Success() {
		// Mocking
		List<String> expectedStatuses = new ArrayList<>();
		expectedStatuses.add("Active");
		expectedStatuses.add("Inactive");
		when(surveyorInfoService.getAllStatus()).thenReturn(expectedStatuses);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllStatus(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertEquals(expectedStatuses, result);
	}

	@Test
	@DisplayName("testGetAllStatus_Empty")
	void testGetAllStatus_Empty() {
		// Mocking
		when(surveyorInfoService.getAllStatus()).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllStatus(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetAllStatus_Null")
	void testGetAllStatus_Null() {
		// Mocking
		when(surveyorInfoService.getAllStatus()).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getAllStatus(header);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetRoles_Success")
	void testGetRoles_Success() {
		// Mocking
		List<String> expectedRoles = new ArrayList<>();
		expectedRoles.add("Role A");
		expectedRoles.add("Role B");
		when(surveyorInfoService.getRoles()).thenReturn(expectedRoles);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getRoles(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertEquals(expectedRoles, result);
	}

	@Test
	@DisplayName("testGetRoles_Empty")
	void testGetRoles_Empty() {
		// Mocking
		when(surveyorInfoService.getRoles()).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getRoles(header);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetRoles_Null")
	void testGetRoles_Null() {
		// Mocking
		when(surveyorInfoService.getRoles()).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getRoles(header);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetRolesByProjectId_Success")
	void testGetRolesByProjectId_Success() {
		// Mocking
		int projectId = 123;
		List<String> expectedRoles = new ArrayList<>();
		expectedRoles.add("Role A");
		expectedRoles.add("Role B");
		when(surveyorInfoService.getRolesByProjectId(projectId)).thenReturn(expectedRoles);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getRolesByProjectId(header,projectId);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertEquals(expectedRoles, result);
	}

	@Test
	@DisplayName("testGetRolesByProjectId_Empty")
	void testGetRolesByProjectId_Empty() {
		// Mocking
		int projectId = 123;
		when(surveyorInfoService.getRolesByProjectId(projectId)).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getRolesByProjectId(header,projectId);

		// Assertion
		assertNotNull(result);
		assertTrue(result instanceof List);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetRolesByProjectId_Null")
	void testGetRolesByProjectId_Null() {
		// Mocking
		int projectId = 123;
		when(surveyorInfoService.getRolesByProjectId(projectId)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getRolesByProjectId(header,projectId);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetSurveyorById_Success")
	void testGetSurveyorById_Success() throws JSONException {
		// Mocking
		int id = 1;
		Object expectedSurveyor = new Object(); // Assuming it returns some object
		when(surveyorInfoService.getSurveyorById(id)).thenReturn(expectedSurveyor);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getSurveyorById(header,id);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedSurveyor, result);
	}

	@Test
	@DisplayName("testGetSurveyorById_Null")
	void testGetSurveyorById_Null() throws JSONException {
		// Mocking
		int id = 1;
		when(surveyorInfoService.getSurveyorById(id)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getSurveyorById(header,id);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetUserByEmployeeId_Success")
	void testGetUserByEmployeeId_Success() throws JSONException {
		// Mocking
		String empId = "EMP001";
		Object expectedUser = new Object(); // Assuming it returns some object
		when(surveyorInfoService.getUserByEmployeeId(empId)).thenReturn(expectedUser);
		//when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getUserByEmployeeId(header,empId);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedUser, result);
	}

	@Test
	@DisplayName("testGetUserByEmployeeId_Null")
	void testGetUserByEmployeeId_Null() throws JSONException {
		// Mocking
		String empId = "EMP001";
		when(surveyorInfoService.getUserByEmployeeId(empId)).thenReturn(null);
		//when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getUserByEmployeeId(header,empId);

		// Assertion
		assertNull(result);
	}

//	@Test
//	void testGetSurveyorsSuccess() {
//		// Initialize mocks
//		MockitoAnnotations.openMocks(this);
//
//		// Mock the behavior of surveyorInfoService.getSurveyors()
//		List<SurveyorResponse> surveyorList = Collections.singletonList(new SurveyorResponse());
//		when(surveyorInfoService.getSurveyors(any(), any(), any(), any(), any(), any())).thenReturn(surveyorList);
//
//		// Call the controller method
//		ApiResponse response = surveyorController.getSurveyors(null, null, null, null, null, null);
//
//		// Verify that surveyorInfoService.getSurveyors() was called
//		verify(surveyorInfoService, times(1)).getSurveyors(any(), any(), any(), any(), any(), any());
//
//		// Verify the response
//		assertEquals(1, response.getStatus().getCode()); // Assuming 1 represents success status
//		assertEquals(surveyorList, response.getData()); // Assuming data contains the surveyor list
//	}

//	@Test
//	@DisplayName("testGetSurveyors_Success")
//	void testGetSurveyors_Success() {
//		// Mocking
//		List<String> usernames = new ArrayList<>();
//		usernames.add("user1");
//		usernames.add("user2");
//		List<String> designations = new ArrayList<>();
//		String status = "active";
//		String dateRange = "2022-01-01 to 2022-12-31";
//		Object expectedSurveyors = new Object(); // Assuming it returns some object
//		when(surveyorInfoService.getSurveyors(usernames, designations, status, dateRange, null, null))
//				.thenReturn(expectedSurveyors);
//
//		// Execution
//		Object result = surveyorController.getSurveyors(usernames, designations, status, dateRange, null, null);
//
//		// Assertion
//		assertNotNull(result);
//		assertEquals(expectedSurveyors, result);
//	}
//
//	@Test
//	@DisplayName("testGetSurveyors_Null")
//	void testGetSurveyors_Null() {
//		// Mocking
//		List<String> usernames = new ArrayList<>();
//		usernames.add("user1");
//		usernames.add("user2");
//		List<String> designations = new ArrayList<>();
//		String status = "active";
//		String dateRange = "2022-01-01 to 2022-12-31";
//		when(surveyorInfoService.getSurveyors(usernames, designations, status, dateRange, null, null)).thenReturn(null);
//
//		// Execution
//		Object result = surveyorController.getSurveyors(usernames, designations, status, dateRange, null, null);
//
//		// Assertion
//		assertNull(result);
//	}

	@Test
	@DisplayName("testGetSurveyorById_Empty")
	void testGetSurveyorById_Empty() throws JSONException {
		// Mocking
		int id = 1;
		when(surveyorInfoService.getSurveyorById(id)).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getSurveyorById(header,id);

		// Assertion
		assertNotNull(result);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetUserByEmployeeId_Empty")
	void testGetUserByEmployeeId_Empty() throws JSONException {
		// Mocking
		String empId = "EMP001";
		when(surveyorInfoService.getUserByEmployeeId(empId)).thenReturn(new ArrayList<>());
//		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getUserByEmployeeId(header,empId);

		// Assertion
		assertNotNull(result);
		assertTrue(((List<?>) result).isEmpty());
	}

//	@Test
//	@DisplayName("testGetSurveyors_Empty")
//	void testGetSurveyors_Empty() {
//		// Mocking
//		List<String> usernames = new ArrayList<>();
//		usernames.add("user1");
//		usernames.add("user2");
//		List<String> designations = new ArrayList<>();
//		String status = "active";
//		String dateRange = "2022-01-01 to 2022-12-31";
//		when(surveyorInfoService.getSurveyors(usernames, designations, status, dateRange, null, null))
//				.thenReturn(new ArrayList<>());
//
//		// Execution
//		Object result = surveyorController.getSurveyors(usernames, designations, status, dateRange, null, null);
//
//		// Assertion
//		assertNotNull(result);
//		assertTrue(((List<?>) result).isEmpty());
//	}

	@Test
	@DisplayName("testUpdateSurveyorStatus_Success")
	void testUpdateSurveyorStatus_Success() {
		// Mocking
		String status = "Active";
		int userId = 1;
		int updatedBy = 2;
		Object expectedResult = new Object(); // Assuming it returns some object
		when(surveyorInfoService.updateSurveyorStatus(status, userId, updatedBy)).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updateSurveyorStatus(header,status, userId, updatedBy);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testAddUser_Success")
	void testAddUser_Success() {
		// Mocking
		AddUserRequest jsonString = new AddUserRequest();
		//String jsonString = "{}";
		Object expectedResult = new Object(); // Assuming it returns some object
		when(surveyorInfoService.addUser(jsonString)).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.addUser(header,jsonString);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testUpdateSurveyorStatus_Empty")
	void testUpdateSurveyorStatus_Empty() {
		// Mocking
		String status = "Active";
		int userId = 1;
		int updatedBy = 2;
		when(surveyorInfoService.updateSurveyorStatus(status, userId, updatedBy)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updateSurveyorStatus(header,status, userId, updatedBy);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testAddUser_Empty")
	void testAddUser_Empty() {
		// Mocking
		AddUserRequest jsonString = new AddUserRequest();
		//String jsonString = "{}";
		when(surveyorInfoService.addUser(jsonString)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.addUser(header,jsonString);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testUpdateSurveyorStatus_Null")
	void testUpdateSurveyorStatus_Null() {
		// Mocking
		String status = "Active";
		int userId = 1;
		int updatedBy = 2;
		when(surveyorInfoService.updateSurveyorStatus(status, userId, updatedBy)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updateSurveyorStatus(header,status, userId, updatedBy);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testAddUser_Null")
	void testAddUser_Null() {
		// Mocking
		AddUserRequest jsonString = new AddUserRequest();
		//String jsonString = "{}";
		when(surveyorInfoService.addUser(jsonString)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.addUser(header,jsonString);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testUpdatePassword_Success")
	void testUpdatePassword_Success() {
		// Mocking
		String password = "newPassword";
		int userId = 1;
		Object expectedResult = new Object(); // Assuming it returns some object
		when(surveyorInfoService.updatePassword(password, userId)).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updatePassword(header,password, userId);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testUpdatePassword_Empty")
	void testUpdatePassword_Empty() {
		// Mocking
		String password = "newPassword";
		int userId = 1;
		when(surveyorInfoService.updatePassword(password, userId)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updatePassword(header,password, userId);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testUpdatePassword_Null")
	void testUpdatePassword_Null() {
		// Mocking
		String password = "newPassword";
		int userId = 1;
		when(surveyorInfoService.updatePassword(password, userId)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updatePassword(header,password, userId);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testUpdateUser_Success")
	void testUpdateUser_Success() {
		// Mocking
		UpdateUserRequest jsonString = new UpdateUserRequest();
//		String jsonString = "{}";
		Object expectedResult = new Object(); // Assuming it returns some object
		when(surveyorInfoService.updateUser(jsonString)).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updateUser(header,jsonString);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testUpdateUser_Empty")
	void testUpdateUser_Empty() {
		// Mocking
		UpdateUserRequest jsonString = new UpdateUserRequest();
//		String jsonString = "{}";
		when(surveyorInfoService.updateUser(jsonString)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updateUser(header,jsonString);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testUpdateUser_Null")
	void testUpdateUser_Null() {
		// Mocking
		UpdateUserRequest jsonString = new UpdateUserRequest();
//		String jsonString = "{}";
		when(surveyorInfoService.updateUser(jsonString)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.updateUser(header,jsonString);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGenerateQRCode_Success")
	void testGenerateQRCode_Success() {
		// Mocking
		byte[] expectedBytes = { 0x01, 0x02, 0x03 }; // Sample image bytes
		when(pdfService.generateQRCode(anyString(), anyString())).thenReturn(expectedBytes);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		ResponseEntity<byte[]> responseEntity = surveyorController.generateQRCode(header,"http://example.com", "123");

		// Assertion
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertArrayEquals(expectedBytes, responseEntity.getBody());
	}

	@Test
	@DisplayName("testGenerateQRCode_NullBytes")
	void testGenerateQRCode_NullBytes() {
		// Mocking
		when(pdfService.generateQRCode(anyString(), anyString())).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		ResponseEntity<byte[]> responseEntity = surveyorController.generateQRCode(header,"http://example.com", "123");

		// Assertion
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

//	@Test
//	@DisplayName("testGenerateQRCode_Exception")
//	void testGenerateQRCode_Exception() {
//		// Mocking
//		when(pdfService.generateQRCode(anyString(), anyString())).thenThrow(RuntimeException.class);
//
//		// Execution
//		ResponseEntity<byte[]> responseEntity = surveyorController.generateQRCode("http://example.com", "123");
//
//		// Assertion
//		assertNotNull(responseEntity);
//		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//		assertNull(responseEntity.getBody());
//	}

	@Test
	@DisplayName("testCreateSurveyorIdCardPDF_Success")
	void testCreateSurveyorIdCardPDF_Success() throws DocumentException, IOException, URISyntaxException {
		// Mocking
		String jsonString = "{}";
		when(surveyorInfoService.createSurveyorIdCardPDF(jsonString, null)).thenReturn(new Object()); // Assuming it
																										// returns some
																										// object
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
		// Execution
		Object result = surveyorController.createSurveyorIdCardPDF(header,jsonString, null);

		// Assertion
		assertNotNull(result);
		// Add more specific assertions if needed
	}

	@Test
	@DisplayName("testCreateSurveyorIdCardPDF_Exception")
	void testCreateSurveyorIdCardPDF_Exception() throws DocumentException, IOException, URISyntaxException {
		// Mocking
		String jsonString = "{}";
		when(surveyorInfoService.createSurveyorIdCardPDF(jsonString, null)).thenThrow(DocumentException.class);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
		// Execution and Assertion
		assertThrows(DocumentException.class, () -> surveyorController.createSurveyorIdCardPDF(header,jsonString, null));
	}

	@Test
	@DisplayName("testGetWards_Success")
	void testGetWards_Success() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		List<String> expectedResult = new ArrayList<>();
		expectedResult.add("Ward A");
		expectedResult.add("Ward B");
		when(surveyorInfoService.getWards(status, workArea)).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getWards(header,status, workArea);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testGetWards_Empty")
	void testGetWards_Empty() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		when(surveyorInfoService.getWards(status, workArea)).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getWards(header,status, workArea);

		// Assertion
		assertNotNull(result);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetWards_Null")
	void testGetWards_Null() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		when(surveyorInfoService.getWards(status, workArea)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getWards(header,status, workArea);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetSectors_Success")
	void testGetSectors_Success() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		List<String> expectedResult = new ArrayList<>();
		expectedResult.add("Sector 1");
		expectedResult.add("Sector 2");
		when(surveyorInfoService.getSectors(status, workArea)).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getSectors(header,status, workArea);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testGetSectors_Empty")
	void testGetSectors_Empty() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		when(surveyorInfoService.getSectors(status, workArea)).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getSectors(header,status, workArea);

		// Assertion
		assertNotNull(result);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetSectors_Null")
	void testGetSectors_Null() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		when(surveyorInfoService.getSectors(status, workArea)).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getSectors(header,status, workArea);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetZones_Success")
	void testGetZones_Success() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		List<String> expectedResult = new ArrayList<>();
		expectedResult.add("Zone X");
		expectedResult.add("Zone Y");
		when(surveyorInfoService.getZones(status, workArea)).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getZones(header,status, workArea);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testGetZones_Empty")
	void testGetZones_Empty() {
		// Mocking
		String status = "active";
		List<String> workArea = new ArrayList<>();
		when(surveyorInfoService.getZones(status, workArea)).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getZones(header,status, workArea);

		// Assertion
		assertNotNull(result);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetUsers_Success")
	void testGetUsers_Success() {
		// Mocking
		List<Object> expectedResult = new ArrayList<>();
		expectedResult.add("User A");
		expectedResult.add("User B");
		when(surveyorInfoService.getUsers()).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getUsers(header);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testGetUsers_Empty")
	void testGetUsers_Empty() {
		// Mocking
		when(surveyorInfoService.getUsers()).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getUsers(header);

		// Assertion
		assertNotNull(result);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetUsers_Null")
	void testGetUsers_Null() {
		// Mocking
		when(surveyorInfoService.getUsers()).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getUsers(header);

		// Assertion
		assertNull(result);
	}

	@Test
	@DisplayName("testGetWorkAreaAllocatedUsers_Success")
	void testGetWorkAreaAllocatedUsers_Success() {
		// Mocking
		List<Object> expectedResult = new ArrayList<>();
		expectedResult.add(new Object()); // Assuming it returns some object
		expectedResult.add(new Object()); // Assuming it returns some object
		when(surveyorInfoService.getWorkAreaAllocatedUsers()).thenReturn(expectedResult);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getWorkAreaAllocatedUsers(header);

		// Assertion
		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@DisplayName("testGetWorkAreaAllocatedUsers_Empty")
	void testGetWorkAreaAllocatedUsers_Empty() {
		// Mocking
		when(surveyorInfoService.getWorkAreaAllocatedUsers()).thenReturn(new ArrayList<>());
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getWorkAreaAllocatedUsers(header);

		// Assertion
		assertNotNull(result);
		assertTrue(((List<?>) result).isEmpty());
	}

	@Test
	@DisplayName("testGetWorkAreaAllocatedUsers_Null")
	void testGetWorkAreaAllocatedUsers_Null() {
		// Mocking
		when(surveyorInfoService.getWorkAreaAllocatedUsers()).thenReturn(null);
		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);

		// Execution
		Object result = surveyorController.getWorkAreaAllocatedUsers(header);

		// Assertion
		assertNull(result);
	}

//	@Test
//	@DisplayName("testGetWorkArea_Success")
//	void testGetWorkArea_Success() {
//		// Mocking
//		String status = "active";
//		List<String> wards = new ArrayList<>();
//		List<String> sectors = new ArrayList<>();
//		List<String> zones = new ArrayList<>();
//		List<Object> expectedResult = new ArrayList<>();
//		expectedResult.add(new Object()); // Assuming it returns some object
//		expectedResult.add(new Object()); // Assuming it returns some object
//		when(surveyorInfoService.getWorkArea(status, wards, sectors, zones)).thenReturn(expectedResult);
//		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//		// Execution
//		Object result = surveyorController.getWorkArea(header,status, wards, sectors, zones);
//
//		// Assertion
//		assertNotNull(result);
//		assertEquals(expectedResult, result);
//	}
//
//	@Test
//	@DisplayName("testGetWorkArea_Empty")
//	void testGetWorkArea_Empty() {
//		// Mocking
//		String status = "active";
//		List<String> wards = new ArrayList<>();
//		List<String> sectors = new ArrayList<>();
//		List<String> zones = new ArrayList<>();
//		when(surveyorInfoService.getWorkArea(status, wards, sectors, zones)).thenReturn(new ArrayList<>());
//		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//		// Execution
//		Object result = surveyorController.getWorkArea(header,status, wards, sectors, zones);
//
//		// Assertion
//		assertNotNull(result);
//		assertTrue(((List<?>) result).isEmpty());
//	}
//
//	@Test
//	@DisplayName("testGetWorkArea_Null")
//	void testGetWorkArea_Null() {
//		// Mocking
//		String status = "active";
//		List<String> wards = new ArrayList<>();
//		List<String> sectors = new ArrayList<>();
//		List<String> zones = new ArrayList<>();
//		when(surveyorInfoService.getWorkArea(status, wards, sectors, zones)).thenReturn(null);
//		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//		// Execution
//		Object result = surveyorController.getWorkArea(header,status, wards, sectors, zones);
//
//		// Assertion
//		assertNull(result);
//	}

//	@Test
//	@DisplayName("testGetWorkAllocatedUserList_Success")
//	void testGetWorkAllocatedUserList_Success() {
//		// Mocking
//		List<String> wards = new ArrayList<>();
//		List<String> sectors = new ArrayList<>();
//		List<String> zones = new ArrayList<>();
//		List<String> workAreas = new ArrayList<>();
//		List<String> userName = new ArrayList<>();
//		List<Object> expectedResult = new ArrayList<>();
//		expectedResult.add(new Object()); // Assuming it returns some object
//		expectedResult.add(new Object()); // Assuming it returns some object
//		when(surveyorInfoService.getWorkAllocatedUserList(wards, sectors, zones, workAreas, userName))
//				.thenReturn(expectedResult);
//		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//		// Execution
//		Object result = surveyorController.getWorkAllocatedUserList(header,wards, sectors, zones, workAreas, userName);
//
//		// Assertion
//		assertNotNull(result);
//		assertEquals(expectedResult, result);
//	}
//
//	@Test
//	@DisplayName("testGetWorkAllocatedUserList_Empty")
//	void testGetWorkAllocatedUserList_Empty() {
//		// Mocking
//		List<String> wards = new ArrayList<>();
//		List<String> sectors = new ArrayList<>();
//		List<String> zones = new ArrayList<>();
//		List<String> workAreas = new ArrayList<>();
//		List<String> userName = new ArrayList<>();
//		when(surveyorInfoService.getWorkAllocatedUserList(wards, sectors, zones, workAreas, userName))
//				.thenReturn(new ArrayList<>());
//		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//
//		// Execution
//		Object result = surveyorController.getWorkAllocatedUserList(header,wards, sectors, zones, workAreas, userName);
//
//		// Assertion
//		assertNotNull(result);
//		assertTrue(((List<?>) result).isEmpty());
//	}
//
//	@Test
//	@DisplayName("testGetWorkAllocatedUserList_Null")
//	void testGetWorkAllocatedUserList_Null() {
//		// Mocking
//		List<String> wards = new ArrayList<>();
//		List<String> sectors = new ArrayList<>();
//		List<String> zones = new ArrayList<>();
//		List<String> workAreas = new ArrayList<>();
//		List<String> userName = new ArrayList<>();
//		when(surveyorInfoService.getWorkAllocatedUserList(wards, sectors, zones, workAreas, userName)).thenReturn(null);
//		when(portalProperties.getExpectedAccessToken()).thenReturn(expectedAccessToken);
//		// Execution
//		Object result = surveyorController.getWorkAllocatedUserList(header,wards, sectors, zones, workAreas, userName);
//
//		// Assertion
//		assertNull(result);
//	}

}
