package com.adani.drp.portal.repository.nxrtwo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.adani.drp.portal.entities.nxrtwo.UnitAllRemarks;
import com.adani.drp.portal.utils.AppConstants;


public interface UnitAllRemarksRepo extends JpaRepository<UnitAllRemarks, String> {
	
	@Query(value = AppConstants.getUnitAllRemarksDataByLastEditedDate, nativeQuery = true)
	List<Map<String, Object>> getUnitAllRemarksDataByLastEditedDate(Date fromDate, Date toDate);

	@Query(value = AppConstants.getUnitAllRemarksData, nativeQuery = true)
	List<Map<String, Object>> getAllData();

}
