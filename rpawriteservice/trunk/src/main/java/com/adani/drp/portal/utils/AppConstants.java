package com.adani.drp.portal.utils;

public class AppConstants {

	public static final String getRelGlobalId = " from UnitInfo where unitUniqueId = :unitId ";
	
	public static final String getRpaValidationsData = "from RpaValidations where unitUniqueId = :unitId";

	public static final String getRpaValidationsDataByUnitUniqueId = " SELECT objectid, electoral_roll_year, electoral_roll_part_no, electoral_roll_sr_no, electoral_roll_hut_no, rpa_status, rpa_remark, TO_CHAR(rpa_date, 'YYYY-MM-DD') as rpa_date, is_valid_hoh_name, hoh_name_remark, TO_CHAR(hoh_name_date, 'YYYY-MM-DD') as hoh_name_date, is_valid_electoral, electoral_remark, TO_CHAR(electoral_date, 'YYYY-MM-DD') as electoral_date, is_valid_electricity_bill, electricity_bill_remark, TO_CHAR(electricity_bill_date, 'YYYY-MM-DD') as electricity_bill_date, is_valid_survey_pavti, survey_pavti_remark, TO_CHAR(survey_pavti_date, 'YYYY-MM-DD') as survey_pavti_date, gumasta_remark, TO_CHAR(gumasta_date, 'YYYY-MM-DD') as gumasta_date, is_valid_gumasta, unit_unique_id, created_by, TO_CHAR(created_date, 'YYYY-MM-DD') as  created_date, edited_by, TO_CHAR(edited_date, 'YYYY-MM-DD') as edited_date\r\n"
			+ " from rpa_validations where unit_unique_id In :unitId";

	public static final String getRPADataByLastEditedDate = "  SELECT objectid, electoral_roll_year, electoral_roll_part_no, electoral_roll_sr_no, electoral_roll_hut_no, rpa_status, rpa_remark, TO_CHAR(rpa_date, 'YYYY-MM-DD') as rpa_date, is_valid_hoh_name, hoh_name_remark, TO_CHAR(hoh_name_date, 'YYYY-MM-DD') as hoh_name_date, is_valid_electoral, electoral_remark, TO_CHAR(electoral_date, 'YYYY-MM-DD') as electoral_date, is_valid_electricity_bill, electricity_bill_remark, TO_CHAR(electricity_bill_date, 'YYYY-MM-DD') as electricity_bill_date, is_valid_survey_pavti, survey_pavti_remark, TO_CHAR(survey_pavti_date, 'YYYY-MM-DD') as survey_pavti_date, gumasta_remark, TO_CHAR(gumasta_date, 'YYYY-MM-DD') as gumasta_date, is_valid_gumasta, unit_unique_id, created_by, TO_CHAR(created_date, 'YYYY-MM-DD') as  created_date, edited_by, TO_CHAR(edited_date, 'YYYY-MM-DD') as edited_date "
			+ " FROM sde.rpa_validations where COALESCE(edited_date,created_date ) between :fromDate and :toDate ";

	public static final String getAllRPAData = " SELECT objectid, electoral_roll_year, electoral_roll_part_no, electoral_roll_sr_no, electoral_roll_hut_no, rpa_status, rpa_remark, TO_CHAR(rpa_date, 'YYYY-MM-DD') as rpa_date, is_valid_hoh_name, hoh_name_remark, TO_CHAR(hoh_name_date, 'YYYY-MM-DD') as hoh_name_date, is_valid_electoral, electoral_remark, TO_CHAR(electoral_date, 'YYYY-MM-DD') as electoral_date, is_valid_electricity_bill, electricity_bill_remark, TO_CHAR(electricity_bill_date, 'YYYY-MM-DD') as electricity_bill_date, is_valid_survey_pavti, survey_pavti_remark, TO_CHAR(survey_pavti_date, 'YYYY-MM-DD') as survey_pavti_date, gumasta_remark, TO_CHAR(gumasta_date, 'YYYY-MM-DD') as gumasta_date, is_valid_gumasta, unit_unique_id, created_by, TO_CHAR(created_date, 'YYYY-MM-DD') as  created_date, edited_by, TO_CHAR(edited_date, 'YYYY-MM-DD') as edited_date "
			+ " FROM sde.rpa_validations ";

}
