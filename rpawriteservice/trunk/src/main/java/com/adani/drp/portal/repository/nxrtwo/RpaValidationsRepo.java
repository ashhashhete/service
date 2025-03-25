package com.adani.drp.portal.repository.nxrtwo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.adani.drp.portal.entities.nxrtwo.RpaValidations;
import com.adani.drp.portal.utils.AppConstants;

@Repository
public interface RpaValidationsRepo extends JpaRepository<RpaValidations, Integer> {

    @Query(AppConstants.getRpaValidationsData)
	List<RpaValidations> getByUnitUniqueId(Object unitId);
    
    void deleteByUnitUniqueId(String unitUniqueId);

    @Query(value = AppConstants.getRpaValidationsDataByUnitUniqueId, nativeQuery = true)
	List<Map<String, Object>> getRPADataByUnitUniqueId(List<String> unitId);
    
    @Query(value = AppConstants.getRPADataByLastEditedDate, nativeQuery = true)
	List<Map<String, Object>> getRPADataByLastEditedDate(Date fromDate, Date toDate);
    
    @Query(value = AppConstants.getAllRPAData, nativeQuery = true)
	List<Map<String, Object>> getAllRPAData();

}
