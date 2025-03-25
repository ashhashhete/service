package com.adani.drp.portal.controllers.attachments;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.configurations.SurveyProperties;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.services.DataMigration;
import com.adani.drp.portal.services.ZipExtractor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class AttachmentsController {

	private final PortalProperties portalProperties;

	private final ZipExtractor zipExtractor;

	private final SurveyProperties surveyProperties;

	private final DataMigration dataMigration;

	private ApiResponse apiResponse;

	@GetMapping("/")
	public String home() {
		log.info("Welcome to DRPPL ! ");
		return "Welcome to DRPPL...";
	}

	@GetMapping("/getAttachmentsDetails/{offset}/{limit}")
	public String getAttachmentsDetails(@PathVariable(required = true) int offset,
			@PathVariable(required = true) int limit) {

		zipExtractor.getAttachmentsDetails(offset, limit);

		return "Processing started";

	}

	@GetMapping("/getMediaDetailsByPath")
	public String getMediaDetailsByPath() {
		String path = portalProperties.getBaseDirectoryInternal();
		File folder = new File(path);
		zipExtractor.getMediaDetailsByPath(folder);
		return "Processing started";
	}

	@GetMapping("/migrateData/{offset}/{limit}")
	public String migrateData(@PathVariable(required = true) int offset, @PathVariable(required = true) int limit)
			throws IOException {

		dataMigration.migrateData(offset, limit);

		return "Processing started";

	}

	@PostMapping("/UpdateBullkAttachmentStatus/{userId}")
	public Object UpdateBullkAttachmentStatus(@RequestHeader Map<String, String> headers,
			@RequestBody List<String> unitUniqueList, @PathVariable(required = true) int userId) throws IOException {
		return zipExtractor.UpdateBullkAttachmentStatus(unitUniqueList, userId);
	}

	@GetMapping("/getDistometerData/{offset}/{limit}/{size}")
	public String getDistometerData(@PathVariable(required = true) int offset, @PathVariable(required = true) int limit,
			@PathVariable(required = true) int size) throws IOException {

		dataMigration.getDistometerData(offset, limit, size);

		return "Processing started";

	}

}