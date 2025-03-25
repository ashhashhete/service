package com.adani.drp.portal.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.SurveyProperties;
import com.adani.drp.portal.entities.internalAttachments.MediaDetailsEVW;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.repository.attachments.internalAttachment.MediaDetailsEVWRepo;
import com.adani.drp.portal.utils.ReplaceCategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataMigration {

	private final PortalProperties portalProperties;

	private final SurveyProperties surveyProperties;

	private final MediaDetailsEVWRepo mediaDetailsRepo;

	@Autowired
	static ReplaceCategory replaceCategory;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Async
	public Object migrateData(int offset, int limit) throws IOException {
		String basePath = portalProperties.getBaseDirectoryInternal();
		String backupPath = portalProperties.getBackupDirectory();
		String env = surveyProperties.getEnv();

		List<MediaDetailsEVW> mediaDetails = mediaDetailsRepo.findAllByLimit(offset, limit);
		String query = "SELECT DISTINCT document_category FROM sde.attachment_master WHERE LOWER(document_category) = LOWER(?)";
		String sql = "UPDATE sde.media_details_evw SET file_path = ? WHERE objectid = ?";
		List<Map<String, Object>> resList = mediaDetails.stream().map(mediaData -> {
			String filePath = basePath + mediaData.getFilePath() + mediaData.getFileName()
					+ (mediaData.getFileExt().startsWith(".") ? mediaData.getFileExt() : "." + mediaData.getFileExt());
			String filePath1 = mediaData.getFilePath() + mediaData.getFileName()
					+ (mediaData.getFileExt().startsWith(".") ? mediaData.getFileExt() : "." + mediaData.getFileExt());
			String backupfilePath = backupPath + mediaData.getFilePath() + mediaData.getFileName()
					+ (mediaData.getFileExt().startsWith(".") ? mediaData.getFileExt() : "." + mediaData.getFileExt());
			boolean fileBackup = false;
			int objectid = mediaData.getObjectid();

			String mediaPath = mediaData.getFilePath();
			String documentCategory = "";
			String updatedCategory = "";
			String newFilePath = "";
			String newFilePath1 = "";
			String[] segments = mediaPath.split("/");

			if (segments.length == 4) {
				documentCategory = segments[3];
			} else if (segments.length == 5) {
				documentCategory = segments[4];
			} else if (segments.length == 6) {
				documentCategory = segments[5];
			}

			List<Map<String, Object>> dc = jdbcTemplate.queryForList(query, documentCategory);
			if (dc.isEmpty()) {
				File oldfile = new File(backupfilePath);
				if (env.equalsIgnoreCase("dev")) {
					SetFolderPermissionsWithSudo(oldfile);
				} else {
//					setFolderPermissions(file);
				}
				if (!oldfile.exists() || !oldfile.isFile()) {
					File parent = oldfile.getParentFile();
					if (parent != null && !parent.exists()) {
						boolean dirsCreated = parent.mkdirs();
						if (dirsCreated) {
							System.out.println("Directories were created successfully.");
							try {
								fileBackup(filePath, backupfilePath);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("Failed to create directories.");
						}
					} else {
						try {
							fileBackup(filePath, backupfilePath);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				try {
					updatedCategory = replaceCategory.getValFromKey(documentCategory);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				newFilePath = basePath + mediaPath.replace(documentCategory, updatedCategory) + mediaData.getFileName()
						+ (mediaData.getFileExt().startsWith(".") ? mediaData.getFileExt()
								: "." + mediaData.getFileExt());
				newFilePath1 = mediaPath.replace(documentCategory, updatedCategory) + mediaData.getFileName()
						+ (mediaData.getFileExt().startsWith(".") ? mediaData.getFileExt()
								: "." + mediaData.getFileExt());
				File newFile = new File(newFilePath);

				if (env.equalsIgnoreCase("dev")) {
					SetFolderPermissionsWithSudo(newFile);
				} else {
//					setFolderPermissions(file);
				}

				if (!newFile.exists() || !newFile.isFile()) {
					File parentDir = newFile.getParentFile();
					if (parentDir != null && !parentDir.exists()) {
						boolean dirsCreated = parentDir.mkdirs();
						if (dirsCreated) {
							System.out.println("Directories were created successfully.");
							try {
								fileBackup = fileBackup(filePath, newFilePath);
								jdbcTemplate.update(sql, mediaPath.replace(segments[3], updatedCategory), objectid);

								deleteDirectory(filePath);

							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("Failed to create directories.");
						}
					} else {
						try {
							fileBackup = fileBackup(filePath, newFilePath);
							jdbcTemplate.update(sql, mediaPath.replace(segments[3], updatedCategory), objectid);
//							Path path = Paths.get(filePath);
							deleteDirectory(filePath);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				Map<String, Object> data = new LinkedHashMap<>();
				data.put("objectid", objectid);
				data.put("basePath", basePath);
				data.put("old path", filePath1);
				data.put("new path", newFilePath1);
				data.put("is_backup", fileBackup);
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
		String reportName = "DataMigration_Report_" + offset + "_" + limit + "_" + referenceId + ".xlsx";
		String reportUrl = "";

		try {
			generateExcel_Report(resList, reportPath + reportName, dataCount);
			reportUrl = surveyProperties.getBoothPdfUrl() + reportName;
		} catch (IOException e) {
			log.error("Error : " + e.getMessage());
		}

		return new ApiResponse(new ApiResponse.Status(1, "121", portalProperties.getSuccessCodes().get(121)),
				reportUrl);
	}

	public static String removeSubstring(String str, String toRemove) {
		if (str.contains(toRemove)) {
			return str.replace(toRemove, "");
		}
		return str;
	}

	public boolean fileBackup(String sourcePath, String targetPath) throws IOException {

		Path sourceDir = Paths.get(sourcePath);
		Path targetDir = Paths.get(targetPath);
		try {
			Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Path targetPath = targetDir.resolve(sourceDir.relativize(dir));
					if (!Files.exists(targetPath)) {
						Files.createDirectory(targetPath);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(file, targetDir.resolve(sourceDir.relativize(file)),
							StandardCopyOption.REPLACE_EXISTING);
					return FileVisitResult.CONTINUE;
				}
			});

			log.info("Backup completed successfully.");
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
			log.error("Backup failed: " + e.getMessage());
			return false;
		}
	}

	private static void generateExcel_Report(List<Map<String, Object>> uploadedFailed, String reportPath,
			Map<String, Object> dataCount) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet;

			sheet = workbook.createSheet("backup Report");

			Row row1 = sheet.createRow(0);
			row1.createCell(0).setCellValue("Total Records");
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
				String[] headers = { "Sr.No", "Objectid", "Basepath", "Old Filepath", "New Filepath", "is_backup" };
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
					row.createCell(2).setCellValue(String.valueOf(data.get("basePath")));
					row.createCell(3).setCellValue(String.valueOf(data.get("old path")));
					row.createCell(4).setCellValue(String.valueOf(data.get("new path")));
					row.createCell(5).setCellValue(String.valueOf(data.get("is_backup")));
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

	public static void deleteDirectory(String filePath) throws IOException {

		Path path = Paths.get(filePath);
		String newPath = "";
		int lastSlashIndex = filePath.lastIndexOf('/');

		// Check if the last slash was found and it's not the first character
		if (lastSlashIndex > 0) {
			// Substring up to the last slash
			newPath = filePath.substring(0, lastSlashIndex);
			System.out.println("New path: " + newPath);
		} else {
			System.out.println("The file path is empty or not valid.");
		}

		if (Files.exists(path)) {
			Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
				if (!file.delete()) {
					System.err.println("Failed to delete " + file);
				}
			});
			deleteFolder(newPath);
		} else {
			System.err.println("The folder does not exist: " + path);
		}
	}

	public static void deleteFolder(String path) {

		File directory = new File(path);

		// Check if the directory is empty and delete it
		if (directory.isDirectory() && directory.list().length == 0) {
			if (directory.delete()) {
				System.out.println("Directory deleted successfully.");
			} else {
				System.out.println("Failed to delete the directory.");
			}
		} else {
			System.out.println("Directory is not empty or is not a directory.");
		}
	}

	@Async
	public void getDistometerData(int offset, int limit, int size) {
		String fileSize = size + " " + surveyProperties.getFileSize();
		String query = "SELECT m.document_category, md.data_size, md.file_path, md.file_name, md.file_ext "
				+ "FROM sde.media_details_evw md join sde.media_info_evw m on m.objectid = md.attachment_id "
				+ "WHERE md.data_size <= " + fileSize + " and m.document_category ilike 'Distometer' "
				+ "order by data_size offset ? limit ?";

		List<Map<String, Object>> distometerData = jdbcTemplate.queryForList(query, offset, limit);

		int count = mediaDetailsRepo.getDistometerCount();

		Map<String, Object> dataCount = new LinkedHashMap<>();
		dataCount.put("Total Record", count);
		dataCount.put("Limit", limit);
		dataCount.put("Offset", offset);

		// Create a new workbook and sheet
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Distometer Data");
			
			Row row1 = sheet.createRow(0);
			row1.createCell(0).setCellValue("Total Records");
			row1.createCell(1).setCellValue(String.valueOf(dataCount.get("Total Record")));

			Row row2 = sheet.createRow(1);
			row2.createCell(0).setCellValue("From");
			row2.createCell(1).setCellValue(String.valueOf(dataCount.get("Offset")));
			Row row3 = sheet.createRow(2);
			row3.createCell(0).setCellValue("To");
			row3.createCell(1).setCellValue(String.valueOf(dataCount.get("Limit")));


			// Check if the result set is empty
			if (distometerData.isEmpty()) {
				// Add a message indicating no data was found
				Row messageRow = sheet.createRow(4);
				messageRow.createCell(0).setCellValue("No files less than "+size+"KB with in given range");
			} else {
				// Create a header row
				
				Row headerRow = sheet.createRow(4);
				String[] headers = { "S No.", "Document Category", "Data Size", "File Path", "File Name",
						"File Extension" };
				for (int i = 0; i < headers.length; i++) {
					headerRow.createCell(i).setCellValue(headers[i]);
				}

				// Write data to the sheet
				int rowNum = 5;
				int serialNo = 1;
				for (Map<String, Object> row : distometerData) {
					Row excelRow = sheet.createRow(rowNum++);
					excelRow.createCell(0).setCellValue(serialNo++);
					excelRow.createCell(1).setCellValue(row.get("document_category").toString());
					excelRow.createCell(2).setCellValue(Long.parseLong(row.get("data_size").toString()));
					excelRow.createCell(3).setCellValue(row.get("file_path").toString());
					excelRow.createCell(4).setCellValue(row.get("file_name").toString());
					excelRow.createCell(5).setCellValue(row.get("file_ext").toString());
				}

				// Adjust column width to fit the content
				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}
			}

			// Write the output to a file
			String reportPath = surveyProperties.getBoothBasePath();
			long timeMilli = System.currentTimeMillis();
			String reportName = "DistometerData_" + offset + "_" + limit + "_" + timeMilli + ".xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(reportPath + reportName)) {
				workbook.write(fileOut);
			} catch (IOException e) {
				// Log the error
				e.printStackTrace();
			}
		} catch (IOException e) {
			// Log the error
			e.printStackTrace();
		}
	}

}
