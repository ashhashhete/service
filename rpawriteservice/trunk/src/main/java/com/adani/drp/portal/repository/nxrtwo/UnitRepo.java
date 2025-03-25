package com.adani.drp.portal.repository.nxrtwo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.adani.drp.portal.entities.nxrtwo.UnitInfo;
import com.adani.drp.portal.utils.AppConstants;

@Repository
public interface UnitRepo extends JpaRepository<UnitInfo, String> {
	
	@Query(AppConstants.getRelGlobalId)
	Optional<UnitInfo> getByUnitUniqueId(String unitId);
}
