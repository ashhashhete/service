package com.adani.drp.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adani.drp.portal.entities.ExternalAttachmentStatus;


@Repository
public interface ExternalAttachmentStatusRepo extends JpaRepository<ExternalAttachmentStatus, Long> {

    List<ExternalAttachmentStatus> findByRelGlobalId(String relGlobalId);

}
