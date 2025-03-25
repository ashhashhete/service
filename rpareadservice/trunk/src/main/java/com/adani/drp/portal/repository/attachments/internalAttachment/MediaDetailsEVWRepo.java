package com.adani.drp.portal.repository.attachments.internalAttachment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.adani.drp.portal.entities.internalAttachments.MediaDetailsEVW;
import com.adani.drp.portal.utils.AppConstants;

@Repository
public interface MediaDetailsEVWRepo extends JpaRepository<MediaDetailsEVW, Integer> {

	@Query(value = AppConstants.GET_AttachmentDetails, nativeQuery = true)
	List<MediaDetailsEVW> findAllByLimit(int offset, int limit);

	@Query(value = AppConstants.GET_Attachment, nativeQuery = true)
	Optional<MediaDetailsEVW> findAttachment(String result);

	@Query(value = AppConstants.GET_ATTACHMENT_DETAILS_TOTAL_COUNT, nativeQuery = true)
	int getCount();
	
	@Query(value = AppConstants.GET_DISTOMETER_COUNT, nativeQuery = true)
	int getDistometerCount();
}
