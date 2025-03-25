package com.adani.drp.portal.repository.nxrtwo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adani.drp.portal.entities.nxrtwo.RpaValidationsUploadFailure;

@Repository
public interface RpaValidationsUploadFailureRepo extends JpaRepository<RpaValidationsUploadFailure, Integer> {

}
