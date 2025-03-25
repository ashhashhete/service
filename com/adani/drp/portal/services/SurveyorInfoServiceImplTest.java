package com.adani.drp.portal.services;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.exception.ExceptionHandler;
import com.adani.drp.portal.repository.survey.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class SurveyorInfoServiceImplTest {

	@Mock
	private UsersRepo usersRepo;

	@InjectMocks
	private SurveyorInfoServiceImpl surveyorInfoService;

	@Mock
	private PortalProperties portalProperties;

	@Mock
	ExceptionHandler exceptionHandler;

	private final UsersRepo usersRepository = Mockito.mock(UsersRepo.class);
	
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

//	@ParameterizedTest
//	@MethodSource("provideUsernameResponses")
//	@DisplayName("testGetAllUsername")
//	void testGetAllUsername(List<String> expectedResponse) {
//		when(usersRepo.findDistinctUserName()).thenReturn(expectedResponse);
//		List<String> actualResponse = surveyorInfoService.getAllUsername();
//		assertEquals(expectedResponse, actualResponse);
//	}
//
//	private static Stream<List<String>> provideUsernameResponses() {
//		return Stream.of(TestCommons.createDropDownListResponse("userNames.json"), null, Collections.emptyList(),
//				new ArrayList<>());
//	}
//	
//	 @Test
//	    public void testGetSurveyors_NullInputs_ReturnsEmptyList() {
//	        List<SurveyorResponse> surveyors = surveyorInfoService.getSurveyors(null, null, null, null, null, null);
//	        assertTrue(surveyors.isEmpty());
//	    }
//
//	    @Test
//	    public void testGetSurveyors_FetchAllSurveyors() {
//	        when(usersRepository.findAllSurveyors()).thenReturn(Collections.singletonList(getMockUser()));
//	        List<SurveyorResponse> surveyors = surveyorInfoService.getSurveyors(null, null, null, null, null, null);
//	        assertEquals(1, surveyors.size());
//	        // Add more assertions as needed
//	    }
//
//	    @Test
//	    public void testGetSurveyors_FetchSurveyorsByUsernamesAndDesignations() {
//	        List<String> usernames = List.of("user1", "user2");
//	        List<String> designations = List.of("designation1", "designation2");
//	        when(usersRepository.findByUsernameAndDesignation(usernames, designations)).thenReturn(getMockUsers());
//	        List<SurveyorResponse> surveyors = surveyorInfoService.getSurveyors(usernames, designations, null, null, null, null);
//	        assertEquals(2, surveyors.size());
//	        // Add more assertions as needed
//	    }
//
//	    @Test
//	    public void testGetSurveyors_FetchSurveyorsByUsernamesOnly() {
//	        List<String> usernames = List.of("user1", "user2");
//	        when(usersRepository.findByUsername(usernames)).thenReturn(getMockUsers());
//	        List<SurveyorResponse> surveyors = surveyorInfoService.getSurveyors(usernames, null, null, null, null, null);
//	        assertEquals(2, surveyors.size());
//	        // Add more assertions as needed
//	    }
//
//	    @Test
//	    public void testGetSurveyors_FetchSurveyorsByDesignationsOnly() {
//	        List<String> designations = List.of("designation1", "designation2");
//	        when(usersRepository.findByDesignation(designations)).thenReturn(getMockUsers());
//	        List<SurveyorResponse> surveyors = surveyorInfoService.getSurveyors(null, designations, null, null, null, null);
//	        assertEquals(2, surveyors.size());
//	        // Add more assertions as needed
//	    }
//
//	    @Test
//	    public void testGetSurveyors_FilterByStatus() {
//	        String status = "active";
//	        when(usersRepository.findByUsername(any())).thenReturn(getMockUsersWithStatus(status));
//	        List<SurveyorResponse> surveyors = surveyorInfoService.getSurveyors(null, null, status, null, null, null);
//	        assertEquals(2, surveyors.size());
//	        // Add more assertions as needed
//	    }
//
//	    @Test
//	    public void testGetSurveyors_FilterByDateRange() {
//	        String fromDate = "2022-01-01";
//	        String toDate = "2022-02-01";
//	        when(usersRepository.findByValidFrom(fromDate, toDate)).thenReturn(getMockUsers());
//	        List<SurveyorResponse> surveyors = surveyorInfoService.getSurveyors(null, null, null, "validFrom", fromDate, toDate);
//	        assertEquals(2, surveyors.size());
//	        // Add more assertions as needed
//	    }
//
//	    // Write more test cases for different scenarios
//
//	    private List<Map<String, Object>> getMockUsers() {
//	        Map<String, Object> user1 = getMockUser();
//	        Map<String, Object> user2 = getMockUser();
//	        return List.of(user1, user2);
//	    }
//
//	    private List<Map<String, Object>> getMockUsersWithStatus(String status) {
//	        Map<String, Object> user1 = getMockUser();
//	        user1.put("status", status);
//	        Map<String, Object> user2 = getMockUser();
//	        user2.put("status", status);
//	        return List.of(user1, user2);
//	    }
//
//	    private Map<String, Object> getMockUser() {
//	        Map<String, Object> user = new HashMap<>();
//	        user.put("id", 1);
//	        user.put("firstName", "John");
//	        user.put("lastName", "Doe");
//	        user.put("userName", "user");
//	        user.put("status", "active");
//	        // Add more fields as needed
//	        return user;
//	    }
//	
	

}
