package com.adani.drp.portal.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.SurveyProperties;
import com.adani.drp.portal.models.requests.UnitRequest;
import com.adani.drp.portal.models.responses.UnitListElement;
import com.adani.drp.portal.models.responses.UnitResponse;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.repository.UnitRepo;
import com.adani.drp.portal.utils.CommonMethods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnitService {

	private final PortalProperties portalProperties;

	private final SurveyProperties surveyProperties;

	private final UnitRepo unitRepo;

	@PersistenceContext
	private final EntityManager entityManager;

	public UnitResponse getUnitsBySurveyDate(String fromDate, String toDate, int offset, int limit) {

		log.info("UnitService ----> getUnitsBySurveyDate");

		UnitResponse unitResponse = new UnitResponse();

		List<Map<String, Object>> unitDetails = unitRepo.findUnitsBySurveyDate(
				CommonMethods.parseDateTime(fromDate + " 00:00:00"), CommonMethods.parseDateTime(toDate + " 23:59:59"),
				offset, limit);

		log.info("adding query result to response.");

		List<UnitListElement> responseList = unitDetails.stream().map((Map<String, Object> data) -> {

			UnitListElement unit = new UnitListElement();

			unit.setUnitUniqueId((String) data.get("unit_unique_id"));
			unit.setSurveyDate(CommonMethods.cnvTimeStampToPattern("dd/MM/yyyy", (Timestamp) data.get("survey_date")));
			unit.setUnitUsage((String) data.get("unit_usage"));
			unit.setStructureYear((String) data.get("description"));
			unit.setHohName((String) data.get("hoh_name"));
			unit.setGenQcStatus((String) data.get("gen_qc_status"));
			unit.setUnitStatus((String) data.get("unit_status"));
			unit.setRemarks((String) data.get("remarks"));

			return unit;
		}).collect(Collectors.toList());
		
		List<Map<String, Object>> totalCount = unitRepo.findUnitsCountBySurveyDate(
				CommonMethods.parseDateTime(fromDate + " 00:00:00"), CommonMethods.parseDateTime(toDate + " 23:59:59"));

		unitResponse.setTotalCount(totalCount.size());
		unitResponse.setReceivedDataCount(unitDetails.size());
		unitResponse.setUnits(responseList);

		return unitResponse;
	}

	public static ApiResponse callExternalService(String bearerToken, PortalProperties portalProperties) {
		log.info("UnitService ----> callExternalService");
		ApiResponse apiResponse = new ApiResponse();
		try {

			String externalServiceUrl = portalProperties.getExtReauthenticationApi();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Secret-Key", portalProperties.getExtAuthKey());
			headers.add("Authorization", "Bearer " + bearerToken);

			log.info("ext url: {}", externalServiceUrl);
			log.info("ext headers: {}", headers.toString().replace(portalProperties.getExtAuthKey(), "XXXX"));

			HttpEntity<String> entity = new HttpEntity<>(headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<String> response = restTemplate.exchange(externalServiceUrl, HttpMethod.POST, entity,
					String.class

			);

			log.info("request sent successfully {}",
					entity.toString().replace(portalProperties.getExtAuthKey(), "XXXX"));

			if (response.getStatusCode().is2xxSuccessful()) {
				log.info("ext token retrieved");
				apiResponse = new ApiResponse(
						new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)),
						response.getBody());
			}

		} catch (Exception e) {
			log.trace("Unauthorized request!", e);
			apiResponse = new ApiResponse(new ApiResponse.Status(0, "401", portalProperties.getFailureCodes().get(401)),
					new ArrayList<>());
		}
		return apiResponse;
	}

	public Map<String, Object> getAttachmentsBySurveyID(String uniqueSurveyId, String documentSource,
	        String documentCategory) throws IOException {
	    log.info("UnitService ----> getAttachmentsBySurveyID");
	    Map<String, Object> resList = new LinkedHashMap<>();
	    String basePath = portalProperties.getBaseDirectoryInternal();

	    try {
	        List<String> docSource = new ArrayList<>();
	        if (documentSource == null) {
	            docSource.add("unit_info");
	            docSource.add("hoh_info");
	            docSource.add("member_info");
	        } else {
	            if (documentSource.equalsIgnoreCase("unit")) {
	                docSource.add("unit_info");
	            } else if (documentSource.equalsIgnoreCase("hoh")) {
	                docSource.add("hoh_info");
	            } else if (documentSource.equalsIgnoreCase("member")) {
	                docSource.add("member_info");
	            }
	        }

	        List<String> docCategory = new ArrayList<>();
	        if (documentCategory == null) {
	            docCategory.add("proof of license");
	            docCategory.add("1 year recent proof");
	            docCategory.add("proof of structure");
	            docCategory.add("proof of identity");
	            docCategory.add("aadhar card");
	            docCategory.add("hoh_member_aadhar_card");
	            docCategory.add("survey pavti");
	        } else {
	            docCategory.add(documentCategory.toLowerCase());
	        }

	        List<String> globalid = unitRepo.getGlobalid(uniqueSurveyId);
	        log.info("globalid: " + globalid.toString());

	        if (!globalid.isEmpty()) {
	        	
	        	String queryStr = "";
	        	if(docCategory.size() == 1 && (docCategory.contains("proof of structure") || docCategory.contains("proof of identity") || docCategory.contains("proof of license"))) {
	        		
	        		queryStr = "select m.document_category, m.document_type, m.parent_table_name, md.file_path, md.file_name, md.file_ext from sde.media_info_evw m " +
                            "join sde.media_details_evw md on md.rel_globalid = m.globalid " +
                            "where lower(m.parent_table_name) = :source and lower(m.document_category) in (:docCategory) and m.rel_globalid in (:globalId)";
	        	}else {
	        		queryStr = "select m.document_category, m.document_type, m.parent_table_name, md.file_path, md.file_name, md.file_ext from sde.media_info_evw m " +
                            "join sde.media_details_evw md on md.rel_globalid = m.globalid " +
                            "where m.document_type in ('Gumasta','Gumasta before 1.1.2000','Gumasta (1 Year)','Electoral Roll','Electoral Roll (1 Year)','Electricity Connection','Electricity Connection (1Year)'," +
                            "'Aadhar Card','Electricity Connection Bill','Electricity Bill','Electricity Connection','Electricity Connection (1Year)','Electricity Connection Bill'," +
                            "'Electricity Connection Receipt/ Electricity Bill','Electrol Roll Book','Electoral Roll','Electoral Roll (1 Year)','Survey Pavti/Photo Pass','Survey Pavti or Photo Pass','Voter ID','Aadhar Card','Survey Pavti') " +
                            "and lower(m.parent_table_name) = :source and lower(m.document_category) in (:docCategory) and m.rel_globalid in (:globalId)";
	        	}	        	
//	            String queryStr = "select m.document_category, m.document_type, m.parent_table_name, md.file_path, md.file_name, md.file_ext from sde.media_info_evw m " +
//	                             "join sde.media_details_evw md on md.rel_globalid = m.globalid " +
//	                             "where m.document_type in ('Gumasta','Gumasta before 1.1.2000','Gumasta (1 Year)','Electoral Roll','Electoral Roll (1 Year)','Electricity Connection','Electricity Connection (1Year)'," +
//	                             "'Aadhar Card','Electricity Connection Bill','Electricity Bill','Electricity Connection','Electricity Connection (1Year)','Electricity Connection Bill'," +
//	                             "'Electricity Connection Receipt/ Electricity Bill','Electrol Roll Book','Electoral Roll','Electoral Roll (1 Year)','Survey Pavti/Photo Pass','Survey Pavti or Photo Pass','Voter ID','Aadhar Card','Survey Pavti') " +
//	                             "and lower(m.parent_table_name) = :source and lower(m.document_category) in (:docCategory) and m.rel_globalid in (:globalId)";

	            log.info("query string: " + queryStr);
	            for (String source : docSource) {
	                Query query = entityManager.createNativeQuery(queryStr.toString());
	                query.setParameter("source", source);
	                query.setParameter("docCategory", docCategory);
	                query.setParameter("globalId", globalid);

	                String Columns = "document_category,document_type,document_source,file_path,file_name,file_ext";
	                String[] columns = Columns.split(",");
	                List<Object[]> queryList = query.getResultList();
	                List<Map<String, Object>> resultList = new ArrayList<>();

	                for (Object[] obj : queryList) {
	                    Map<String, Object> row = new LinkedHashMap<>(columns.length);
	                    for (int i = 0; i < columns.length; i++) {
	                        row.put(columns[i], obj[i] != null ? obj[i].toString() : "");
	                    }
	                    resultList.add(row);
	                }

	                List<Map<String, Object>> finalList = new ArrayList<>();
	                for (Map<String, Object> data : resultList) {
	                    Map<String, Object> dataList = new LinkedHashMap<>();

	                    dataList.put("documentCategory", data.get("document_category"));
	                    dataList.put("documentType", data.get("document_type"));
	                    dataList.put("fileName", data.get("file_name").toString() + data.get("file_ext").toString());

	                    String attachmentPath = basePath + data.get("file_path").toString()
	                            + data.get("file_name").toString() + data.get("file_ext").toString();
	                    log.info("attachmentPath: " + attachmentPath);

	                    String folderPath = basePath + data.get("file_path").toString();
	                    String fileName = data.get("file_name").toString() + data.get("file_ext").toString();

	                    File file = new File(folderPath, fileName);
	                    String Base64 = "";
	                    if (file.exists() && file.isFile()) {
	                        log.info("The file exists in the folder.");
	                        Base64 = encodeFileToBase64(attachmentPath);
	                    }
	                    dataList.put("attachment", Base64);
	                    finalList.add(dataList);
	                }

	                if (!finalList.isEmpty()) { // Only add non-empty lists to resList
	                    if (source.equalsIgnoreCase("unit_info")) {
	                        resList.put("unit", finalList);
	                    } else if (source.equalsIgnoreCase("hoh_info")) {
	                        resList.put("hoh", finalList);
	                    } else if (source.equalsIgnoreCase("member_info")) {
	                        resList.put("member", finalList);
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        log.error("Error occurred while fetching attachments by survey ID: " + e.getMessage());
	        throw new IOException("Failed to get attachments by survey ID" + e.getMessage());
	    }
	    return resList;
	}
	
	public Map<String, Object> getAttBySurveyID(String uniqueSurveyId, String documentSource,
	        String documentCategory) throws IOException {
	    log.info("UnitService ----> getAttachmentsBySurveyID");
	    Map<String, Object> resList = new LinkedHashMap<>();
	    String basePath = portalProperties.getBaseDirectoryInternal();

	    try {
	        List<String> docSource = new ArrayList<>();
	        if (documentSource == null) {
	            docSource.add("unit_info");
	            docSource.add("hoh_info");
	            docSource.add("member_info");
	        } else {
	            if (documentSource.equalsIgnoreCase("unit")) {
	                docSource.add("unit_info");
	            } else if (documentSource.equalsIgnoreCase("hoh")) {
	                docSource.add("hoh_info");
	            } else if (documentSource.equalsIgnoreCase("member")) {
	                docSource.add("member_info");
	            }
	        }

	        List<String> docCategory = new ArrayList<>();
	        if (documentCategory == null) {
	            docCategory.add("proof of license");
	            docCategory.add("1 year recent proof");
	            docCategory.add("proof of structure");
	            docCategory.add("proof of identity");
	            docCategory.add("aadhar card");
	            docCategory.add("hoh_member_aadhar_card");
	            docCategory.add("survey pavti");
	        } else {
	            docCategory.add(documentCategory.toLowerCase());
	        }

	        List<String> globalid = unitRepo.getGlobalid(uniqueSurveyId);
	        log.info("globalid: " + globalid.toString());

			if (!globalid.isEmpty()) {

				String queryStr = "select m.document_category, m.document_type, m.parent_table_name, md.file_path, md.file_name, md.file_ext from sde.media_info_evw m "
						+ "join sde.media_details_evw md on md.rel_globalid = m.globalid "
						+ "where lower(m.parent_table_name) = :source and lower(m.document_category) in (:docCategory) and m.rel_globalid in (:globalId)";

				log.info("query string: " + queryStr);
				for (String source : docSource) {
					Query query = entityManager.createNativeQuery(queryStr.toString());
					query.setParameter("source", source);
					query.setParameter("docCategory", docCategory);
					query.setParameter("globalId", globalid);

					String Columns = "document_category,document_type,document_source,file_path,file_name,file_ext";
					String[] columns = Columns.split(",");
					List<Object[]> queryList = query.getResultList();
					List<Map<String, Object>> resultList = new ArrayList<>();

					for (Object[] obj : queryList) {
						Map<String, Object> row = new LinkedHashMap<>(columns.length);
						for (int i = 0; i < columns.length; i++) {
							row.put(columns[i], obj[i] != null ? obj[i].toString() : "");
						}
						resultList.add(row);
					}

					List<Map<String, Object>> finalList = new ArrayList<>();
					for (Map<String, Object> data : resultList) {
						Map<String, Object> dataList = new LinkedHashMap<>();

						dataList.put("documentCategory", data.get("document_category"));
						dataList.put("documentType", data.get("document_type"));
						dataList.put("fileName", data.get("file_name").toString() + data.get("file_ext").toString());

						String attachmentPath = basePath + data.get("file_path").toString()
								+ data.get("file_name").toString() + data.get("file_ext").toString();
						log.info("attachmentPath: " + attachmentPath);

						String folderPath = basePath + data.get("file_path").toString();
						String fileName = data.get("file_name").toString() + data.get("file_ext").toString();

						File file = new File(folderPath, fileName);
						String Base64 = "";
						if (file.exists() && file.isFile()) {
							log.info("The file exists in the folder.");
							Base64 = encodeFileToBase64(attachmentPath);
						}
						dataList.put("attachment", Base64);
						finalList.add(dataList);
					}

					if (!finalList.isEmpty()) { // Only add non-empty lists to resList
						if (source.equalsIgnoreCase("unit_info")) {
							resList.put("unit", finalList);
						} else if (source.equalsIgnoreCase("hoh_info")) {
							resList.put("hoh", finalList);
						} else if (source.equalsIgnoreCase("member_info")) {
							resList.put("member", finalList);
						}
					}
				}
			}
	    } catch (Exception e) {
	        log.error("Error occurred while fetching attachments by survey ID: " + e.getMessage());
	        throw new IOException("Failed to get attachments by survey ID" + e.getMessage());
	    }
	    return resList;
	}


	public static String encodeFileToBase64(String filePath) throws IOException {
		// Read the file's bytes from the file path
		byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

		// Encode the bytes to a Base64 string
		return Base64.getEncoder().encodeToString(fileContent);
	}

//	public static String authenticate(String username, String password, PortalProperties portalProperties) {
//
//		log.info("UnitService ----> authenticate");
//		String res = "";
//		String authUrl = portalProperties.getAuthUrl();
//		String key = portalProperties.getKey();
//		try {
//
//			URL url = new URL(authUrl);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//			log.info("ext url: " + authUrl);
//
//			// Setting request method to POST
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestProperty("Secret-Key", key);
//
//			// Enabling input and output streams
//			conn.setDoOutput(true);
//
//			// Creating the JSON body
//			String jsonInputString = "{\"jsonrpc\": \"2.0\",\"params\": {\"username\": \"" + username + "\","
//					+ " \"password\": \"" + DigestUtils.sha512Hex(password) + "\" }}";
//
//			log.info("input string: " + jsonInputString);
//
//			// Sending the request
//			try (OutputStream os = conn.getOutputStream()) {
//				byte[] input = jsonInputString.getBytes("utf-8");
//				os.write(input, 0, input.length);
//			}
//
//			// Reading the response
//			int responseCode = conn.getResponseCode();
//			log.info("request responseCode: " + responseCode);
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				String inputLine;
//				StringBuilder response = new StringBuilder();
//
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine);
//				}
//				in.close();
//
//				// Print result
//				res = response.toString();
//
//			} else {
//				res = "POST request not worked";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
	
	
	public static String authenticate(String username, String password, PortalProperties portalProperties) {

	    log.info("UnitService ----> authenticate");
	    String res = "";
	    String authUrl = portalProperties.getAuthUrl();
	    String key = portalProperties.getKey();

	    try {
	        URL url = new URL(authUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	        log.info("ext url: " + authUrl);

	        // Setting request method to POST
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Secret-Key", key);

	        // Enabling input and output streams
	        conn.setDoOutput(true);

	        // Creating the JSON body
	        String jsonInputString = "{\"jsonrpc\": \"2.0\",\"params\": {\"username\": \"" + username + "\","
	                + " \"password\": \"" + DigestUtils.sha512Hex(password) + "\" }}";

	        log.info("input string: " + jsonInputString);

	        // Sending the request
	        try (OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }

	        // Reading the response
	        int responseCode = conn.getResponseCode();
	        log.info("request responseCode: " + responseCode);
	        
	        BufferedReader in;
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        
	        String inputLine;
	        StringBuilder response = new StringBuilder();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();

	        res = response.toString();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return res;
	}

	public ApiResponse getReprocessIDs(String fromDate, String toDate) {
		ApiResponse apiResponse = new ApiResponse();

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			if (fromDate != null || toDate != null) {

				if (fromDate != null && toDate != null) {

					log.info("fromDate: " + fromDate);
					log.info("toDate: " + toDate);

					// Parse the strings into LocalDate
					LocalDate fDate = LocalDate.parse(fromDate, formatter);
					LocalDate tDate = LocalDate.parse(toDate, formatter);

					if (fDate.isBefore(tDate) || fDate.isEqual(tDate)) {

						String queryStr = " select distinct unit_unique_id,surveyDate,unitUsage,structureYear,hohName,genQcStatus,unitStatus,remarks from sde.rpa_fetch_updated_records( '"
								+ fDate + "','" + tDate + "')";
						log.info("query string: " + queryStr);
						Query query = entityManager.createNativeQuery(queryStr.toString());

						String Columns = "unitUniqueId,surveyDate,unitUsage,structureYear,hohName,genQcStatus,unitStatus,remarks";
						String[] columns = Columns.split(",");
						List<Object[]> queryList = query.getResultList();
						List<Map<String, Object>> unitDetails = new ArrayList<>();

						for (Object[] obj : queryList) {
							Map<String, Object> row = new LinkedHashMap<>(columns.length);
							for (int i = 0; i < columns.length; i++) {
								row.put(columns[i], obj[i] != null ? obj[i].toString() : "");
							}
							unitDetails.add(row);
						}

						if (unitDetails == null) {
							log.info("unit list not found");
							apiResponse = new ApiResponse(new ApiResponse.Status(0, "404", "No Data Found"),
									unitDetails);
						} else if (unitDetails.isEmpty()) {
							log.info("unit list empty");
							apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"),
									unitDetails);
						} else {
							log.info("unitsList retrieved");
							apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), unitDetails);
						}
					} else {
						apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Range."),
								new ArrayList<>());
					}

				} else {
					apiResponse = new ApiResponse(
							new ApiResponse.Status(1, "404", "From Date and To Date Both are required."),
							new ArrayList<>());
				}

			} else {
				String queryStr = " select distinct unit_unique_id,surveyDate,unitUsage,structureYear,hohName,genQcStatus,unitStatus,remarks from sde.rpa_fetch_updated_records( )";
				log.info("query string: " + queryStr);
				Query query = entityManager.createNativeQuery(queryStr.toString());

				String Columns = "unitUniqueId,surveyDate,unitUsage,structureYear,hohName,genQcStatus,unitStatus,remarks";
				String[] columns = Columns.split(",");
				List<Object[]> queryList = query.getResultList();
				List<Map<String, Object>> unitDetails = new ArrayList<>();

				for (Object[] obj : queryList) {
					Map<String, Object> row = new LinkedHashMap<>(columns.length);
					for (int i = 0; i < columns.length; i++) {
						row.put(columns[i], obj[i] != null ? obj[i].toString() : "");
					}
					unitDetails.add(row);
				}

				if (unitDetails == null) {
					log.info("unit list not found");
					apiResponse = new ApiResponse(new ApiResponse.Status(0, "404", "No Data Found"), unitDetails);
				} else if (unitDetails.isEmpty()) {
					log.info("unit list empty");
					apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "No Data Found"), unitDetails);
				} else {
					log.info("unitsList retrieved");
					apiResponse = new ApiResponse(new ApiResponse.Status(1, "200", "Success"), unitDetails);
				}

			}
		} catch (DateTimeParseException e) {
			apiResponse = new ApiResponse(new ApiResponse.Status(1, "404", "Invalid Date Format."), new ArrayList<>());
		}

		return apiResponse;
	}

}
