package com.adani.drp.portal.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.entities.nxrtwo.RpaValidations;
import com.adani.drp.portal.entities.nxrtwo.RpaValidationsUploadFailure;
import com.adani.drp.portal.entities.nxrtwo.RpaValidationsUploadStatus;
import com.adani.drp.portal.entities.nxrtwo.UnitInfo;
import com.adani.drp.portal.models.requests.RPAUnitDetailsRequest;
import com.adani.drp.portal.models.requests.RPAValidationsRequest;
import com.adani.drp.portal.models.responses.ApiResponse;
import com.adani.drp.portal.repository.nxrtwo.UnitRepo;
import com.adani.drp.portal.utils.CommonMethods;
import com.adani.drp.portal.repository.nxrtwo.RpaValidationsRepo;
import com.adani.drp.portal.repository.nxrtwo.RpaValidationsUploadFailureRepo;
import com.adani.drp.portal.repository.nxrtwo.RpaValidationsUploadStatusRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RPAServiceImpl {
	
	private final PortalProperties portalProperties;

	private final UnitRepo unitRepo;
	
	private final RpaValidationsRepo rpaValidationsRepo;
	
	private final RpaValidationsUploadStatusRepo RpaUploadStatusRepo;
	
	private final RpaValidationsUploadFailureRepo RpaUploadFailureRepo;
	
	public boolean inputValidation(RPAUnitDetailsRequest unitDetails) {
		
//		String regex = "^d{4}-d{2}-d{2}$";
		String regex = "^d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]d|3[01])$";
		String rpa_Date = unitDetails.getRpaDate().orElse(null);
		String hohNameValid_Date = unitDetails.getHohNameValidDate().orElse(null);
		String electoral_Date = unitDetails.getElectoralDate().orElse(null);
		String electricityBill_Date = unitDetails.getElectricityBillDate().orElse(null);
		String surveyPavti_Date = unitDetails.getSurveyPavtiDate().orElse(null);
		String gumasta_Date = unitDetails.getGumastaDate().orElse(null);
		
		if(rpa_Date != null && !rpa_Date.isEmpty()) {
			boolean isValid = dateValidation(rpa_Date);
			if(!isValid) {
				return false;
			}
		}
		if(hohNameValid_Date != null && !hohNameValid_Date.isEmpty()) {
			boolean isValid = dateValidation(hohNameValid_Date);
			if(!isValid) {
				return false;
			}
		}
		if(electoral_Date != null && !electoral_Date.isEmpty()) {
			boolean isValid = dateValidation(electoral_Date);
			if(!isValid) {
				return false;
			}
		}
		if(electricityBill_Date != null && !electricityBill_Date.isEmpty()) {
			boolean isValid = dateValidation(electricityBill_Date);
			if(!isValid) {
				return false;
			}
		}
		if(surveyPavti_Date != null && !surveyPavti_Date.isEmpty()) {
			boolean isValid = dateValidation(surveyPavti_Date);
			if(!isValid) {
				return false;
			}
		}
		if(gumasta_Date != null && !gumasta_Date.isEmpty()) {
			boolean isValid = dateValidation(gumasta_Date);
			if(!isValid) {
				return false;
			}
		}
		return true;
		
	}
	
	public boolean dateValidation(String date) {
        String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        Pattern pattern = Pattern.compile(regex);
//        String date = "2024-08-16";
        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

	public Object insertRPAData(RPAValidationsRequest rpaRequest) {
		log.info("Function-> RPAServiceImpl -> insertRPAData");
		int userId = rpaRequest.getUserId();
		List<RPAUnitDetailsRequest> RPAUnitDetails = rpaRequest.getUnitDetails();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		long timeMilli = date.getTime();
		String transactionId = Long.toString(timeMilli);
		
		Map<String, Object> resList = new HashMap<>();
		List<String> insertSuccess = new ArrayList<>();
		List<Map<String, Object>> insertFailed = new ArrayList<>();

		for (RPAUnitDetailsRequest unitDetails : RPAUnitDetails) {

			Optional<UnitInfo> unitdata = unitRepo.getByUnitUniqueId(unitDetails.getUniqueSurveyId());

			if (!unitdata.isEmpty()) {

				List<RpaValidations> rpaData = rpaValidationsRepo
						.getByUnitUniqueId(unitDetails.getUniqueSurveyId());

				if (rpaData.isEmpty()) {
					boolean inputValidation = inputValidation(unitDetails);
					if (inputValidation) {
						String rpa_Date = unitDetails.getRpaDate().orElse(null);
						String hohNameValid_Date = unitDetails.getHohNameValidDate().orElse(null);
						String electoral_Date = unitDetails.getElectoralDate().orElse(null);
						String electricityBill_Date = unitDetails.getElectricityBillDate().orElse(null);
						String surveyPavti_Date = unitDetails.getSurveyPavtiDate().orElse(null);
						String gumasta_Date = unitDetails.getGumastaDate().orElse(null);

						RpaValidations saveObj = new RpaValidations();

						saveObj.setUnitUniqueId(unitDetails.getUniqueSurveyId());

						Integer year = unitDetails.getElectoralRollYear().orElse(null);
						if (year != null)
							saveObj.setElectoralRollYear(year);

						if (unitDetails.getElectoralRollPartNo() != null)
							saveObj.setElectoralRollPartNo(unitDetails.getElectoralRollPartNo());

						if (unitDetails.getElectoralRollSrNo() != null)
							saveObj.setElectoralRollSrNo(unitDetails.getElectoralRollSrNo());

						if (unitDetails.getElectoralRollHutNo() != null)
							saveObj.setElectoralRollHutNo(unitDetails.getElectoralRollHutNo());

						if (unitDetails.getRpaStatus() != null)
							saveObj.setRpaStatus(unitDetails.getRpaStatus());

						if (unitDetails.getRpaRemarks() != null)
							saveObj.setRpaRemark(unitDetails.getRpaRemarks());

						if (rpa_Date != null) {
							Date rpaDate = CommonMethods.cnvStrToDate(rpa_Date);
							saveObj.setRpaDate(rpaDate);
						}

						Boolean IsValidHohName = unitDetails.getIsValidHohName().orElse(null);
						if (IsValidHohName != null)
							saveObj.setIsValidHohName(IsValidHohName);

						if (unitDetails.getHohNameRemark() != null)
							saveObj.setHohNameRemark(unitDetails.getHohNameRemark());

						if (hohNameValid_Date != null) {
							Date hohNameValidDate = CommonMethods.cnvStrToDate(hohNameValid_Date);
							saveObj.setHohNameDate(hohNameValidDate);
						}

						Boolean IsValidElectoral = unitDetails.getIsValidElectoral().orElse(null);
						if (IsValidElectoral != null)
							saveObj.setIsValidElectoral(IsValidElectoral);

						if (unitDetails.getElectoralRemark() != null)
							saveObj.setElectoralRemark(unitDetails.getElectoralRemark());

						if (electoral_Date != null) {
							Date electoralDate = CommonMethods.cnvStrToDate(electoral_Date);
							saveObj.setElectoralDate(electoralDate);
						}

						Boolean IsValidElectricityBill = unitDetails.getIsValidElectricityBill().orElse(null);
						if (IsValidElectricityBill != null)
							saveObj.setIsValidElectricityBill(IsValidElectricityBill);

						if (unitDetails.getElectricityBillRemark() != null)
							saveObj.setElectricityBillRemark(unitDetails.getElectricityBillRemark());

						if (electricityBill_Date != null) {
							Date electricityBillDate = CommonMethods.cnvStrToDate(electricityBill_Date);
							saveObj.setElectricityBillDate(electricityBillDate);
						}

						Boolean IsValidSurveyPavti = unitDetails.getIsValidSurveyPavti().orElse(null);
						if (IsValidSurveyPavti != null)
							saveObj.setIsValidSurveyPavti(IsValidSurveyPavti);

						if (unitDetails.getSurveyPavtiRemark() != null)
							saveObj.setSurveyPavtiRemark(unitDetails.getSurveyPavtiRemark());

						if (surveyPavti_Date != null) {
							Date surveyPavtiDate = CommonMethods.cnvStrToDate(surveyPavti_Date);
							saveObj.setSurveyPavtiDate(surveyPavtiDate);
						}

						Boolean IsValidGumasta = unitDetails.getIsValidGumasta().orElse(null);
						if (IsValidGumasta != null)
							saveObj.setIsValidGumasta(IsValidGumasta);

						if (unitDetails.getGumastaRemark() != null)
							saveObj.setGumastaRemark(unitDetails.getGumastaRemark());

						if (gumasta_Date != null) {
							Date gumastaDate = CommonMethods.cnvStrToDate(gumasta_Date);
							saveObj.setGumastaDate(gumastaDate);
						}

						saveObj.setCreatedBy(userId);
						saveObj.setCreatedDate(timestamp);

						try {
							rpaValidationsRepo.save(saveObj);
						} catch (Exception e) {
//						e.printStackTrace();
							log.error(e.getMessage());
							Map<String, Object> data = new LinkedHashMap<>();
							data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
							data.put("remarks", e.getMessage());
							insertFailed.add(data);
							insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "INSERT",
									userId, timestamp, e.getMessage());
						}

						insertSuccess.add(unitDetails.getUniqueSurveyId());
					}else {
						Map<String, Object> data = new LinkedHashMap<>();
						data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
						data.put("remarks", "Invalid Date Format");
						insertFailed.add(data);
						insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "INSERT", 
								userId, timestamp, "Invalid Date Format");
					}
				} else {
					Map<String, Object> data = new LinkedHashMap<>();
					data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
					data.put("remarks", "Unique Survey ID Already Present in RPA Details");
					insertFailed.add(data);
					insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "INSERT", 
							userId, timestamp, "Unique Survey ID Already Present in RPA Details");
				}
			} else {
				Map<String, Object> data = new LinkedHashMap<>();
				data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
				data.put("remarks", "Unique Survey ID is Not Present in Unit Details");
				insertFailed.add(data);
				insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "INSERT", 
						userId, timestamp, "Unique Survey ID is Not Present in Unit Details");
			}

		}
		
		String status = "";
		if(!insertSuccess.isEmpty() && !insertFailed.isEmpty()) {
			status = "Partial Success";
		}else if(insertSuccess.isEmpty() && !insertFailed.isEmpty()) {
			status = "Failed";
		}else if(!insertSuccess.isEmpty() && insertFailed.isEmpty()) {
			status = "Success";
		}
		insertRpaValidationsUploadStatus(transactionId, status, "INSERT", userId, timestamp);
		
		resList.put("insertSuccess", insertSuccess);
		resList.put("insertFailed", insertFailed);
		return new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)), resList);
	}
	
	public Object updateRPAData(RPAValidationsRequest rpaRequest) {
		log.info("Function-> RPAServiceImpl -> updateRPAData");
		int userId = rpaRequest.getUserId();
		List<RPAUnitDetailsRequest> RPAUnitDetails = rpaRequest.getUnitDetails();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		Date date = new Date();
		long timeMilli = date.getTime();
		String transactionId = Long.toString(timeMilli);
		
		Map<String, Object> resList = new HashMap<>();
		List<String> updateSuccess = new ArrayList<>();
		List<Map<String, Object>> updateFailed = new ArrayList<>();

		for (RPAUnitDetailsRequest unitDetails : RPAUnitDetails) {

			Optional<UnitInfo> unitdata = unitRepo.getByUnitUniqueId(unitDetails.getUniqueSurveyId());

			if (!unitdata.isEmpty()) {

				List<RpaValidations> rpaData = rpaValidationsRepo.getByUnitUniqueId(unitDetails.getUniqueSurveyId());

				if (!rpaData.isEmpty()) {
					boolean inputValidation = inputValidation(unitDetails);
					if (inputValidation) {
						RpaValidations saveObj = new RpaValidations();

						log.info("updating existing entry {}", rpaData.get(0));
						saveObj = rpaData.get(0);

						Integer year = unitDetails.getElectoralRollYear().orElse(null);
						if (year != null)
							saveObj.setElectoralRollYear(year);

						if (unitDetails.getElectoralRollPartNo() != null)
							saveObj.setElectoralRollPartNo(unitDetails.getElectoralRollPartNo());

						if (unitDetails.getElectoralRollSrNo() != null)
							saveObj.setElectoralRollSrNo(unitDetails.getElectoralRollSrNo());

						if (unitDetails.getElectoralRollHutNo() != null)
							saveObj.setElectoralRollHutNo(unitDetails.getElectoralRollHutNo());

						if (unitDetails.getRpaStatus() != null)
							saveObj.setRpaStatus(unitDetails.getRpaStatus());

						if (unitDetails.getRpaRemarks() != null)
							saveObj.setRpaRemark(unitDetails.getRpaRemarks());

						String rpa_Date = unitDetails.getRpaDate().orElse(null);
						if (rpa_Date != null) {
							Date rpaDate = CommonMethods.cnvStrToDate(rpa_Date);
							saveObj.setRpaDate(rpaDate);
						}

						Boolean IsValidHohName = unitDetails.getIsValidHohName().orElse(null);
						if (IsValidHohName != null)
							saveObj.setIsValidHohName(IsValidHohName);

						if (unitDetails.getHohNameRemark() != null)
							saveObj.setHohNameRemark(unitDetails.getHohNameRemark());

						String hohNameValid_Date = unitDetails.getHohNameValidDate().orElse(null);
						if (hohNameValid_Date != null) {
							Date hohNameValidDate = CommonMethods.cnvStrToDate(hohNameValid_Date);
							saveObj.setHohNameDate(hohNameValidDate);
						}

						Boolean IsValidElectoral = unitDetails.getIsValidElectoral().orElse(null);
						if (IsValidElectoral != null)
							saveObj.setIsValidElectoral(IsValidElectoral);

						if (unitDetails.getElectoralRemark() != null)
							saveObj.setElectoralRemark(unitDetails.getElectoralRemark());

						String electoral_Date = unitDetails.getElectoralDate().orElse(null);
						if (electoral_Date != null) {
							Date electoralDate = CommonMethods.cnvStrToDate(electoral_Date);
							saveObj.setElectoralDate(electoralDate);
						}

						Boolean IsValidElectricityBill = unitDetails.getIsValidElectricityBill().orElse(null);
						if (IsValidElectricityBill != null)
							saveObj.setIsValidElectricityBill(IsValidElectricityBill);

						if (unitDetails.getElectricityBillRemark() != null)
							saveObj.setElectricityBillRemark(unitDetails.getElectricityBillRemark());

						String electricityBill_Date = unitDetails.getElectricityBillDate().orElse(null);
						if (electricityBill_Date != null) {
							Date electricityBillDate = CommonMethods.cnvStrToDate(electricityBill_Date);
							saveObj.setElectricityBillDate(electricityBillDate);
						}

						Boolean IsValidSurveyPavti = unitDetails.getIsValidSurveyPavti().orElse(null);
						if (IsValidSurveyPavti != null)
							saveObj.setIsValidSurveyPavti(IsValidSurveyPavti);

						if (unitDetails.getSurveyPavtiRemark() != null)
							saveObj.setSurveyPavtiRemark(unitDetails.getSurveyPavtiRemark());

						String surveyPavti_Date = unitDetails.getSurveyPavtiDate().orElse(null);
						if (surveyPavti_Date != null) {
							Date surveyPavtiDate = CommonMethods.cnvStrToDate(surveyPavti_Date);
							saveObj.setSurveyPavtiDate(surveyPavtiDate);
						}

						Boolean IsValidGumasta = unitDetails.getIsValidGumasta().orElse(null);
						if (IsValidGumasta != null)
							saveObj.setIsValidGumasta(IsValidGumasta);

						if (unitDetails.getGumastaRemark() != null)
							saveObj.setGumastaRemark(unitDetails.getGumastaRemark());

						String gumasta_Date = unitDetails.getGumastaDate().orElse(null);
						if (gumasta_Date != null) {
							Date gumastaDate = CommonMethods.cnvStrToDate(gumasta_Date);
							saveObj.setGumastaDate(gumastaDate);
						}

						saveObj.setEditedBy(userId);
						saveObj.setEditedDate(timestamp);

						try {
							rpaValidationsRepo.save(saveObj);
						} catch (Exception e) {
							log.error(e.getMessage());
							Map<String, Object> data = new LinkedHashMap<>();
							data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
							data.put("remarks", e.getMessage());
							updateFailed.add(data);
							insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "UPDATE",
									userId, timestamp, e.getMessage());
						}

						updateSuccess.add(unitDetails.getUniqueSurveyId());
					}else {
						Map<String, Object> data = new LinkedHashMap<>();
						data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
						data.put("remarks", "Invalid Date Format");
						updateFailed.add(data);
						insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "UPDATE", 
								userId, timestamp, "Invalid Date Format");
					}
				} else {
					Map<String, Object> data = new LinkedHashMap<>();
					data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
					data.put("remarks", "Unique Survey ID is Not Present in RPA Details");
					updateFailed.add(data);
					insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "UPDATE", 
							userId, timestamp, "Unique Survey ID is Not Present in RPA Details");
				}
			} else {
				Map<String, Object> data = new LinkedHashMap<>();
				data.put("uniqueSurveyId", unitDetails.getUniqueSurveyId());
				data.put("remarks", "Unique Survey ID is Not Present in Unit Details");
				updateFailed.add(data);
				insertRpaValidationsUploadFailure(transactionId, unitDetails.getUniqueSurveyId(), "UPDATE", 
						userId, timestamp, "Unique Survey ID is Not Present in Unit Details");
			}

		}
		
		String status = "";
		if(!updateSuccess.isEmpty() && !updateFailed.isEmpty()) {
			status = "Partial Success";
		}else if(updateSuccess.isEmpty() && !updateFailed.isEmpty()) {
			status = "Failed";
		}else if(!updateSuccess.isEmpty() && updateFailed.isEmpty()) {
			status = "Success";
		}
		insertRpaValidationsUploadStatus(transactionId, status, "UPDATE", userId, timestamp);
		
		resList.put("updateSuccess", updateSuccess);
		resList.put("updateFailed", updateFailed);
		return new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)), resList);
	}

	public Object insertRpaValidationsUploadStatus(String transactionId, String status, String operation, int userId, Timestamp timestamp) {
		log.info("Function-> RPAServiceImpl -> insertRpaValidationsUploadStatus");
		RpaValidationsUploadStatus saveObj = new RpaValidationsUploadStatus();
		
		saveObj.setTransactionId(transactionId);
		saveObj.setStatus(status);
		saveObj.setOperation(operation);
		saveObj.setCreatedBy(userId);
		saveObj.setCreatedDate(timestamp);
		
		try {
			RpaUploadStatusRepo.save(saveObj);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return null;
		
	}
	
	public Object insertRpaValidationsUploadFailure(String transactionId, String unitUniqueId, String operation, 
			int userId, Timestamp timestamp, String remarks) {
		log.info("Function-> RPAServiceImpl -> insertRpaValidationsUploadFailure");
		RpaValidationsUploadFailure saveObj = new RpaValidationsUploadFailure();
		
		saveObj.setTransactionId(transactionId);
		saveObj.setUnitUniqueId(unitUniqueId);
		saveObj.setOperation(operation);
		saveObj.setRemarks(remarks);
		saveObj.setCreatedBy(userId);
		saveObj.setCreatedDate(timestamp);
		
		try {
			RpaUploadFailureRepo.save(saveObj);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return null;
		
	}

	@Transactional
	public Object deleteRPAData(String jsonString) {
		log.info("Function-> RPAServiceImpl -> deleteRPAData");
		JSONObject jObj = new JSONObject(jsonString);
		
		int userId = jObj.getInt("userId");
		JSONArray unitdetails = jObj.getJSONArray("unitDetails");
		List<String> unitDetails = CommonMethods.jsonArrayToList(unitdetails);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		Date date = new Date();
		long timeMilli = date.getTime();
		String transactionId = Long.toString(timeMilli);
		
		Map<String, Object> resList = new HashMap<>();
		List<String> deleteSuccess = new ArrayList<>();
		List<Map<String, Object>> deleteFailed = new ArrayList<>();
		
		for(String unitDetail : unitDetails) {
			
			List<RpaValidations> rpaData = rpaValidationsRepo.getByUnitUniqueId(unitDetail);
			
			if(!rpaData.isEmpty()) {
				
				try {
					rpaValidationsRepo.deleteByUnitUniqueId(unitDetail);
					deleteSuccess.add(unitDetail);
				} catch (Exception e) {
					log.error(e.getMessage());
					Map<String, Object> data = new LinkedHashMap<>();
					data.put("uniqueSurveyId", unitDetail);
					data.put("remarks", e.getMessage());
					deleteFailed.add(data);
					insertRpaValidationsUploadFailure(transactionId, unitDetail, "DELETE", userId, timestamp, e.getMessage());
				}
			}else {
				
				Map<String, Object> data = new LinkedHashMap<>();
				data.put("uniqueSurveyId", unitDetail);
				data.put("remarks", "Unique Survey ID is Not Present in RPA Details");
				deleteFailed.add(data);
				insertRpaValidationsUploadFailure(transactionId, unitDetail, "DELETE", userId, timestamp, "Unique Survey ID is Not Present in RPA Details");
			}
			
		}
		
		String status = "";
		if(!deleteSuccess.isEmpty() && !deleteFailed.isEmpty()) {
			status = "Partial Success";
		}else if(deleteSuccess.isEmpty() && !deleteFailed.isEmpty()) {
			status = "Failed";
		}else if(!deleteSuccess.isEmpty() && deleteFailed.isEmpty()) {
			status = "Success";
		}
		insertRpaValidationsUploadStatus(transactionId, status, "DELETE", userId, timestamp);
		
		resList.put("deleteSuccess", deleteSuccess);
		resList.put("deleteFailed", deleteFailed);
		return new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)), resList);
	}

	public Object getRPAData(String jsonString) {
		log.info("Function-> RPAServiceImpl -> getRPAData");
		JSONObject jObj = new JSONObject(jsonString);
		JSONArray unitdetails = jObj.getJSONArray("unitDetails");
		List<String> unitDetails = CommonMethods.jsonArrayToList(unitdetails);
		
		List<Map<String, Object>> rpaData = new ArrayList<>();
		rpaData = rpaValidationsRepo.getRPADataByUnitUniqueId(unitDetails);
		
		return new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)), rpaData);
	}
	
	

	public ApiResponse getALLRPAData(String fromDate, String toDate) {

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
						
						List<Map<String, Object>> unitDetails = rpaValidationsRepo.getRPADataByLastEditedDate(
								CommonMethods.parseDateTime(fromDate + " 00:00:00"), CommonMethods.parseDateTime(toDate + " 23:59:59"));

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
							new ApiResponse.Status(1, "404", "From Date and To Date Both are required or Both date will be null."),
							new ArrayList<>());
				}

			} else {
				List<Map<String, Object>> unitDetails = rpaValidationsRepo.getAllRPAData();

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
