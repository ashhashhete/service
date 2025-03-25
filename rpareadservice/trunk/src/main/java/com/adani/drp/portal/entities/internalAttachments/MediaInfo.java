package com.adani.drp.portal.entities.internalAttachments;

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
@Table(name = "media_info_evw")
@Setter
@Getter
@RequiredArgsConstructor
public class MediaInfo {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objectid")
    private int objectId;

    @Column(name = "globalid")
    private String globalId;

    @Column(name = "rel_globalid")
    private String relGlobalId;

    @Column(name = "filename")
    private String filename;

    @Column(name = "last_edited_user")
    private String lastEditedUser;

    @Column(name = "last_edited_date")
    private Date lastEditedDate;

    @Column(name = "parent_table_name")
    private String parentTableName;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_category")
    private String documentCategory;

    @Column(name = "document_remarks")
    private String documentRemarks;

}
