package com.adani.drp.portal.entities.nxrtwo;

import java.sql.Timestamp;

import javax.persistence.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "rpa_validations_upload_failure")
@Data
@RequiredArgsConstructor
public class RpaValidationsUploadFailure {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objectid")
    private Long objectId;
	
	@Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "unit_unique_id")
    private String unitUniqueId;

    @Column(name = "operation")
    private String operation;
    
    @Column(name = "remarks")
    private String remarks;
    
    @Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

}
