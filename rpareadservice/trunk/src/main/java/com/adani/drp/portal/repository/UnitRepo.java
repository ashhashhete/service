package com.adani.drp.portal.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.adani.drp.portal.entities.UnitInfo;
import com.adani.drp.portal.utils.AppConstants;

@Repository
public interface UnitRepo extends JpaRepository<UnitInfo, String> {

	@Query(value = AppConstants.getRelGlobalId, nativeQuery = true)
	Optional<UnitInfo> getByUnitUniqueId(String unitId);

	@Query(value = AppConstants.GET_UNITS_BY_SURVEY_DATE, nativeQuery = true)
	List<Map<String, Object>> findUnitsBySurveyDate(Date fromDate, Date toDate, int offset, int limit);
	
	@Query(value = AppConstants.GET_GLOBALID, nativeQuery = true)
	List<String> getGlobalid(String uniqueSurveyId);
	
	@Query(value = AppConstants.GET_UNITS_COUNT_BY_SURVEY_DATE, nativeQuery = true)
	List<Map<String, Object>> findUnitsCountBySurveyDate(Date fromDate, Date toDate);

}
