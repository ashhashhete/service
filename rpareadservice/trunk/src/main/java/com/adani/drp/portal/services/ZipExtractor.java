package com.adani.drp.portal.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.SurveyProperties;
import com.adani.drp.portal.entities.ExternalAttachmentStatus;
import com.adani.drp.portal.entities.UnitInfo;
import com.adani.drp.portal.entities.internalAttachments.MediaDetailsEVW;
import com.adani.drp.portal.models.requests.ExtAttStatusRequest;
import com.adani.drp.portal.models.responses.ExtAttStatusResponse;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.repository.ExternalAttachmentStatusRepo;
import com.adani.drp.portal.repository.UnitRepo;
import com.adani.drp.portal.repository.attachments.internalAttachment.MediaDetailsEVWRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZipExtractor {

	private final PortalProperties portalProperties;

	private final SurveyProperties surveyProperties;

	private final MediaDetailsEVWRepo mediaDetailsRepo;

	private final UnitRepo unitRepo;
	
	private final ExternalAttachmentStatusRepo externalAttachmentStatusRepo;

	private static void generateExcel_Report(List<Map<String, Object>> uploadedFailed, String reportPath,
			Map<String, Object> dataCount) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet;

			sheet = workbook.createSheet("Files Report");

			Row row1 = sheet.createRow(0);
			row1.createCell(0).setCellValue("Total Record");
			row1.createCell(1).setCellValue(String.valueOf(dataCount.get("Total Record")));

			Row row2 = sheet.createRow(1);
			row2.createCell(0).setCellValue("From");
			row2.createCell(1).setCellValue(String.valueOf(dataCount.get("Offset")));
			Row row3 = sheet.createRow(2);
			row3.createCell(0).setCellValue("To");
			row3.createCell(1).setCellValue(String.valueOf(dataCount.get("Limit")));
			if (!uploadedFailed.isEmpty()) {
				// Create header row
				Row headerRow = sheet.createRow(4);
				String[] headers = { "Sr.No", "objectid", "path", "Remark" };
				for (int i = 0; i < headers.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(createHeaderCellStyle(workbook));
				}
				// Fill data rows
				int rowNum = 5;
				int count = 1;

				for (Map<String, Object> data : uploadedFailed) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(count);
					row.createCell(1).setCellValue(String.valueOf(data.get("objectid")));
					row.createCell(2).setCellValue(String.valueOf(data.get("path")));
					row.createCell(3).setCellValue(String.valueOf(data.get("Remark")));
					count++;
				}
				// Auto-size columns
				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}
			}
			// Write the output to a file
			try (FileOutputStream fileOut = new FileOutputStream(reportPath)) {
				workbook.write(fileOut);
				workbook.close();
			}
			log.info("EXCEL Report Generated");
		}
	}

	private static CellStyle createHeaderCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}

	@Async
	public Object getAttachmentsDetails(int offset, int limit) {

		String basePath = portalProperties.getBaseDirectoryInternal();
		List<MediaDetailsEVW> mediaDetails = mediaDetailsRepo.findAllByLimit(offset, limit);
		String env = surveyProperties.getEnv();

		List<Map<String, Object>> resList = mediaDetails.stream().map(mediaData -> {
			String filePath = "";

			if (mediaData.getFileExt().startsWith(".")) {
				filePath = basePath + mediaData.getFilePath() + mediaData.getFileName() + mediaData.getFileExt();
			} else {
				filePath = basePath + mediaData.getFilePath() + mediaData.getFileName() + "." + mediaData.getFileExt();
			}

			File file = new File(filePath);

			if (env.equalsIgnoreCase("dev")) {
				SetFolderPermissionsWithSudo(file);
			} else {
//				setFolderPermissions(file);
			}

			if (!file.exists() && !file.isFile()) {
				Map<String, Object> data = new LinkedHashMap<>();
				data.put("objectid", mediaData.getObjectid());
				data.put("path", filePath);
				data.put("Remark", "The file does not exist in the folder.");
				return data;
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
		int count = mediaDetailsRepo.getCount();

		Map<String, Object> dataCount = new LinkedHashMap<>();
		dataCount.put("Total Record", count);
		dataCount.put("Limit", limit);
		dataCount.put("Offset", offset);

		String reportPath = surveyProperties.getBoothBasePath();
		Date date = new Date();
		long timeMilli = date.getTime();
		String referenceId = Long.toString(timeMilli);
		String reportName = "List_Of_Files_Not_Exist_" + referenceId + ".xlsx";
		String reporturl = "";

		try {
			generateExcel_Report(resList, reportPath + reportName, dataCount);
			reporturl = surveyProperties.getBoothPdfUrl() + reportName;
		} catch (IOException e) {
			log.error("Error : " + e.getMessage());
		}

		return new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)),
				reporturl);
	}

	public static void SetFolderPermissionsWithSudo(File folder) {

//		String folderPath = "/path/to/your/folder";
		String command = "sudo chmod -R 777 " + folder.getPath();
		String username = "root";
		String password = "Gis@oyst%2023";

		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", "echo " + password + " | sudo -S " + command);
			pb.redirectErrorStream(true);
			Process process = pb.start();

			// Reading output
			process.getInputStream().transferTo(System.out);
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				System.out.println("Permissions set successfully!");
			} else {
				System.out.println("Error setting permissions!");
			}
		} catch (IOException | InterruptedException e) {
//			System.err.println("Error executing command: " + e.getMessage());
		}
	}

//	public void setFolderPermissions(File folder) {
////		log.info("Providing File Permissions");
//
//		Path path = folder.toPath();
//
//		try {
//			Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
//			// Apply the permissions
//			Files.setPosixFilePermissions(path, perms);
//			log.info("Permissions set successfully!");
//		} catch (IOException e) {
////			e.printStackTrace();
//			log.error("error: " + e.getMessage());
//		}
//	}

	public static String removeSubstring(String str, String toRemove) {
		if (str.contains(toRemove)) {
			return str.replace(toRemove, "");
		}
		return str;
	}

	@Async
	public Object getMediaDetailsByPath(File folder) {
		List<Map<String, Object>> resList = getMediaDetails(folder);
		String reportPath = surveyProperties.getBoothBasePath();
		Date date = new Date();
		long timeMilli = date.getTime();
		String referenceId = Long.toString(timeMilli);
		String reportName = "Files_Not_Exist_In_MediaDetails" + referenceId + ".xlsx";
		String reporturl = "";

		if (!resList.isEmpty()) {
			try {
				generateExcel(resList, reportPath + reportName);
				reporturl = surveyProperties.getBoothPdfUrl() + reportName;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)),
				reporturl);
	}

	public List<Map<String, Object>> getMediaDetails(File folder) {
		List<Map<String, Object>> resList = new ArrayList<>();
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			int count = 0;
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						String file_path = file.toString();
						String folderPath = portalProperties.getBaseDirectoryInternal();
						String result = removeSubstring(file_path, folderPath).replaceAll("\\\\", "/");

						Optional<MediaDetailsEVW> mediaDetailsOpt;
						try {
							mediaDetailsOpt = mediaDetailsRepo.findAttachment(result);
						} catch (Exception e) {
							log.error("Error : " + e.getMessage());
							continue;
						}

						if (!mediaDetailsOpt.isPresent()) {
							Map<String, Object> data = new LinkedHashMap<>();
							data.put("path", result);
							data.put("Remark",
									"The file exist in the folder but record not available in the mediadetails ");
							resList.add(data);
						}

						count++;
					} else if (file.isDirectory()) {
						resList.addAll(getMediaDetails(file));
					}
				}
			}
		}
		return resList;
	}

	private static void generateExcel(List<Map<String, Object>> uploadedFailed, String reportPath) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet;

			if (!uploadedFailed.isEmpty()) {
				sheet = workbook.createSheet("Files Report");
				// Create header row
				Row headerRow = sheet.createRow(0);
				String[] headers = { "Sr.No", "File path", "Remark" };
				for (int i = 0; i < headers.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(createHeaderCellStyle(workbook));
				}
				// Fill data rows
				int rowNum = 1;
				int count = 1;
				for (Map<String, Object> data : uploadedFailed) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(count);
					row.createCell(1).setCellValue(String.valueOf(data.get("path")));
					row.createCell(2).setCellValue(String.valueOf(data.get("Remark")));
					count++;
				}
				// Auto-size columns
				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}
			}
			// Write the output to a file
			try (FileOutputStream fileOut = new FileOutputStream(reportPath)) {
				workbook.write(fileOut);
				workbook.close();
			}
			log.info("EXCEL Report Generated");
		}
	}

	public Object UpdateBullkAttachmentStatus(List<String> unitUniqueList, int userId) {
		List<Map<String, Object>> resList = new ArrayList<>();

		for (String unitUniqueId : unitUniqueList) {

			Optional<UnitInfo> unit = unitRepo.getByUnitUniqueId(unitUniqueId);
			String globalid = unit.get().getGlobalId();
			ExtAttStatusRequest extAttStatusRequest = new ExtAttStatusRequest();
			extAttStatusRequest.setUnitUniqueId(unitUniqueId);
			extAttStatusRequest.setRelGlobalid(globalid);
			extAttStatusRequest.setIsUploaded(true);
			extAttStatusRequest.setEditedBy(userId);

			List<ExtAttStatusResponse> responseList = updateIMAttachmentStatus(extAttStatusRequest);
			String remarks = "";
			if (responseList == null) {
				log.info("IM status not updated");
				remarks = "IM status not updated";
			} else if (responseList.isEmpty()) {
				log.info("IM status update empty resposne");
				remarks = "IM status update empty resposne";
			} else {
				log.info("IM status update success");
				remarks = "IM status update success";
			}

			Map<String, Object> data = new LinkedHashMap<>();
			data.put("Survey Unique Id", unitUniqueId);
			data.put("Globalid", globalid);
			data.put("Remarks", remarks);
			resList.add(data);
		}
		return resList;
	}

	public List<ExtAttStatusResponse> updateIMAttachmentStatus(ExtAttStatusRequest request) {
		List<ExtAttStatusResponse> responseList = new ArrayList<>();
		List<ExternalAttachmentStatus> existingEntryList = new ArrayList<>();
		try {
			existingEntryList = externalAttachmentStatusRepo.findByRelGlobalId(request.getRelGlobalid());
			ExtAttStatusResponse response = new ExtAttStatusResponse();
			ExternalAttachmentStatus saveObj = new ExternalAttachmentStatus();
			if (existingEntryList == null || existingEntryList.isEmpty()) {
				log.info("saving new entry {}", request.getUnitUniqueId());
				saveObj.setRelGlobalId(request.getRelGlobalid());
				saveObj.setUnitUniqueId(request.getUnitUniqueId());
				saveObj.setIsUploaded(request.getIsUploaded());
				saveObj.setCreatedBy(request.getEditedBy());
				saveObj.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				var savedObj = externalAttachmentStatusRepo.save(saveObj);
				response.setIsUploaded(savedObj.getIsUploaded());
				response.setUnitUniqueId(savedObj.getUnitUniqueId());
				responseList.add(response);
			}
			if (existingEntryList.size() == 1) {
				log.info("updating existing entry {}", existingEntryList.get(0));
				saveObj = existingEntryList.get(0);
				saveObj.setIsUploaded(request.getIsUploaded());
				saveObj.setEditedBy(request.getEditedBy());
				saveObj.setEditedDate(new Timestamp(System.currentTimeMillis()));
				var savedObj = externalAttachmentStatusRepo.save(saveObj);
				response.setIsUploaded(savedObj.getIsUploaded());
				response.setUnitUniqueId(savedObj.getUnitUniqueId());
				responseList.add(response);
			}
		} catch (Exception e) {
			log.error("unable to update im status global id: {}", request.getRelGlobalid());
			responseList = new ArrayList<>();
		}
		return responseList;
	}

}
