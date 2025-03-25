package com.adani.drp.portal.entities.internalAttachments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "media_details_evw")
@Setter
@Getter
@RequiredArgsConstructor
public class MediaDetailsEVW implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objectid")
    private int objectid;

    @Column(name = "globalid")
    private String globalid;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_order")
    private Integer fileOrder;

    @Column(name = "data_size")
    private int dataSize;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "attachment_id")
    private Integer attachmentId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "edited_by")
    private String editedBy;

    @Column(name = "edited_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editedDate;

    @Column(name = "rel_globalid")
    private String relGlobalid;

    @Column(name = "created_user")
    private String createdUser;

    @Column(name = "last_edited_user")
    private String lastEditedUser;

    @Column(name = "last_edited_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEditedDate;
}
