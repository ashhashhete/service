package com.adani.drp.portal.utils;

public class AppConstants {

	public static final String GET_AttachmentDetails = "select * from sde.media_details_evw order by objectid ASC offset :offset limit :limit";

	public static final String GET_Attachment = "select * from sde.media_details_evw m where concat(m.file_path, m.file_name, m.file_ext) = :result limit 1";

	public static final String GET_ATTACHMENT_DETAILS_TOTAL_COUNT = "select count(*) from sde.media_details_evw";

	public static final String getRelGlobalId = "select * from sde.unit_info_evw where unit_unique_id = :unitId ";

	public static final String GET_DISTOMETER_COUNT = "select count(*) from sde.media_details_evw md join sde.media_info_evw m on m.objectid = md.attachment_id  where  m.document_category ilike 'Distometer' ";

	public static final String GET_UNITS_BY_SURVEY_DATE = "select u.unit_unique_id, u.unit_usage, d.description, u.survey_date, h.hoh_name, u.gen_qc_status, u.unit_status, u.remarks from sde.unit_info_evw u "
			+ "	join sde.hoh_info_evw h on h.rel_globalid = u.globalid "
			+ "	left join domains_mvw d on d.code = u.structure_year where u.survey_date between :fromDate and :toDate "
			+ "	order by u.objectid offset :offset limit :limit";
	
	public static final String GET_UNITS_COUNT_BY_SURVEY_DATE = "select u.unit_unique_id, u.unit_usage, d.description, u.survey_date, h.hoh_name, u.gen_qc_status, u.unit_status, u.remarks from sde.unit_info_evw u "
			+ "	join sde.hoh_info_evw h on h.rel_globalid = u.globalid "
			+ "	left join domains_mvw d on d.code = u.structure_year where u.survey_date between :fromDate and :toDate ";
	
	public static final String GET_GLOBALID = " SELECT u.globalid AS globalid\r\n"
			+ " FROM sde.unit_info_evw u\r\n"
			+ " WHERE u.unit_unique_id = :uniqueSurveyId "
			+ " UNION ALL\r\n"
			+ " SELECT h.globalid AS globalid\r\n"
			+ " FROM sde.hoh_info_evw h\r\n"
			+ " JOIN sde.unit_info_evw u ON h.rel_globalid = u.globalid\r\n"
			+ " WHERE u.unit_unique_id = :uniqueSurveyId "
			+ " UNION ALL\r\n"
			+ " SELECT m.globalid AS globalid\r\n"
			+ " FROM sde.member_info_evw m\r\n"
			+ " JOIN sde.hoh_info_evw h ON m.rel_globalid = h.globalid\r\n"
			+ " JOIN sde.unit_info_evw u ON h.rel_globalid = u.globalid\r\n"
			+ " WHERE u.unit_unique_id = :uniqueSurveyId ";

}
