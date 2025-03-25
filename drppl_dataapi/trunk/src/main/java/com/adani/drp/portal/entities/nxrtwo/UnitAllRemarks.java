package com.adani.drp.portal.entities.nxrtwo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "unit_all_remarks_vw")
@Setter
@Getter
@RequiredArgsConstructor
public class UnitAllRemarks {
	
	@Id
	@Column(name = "unit_unique_id")
    private String unitUniqueId;
	
	@Column(name = "survey_date")
	private Date surveyDate;
	
	@Column(name = "last_edited_date")
	private Date lastEditedDate;
	
	@Column(name = "imupload_status")
    private String imUploadStatus;
	
	@Column(name = "drppl_evaluation_team1")
    private String drpplEvaluationTeam1;
	
	@Column(name = "drppl_evaluation_team2")
    private String drpplEvaluationTeam2;
	
	@Column(name = "drp_evaluation_team1")
    private String drpEvaluationTeam1;

}
