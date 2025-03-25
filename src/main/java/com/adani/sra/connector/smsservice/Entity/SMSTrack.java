package com.adani.sra.connector.smsservice.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sms_track")
@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class SMSTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objectid")
    private Long objectId;

    @Column(name = "sms_source")
    private String smsSource;

    @Column(name = "send_to")
    private String sendTo;

    @Column(name = "sms_content")
    private String smsContent;

    private boolean sent;

    @Column(name = "sent_date")
    private Date sentDate;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "response_content")
    private String responseContent;

    @Column(name = "unit_unique_id")
    private String unitUniqueId;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "created_date")
    private Date createdDate;


}
