package com.adani.drp.portal.entities.nxrtwo;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.adani.drp.portal.entities.nxrtwo.AuditModel;

import java.util.Date;

@Entity
@Table(name = "rpa_validations")
@Data
@RequiredArgsConstructor

public class RpaValidations extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objectid")
    private Long objectid;

    @Column(name = "electoral_roll_year")
    private Integer electoralRollYear;

    @Column(name = "electoral_roll_part_no")
    private String electoralRollPartNo;

    @Column(name = "electoral_roll_sr_no")
    private String electoralRollSrNo;

    @Column(name = "electoral_roll_hut_no")
    private String electoralRollHutNo;

    @Column(name = "rpa_status")
    private String rpaStatus;

    @Column(name = "rpa_remark")
    private String rpaRemark;

    @Column(name = "rpa_date")
    private Date rpaDate;

    @Column(name = "is_valid_electoral")
    private Boolean isValidElectoral;

    @Column(name = "electoral_remark")
    private String electoralRemark;

    @Column(name = "electoral_date")
    private Date electoralDate;

    @Column(name = "is_valid_electricity_bill")
    private Boolean isValidElectricityBill;

    @Column(name = "electricity_bill_remark")
    private String electricityBillRemark;

    @Column(name = "electricity_bill_date")
    private Date electricityBillDate;

    @Column(name = "is_valid_survey_pavti")
    private Boolean isValidSurveyPavti;

    @Column(name = "survey_pavti_remark")
    private String surveyPavtiRemark;

    @Column(name = "survey_pavti_date")
    private Date surveyPavtiDate;

    @Column(name = "is_valid_gumasta")
    private Boolean isValidGumasta;

    @Column(name = "gumasta_remark")
    private String gumastaRemark;

    @Column(name = "gumasta_date")
    private Date gumastaDate;

    @Column(name = "unit_unique_id")
    private String unitUniqueId;
    
    @Column(name = "is_valid_hoh_name")
    private Boolean isValidHohName;
    
    @Column(name = "hoh_name_remark")
    private String hohNameRemark;
    
    @Column(name = "hoh_name_date")
    private Date hohNameDate;


}
