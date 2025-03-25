package com.adani.drp.portal.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "external_attachments_status")
@Setter
@Getter
@RequiredArgsConstructor
public class ExternalAttachmentStatus {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long objectid;

        @Column(name = "is_uploaded")
        private Boolean isUploaded;

        @Column(name = "rel_globalid")
        private String relGlobalId;

        @Column(name = "created_by")
        private Integer createdBy;

        @Column(name = "created_date")
        private Timestamp createdDate;

        @Column(name = "edited_by")
        private Integer editedBy;

        @Column(name = "edited_date")
        private Timestamp editedDate;

        @Column(name = "unit_unique_id")
        private String unitUniqueId;

    }
