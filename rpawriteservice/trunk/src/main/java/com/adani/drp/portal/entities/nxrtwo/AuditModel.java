package com.adani.drp.portal.entities.nxrtwo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@RequiredArgsConstructor
@MappedSuperclass
public class AuditModel implements Serializable {
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "edited_by")
	private Integer editedBy;

	@Column(name = "edited_date")
	private Timestamp editedDate;

}
