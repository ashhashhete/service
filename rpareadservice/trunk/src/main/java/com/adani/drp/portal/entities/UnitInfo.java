package com.adani.drp.portal.entities;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "unit_info_evw")
@Setter
@Getter
@RequiredArgsConstructor
public class UnitInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer objectid;

    @Column(name = "globalid")
    private String globalId;

    @Column(name = "rel_globalid")
    private String relGlobalId;

    @Column(name = "unit_id")
    private String unitId;

    @Column(name = "relative_path")
    private String relativePath;

    @Column(name = "tenement_number")
    private String tenementNumber;

    @Column(name = "hut_id")
    private String hutId;

    @Column(name = "floor")
    private String floor;

//    @Column(name = "unit_no")
//    private String unitNo;

    @Column(name = "unit_usage")
    private String unitUsage;

    @Column(name = "existence_since")
    private Date existenceSince;

//    @Column(name = "structure_since")
//    private Date structureSince;

    @Column(name = "structure_year")
    private String structureYear;

//    @Column(name = "nature_of_activity")
//    private String natureOfActivity;

//    @Column(name = "residential_area")
//    private BigDecimal residentialArea;

//    @Column(name = "commercial_area")
//    private BigDecimal commercialArea;

//    @Column(name = "rc_residential_area")
//    private BigDecimal rcResidentialArea;

//    @Column(name = "rc_commercial_area")
//    private BigDecimal rcCommercialArea;

//    @Column(name = "other_area")
//    private BigDecimal otherArea;

//    @Column(name = "area_sq_ft")
//    private BigDecimal areaSqFt;

//    @Column(name = "electric_bill", nullable = true)
//    private Short electricBill;

//    @Column(name = "ownership_proof", nullable = true)
//    private Short ownershipProof;

//    @Column(name = "property_tax", nullable = true)
//    private Short propertyTax;

//    @Column(name = "rent_agreement", nullable = true)
//    private Short rentAgreement;

//    @Column(name = "gumasta", nullable = true)
//    private Short gumasta;

//    @Column(name = "chain_document", nullable = true)
//    private Short chainDocument;

//    @Column(name = "financial_documents", nullable = true)
//    private Short financialDocuments;

//    @Column(name = "other_license", nullable = true)
//    private Short otherLicense;

//    @Column(name = "na_tax", nullable = true)
//    private Short naTax;

    @Column(name = "unit_status")
    private String unitStatus;

    @Column(name = "surveyor_name")
    private String surveyorName;

    @Column(name = "remarks")
    private String remarks;
    
    @Column(name = "remarks_other")
    private String remarksOther;

    @Column(name = "created_user")
    private String createdUser;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "last_edited_user")
    private String lastEditedUser;

    @Column(name = "last_edited_date")
    private Date lastEditedDate;


    @Column(name = "media_captured_cnt")
    private Integer mediaCapturedCnt;

    @Column(name = "media_uploaded_cnt")
    private Integer mediaUploadedCnt;

    @Column(name = "respondent_available")
    private String respondentAvailable;

//    @Column(name = "electoral_roll", nullable = true)
//    private Short electoralRoll;

//    @Column(name = "photo_pass", nullable = true)
//    private Short photoPass;

//    @Column(name = "share_certificate", nullable = true)
//    private Short shareCertificate;

//    @Column(name = "school_college_certificate", nullable = true)
//    private Short schoolCollegeCertificate;

//    @Column(name = "employer_certificate", nullable = true)
//    private Short employerCertificate;

//    @Column(name = "restaurant_hotel_license", nullable = true)
//    private Short restaurantHotelLicense;

//    @Column(name = "factory_act_license", nullable = true)
//    private Short factoryActLicense;

//    @Column(name = "food_drug_license", nullable = true)
//    private Short foodDrugLicense;

//    @Column(name = "gdb_archive_oid")
//    private Integer gdbArchiveOid;
//
//    @Column(name = "gdb_from_date")
//    private Date gdbFromDate;
//
//    @Column(name = "gdb_to_date")
//    private Date gdbToDate;

    @Column(name = "surveyor_desig")
    private String surveyorDesig;

    @Column(name = "survey_date")
    private Date surveyDate;

//    @Column(name = "survey_time")
//    private Date surveyTime;

    @Column(name = "drp_officer_name")
    private String drpOfficerName;

    @Column(name = "drp_officer_desig")
    private String drpOfficerDesig;

    @Column(name = "unit_unique_id")
    private String unitUniqueId;

    @Column(name = "visit_count", nullable = true)
    private Short visitCount;

    @Column(name = "ward_no", nullable = true)
    private String wardNo;

    @Column(name = "zone_no")
    private String zoneNo;

    @Column(name = "nagar_name")
    private String nagarName;
    
    @Column(name = "nagar_name_other")
    private String nagarNameOther;

    
    @Column(name = "society_name")
    private String societyName;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "landmark_name")
    private String landmarkName;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "respondent_non_available_remark")
    private String respondentNonAvailableRemark;

    @Column(name = "respondent_name")
    private String respondentName;

    @Column(name = "respondent_mobile")
    private String respondentMobile;

    @Column(name = "respondent_dob")
    private String respondentDob;

    @Column(name = "respondent_age")
    private String respondentAge;

    @Column(name = "respondent_hoh_name")
    private String respondentHohName;

    @Column(name = "respondent_hoh_contact")
    private String respondentHohContact;

    @Column(name = "respondent_hoh_relationship")
    private String respondentHohRelationship;
    
    @Column(name = "respondent_hoh_rel_other")
    private String respondentHohRelOther;

    @Column(name = "respondent_remarks")
    private String respondentRemarks;

    @Column(name = "tenement_document")
    private String tenementDocument;
    
    @Column(name = "tenement_document_other")
    private String tenementDocumentOther;

    @Column(name = "mashal_survey_number")
    private String mashalSurveyNumber;

    @Column(name = "ownership_status")
    private String ownershipStatus;

    @Column(name = "unit_area_sqft")
    private BigDecimal unitAreaSqft;

    @Column(name = "ghumasta_area_sqft")
    private BigDecimal ghumastaAreaSqft;

    @Column(name = "loft_present")
    private String loftPresent;

    @Column(name = "loft_area_sqft")
    private BigDecimal loftAreaSqft;

    @Column(name = "employees_count")
    private Short employeesCount;

    @Column(name = "survey_end_date")
    private String surveyEndDate;

    @Column(name = "surveyors_remarks")
    private String surveyorsRemarks;

    @Column(name = "gen_qc_by")
    private String genQcBy;

    @Column(name = "gen_qc_user")
    private String genQcUser;

    @Column(name = "gen_qc_status")
    private String genQcStatus;

    @Column(name = "gen_qc_end_date")
    private Date genQcEndDate;

    @Column(name = "gen_qc_remarks")
    private String genQcRemarks;

    @Column(name = "client_qc_by")
    private String clientQcBy;

    @Column(name = "client_qc_user")
    private String clientQcUser;

    @Column(name = "client_qc_status")
    private String clientQcStatus;

    @Column(name = "client_qc_end_date")
    private Date clientQcEndDate;

    @Column(name = "client_qc_remarks")
    private String clientQcRemarks;

    @Column(name = "drp_verification_status")
    private String drpVerificationStatus;

    @Column(name = "drp_verification_remarks")
    private String drpVerificationRemarks;

    @Column(name = "mobile_no_for_otp")
    private String mobileNoForOtp;

    @Column(name = "otp_received")
    private Integer otpReceived;

    @Column(name = "otp_remarks")
    private String otpRemarks;
    
    @Column(name = "otp_remarks_other")
    private String otpRemarksOther;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "existence_since_year", nullable = true)
    private Short existenceSinceYear;

//    @Column(name = "residential_proof_count", nullable = true)
//    private Short residentialProofCount;

    @Column(name = "owner_photo_with_uniqueid", nullable = true)
    private Short ownerPhotoWithUniqueId;

//    @Column(name = "unit_qc_by")
//    private String unitQcBy;

//    @Column(name = "unit_qc_user")
//    private String unitQcUser;

//    @Column(name = "unit_qc_date")
//    private Date unitQcDate;

//    @Column(name = "unit_qc_status")
//    private String unitQcStatus;

//    @Column(name = "unit_qc_end_date")
//    private Date unitQcEndDate;

//    @Column(name = "unit_qc_remarks")
//    private String unitQcRemarks;

    @Column(name = "gen_qc_date")
    private Date genQcDate;

    @Column(name = "client_qc_date")
    private Date clientQcDate;

    @Column(name = "sector_no")
    private String sectorNo;

//    @Column(name = "owner_employee_name")
//    private String ownerEmployeeName;

//    @Column(name = "residential_proof_1", nullable = true)
//    private Short residentialProof1;
//
//    @Column(name = "residential_proof_2", nullable = true)
//    private Short residentialProof2;
//
//    @Column(name = "residential_proof_3", nullable = true)
//    private Short residentialProof3;
//
//    @Column(name = "residential_proof_4", nullable = true)
//    private Short residentialProof4;
//
//    @Column(name = "residential_proof_5", nullable = true)
//    private Short residentialProof5;
//
//    @Column(name = "commercial_proof_1", nullable = true)
//    private Short commercialProof1;

    @Column(name = "form_lock", nullable = true)
    private Short formLock;

    @Column(name = "visit_date")
    private Date visitDate;

//    @Column(name = "hut_u_id")
//    private String hutUId;

//    @Column(name = "row_num")
//    private Long rowNum;

//    @Column(name = "unit_area_sqft")
//    private BigDecimal unitAreaSqft;

    @Column(name = "panchnama_form_remarks")
    private String panchnamaFormRemarks;

    @Column(name = "survey_pavti_no")
    private String surveyPavtiNo;

    @Column(name = "access_to_unit")
    private String accessToUnit;

    @Column(name = "thumb_remarks")
    private String thumbRemarks;
    
    @Column(name = "residential_area_sqft")
    private BigDecimal residentialAreaSqft;
    
    @Column(name = "commercial_area_sqft")
    private BigDecimal commercialAreaSqft;
    
    @Column(name = "type_of_other_structure")
    private String typeOfOtherStructure;

    @Column(name = "drp_officer_name_other")
    private String drpOfficerNameOther;

    @Column(name = "drp_officer_desig_other")
    private String drpOfficerDesigOther;

    @Column(name = "is_booth_verified")
    private Short isBoothVerified;
    
  @Column(name = "structure_type_religious")
  private String structureTypeReligious;
  
  @Column(name = "structure_religious_other")
  private String structureTypeReligiousOther;

  @Column(name = "structure_type_amenities")
  private String structureTypeAmenities;
  
  @Column(name = "structure_amenities_other")
  private String structureTypeAmenitiesOther;

  @Column(name = "name_of_structure")
  private String nameOfStructure;
  
  @Column(name = "type_of_diety")
  private String typeOfDiety;

  @Column(name = "type_of_diety_other")
  private String typeOfDietyOther;

  @Column(name = "name_of_diety")
  private String nameOfDiety;
  
  @Column(name = "category_of_faith")
  private String categoryOfFaith;

  @Column(name = "category_of_faith_other")
  private String categoryOfFaithOther;

  @Column(name = "sub_category_of_faith")
  private String subCategoryOfFaith;
  
  @Column(name = "religion_of_structure_belongs")
  private String religionOfStructureBelongs;

  @Column(name = "structure_ownership_status")
  private String structureOwnershipStatus;

  @Column(name = "name_of_trust_or_owner")
  private String nameOfTrustOrOwner;
  
  @Column(name = "nature_of_structure")
  private String natureOfStructure;

  @Column(name = "construction_material")
  private String constructionMaterial;
  
  @Column(name = "daily_visited_people_count")
  private Integer dailyVisitedPeopleCount;
  
  @Column(name = "is_structure_registered")
  private Short isStructureRegistered;
  
  @Column(name = "structure_registered_with")
  private String structureRegisteredWith;

  @Column(name = "other_religious_authority")
  private String otherReligiousAuthority;

  @Column(name = "name_of_trustee")
  private String nameOfTrustee;
  
  @Column(name = "name_of_landowner")
  private String nameOfLandowner;
  
  @Column(name = "noc_from_landlord_or_govt")
  private Short nocFromLandlordOrGovt;
  
  @Column(name = "approval_from_govt")
  private Short approvalFromGovt;
  
  @Column(name = "yearly_festival_conducted")
  private Short festivalConducted;
  
  @Column(name = "ra_total_no_of_floors")
  private Short raTotalFloors;




}

