package com.adani.sra.connector.smsservice.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "otp_track")
@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class OTPTrack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objectid") //,columnDefinition = "NUMERIC(19,0)")
    private Integer objectId;

    @Column(name ="otp_sent")
    private String otpSent;

    @Column(name ="otp_received")
    private String otpReceived;

    @Column(name ="is_success")
    private boolean isSuccess;

    @Column(name ="otpDate")
    private Date otpDate;

    @Column(name = "attempt_no")
    private int attemptNo;

    @Column(name = "sms_track_id")
    private Long smsTrackId;

    @ManyToOne
    @JoinColumn(name = "sms_track_id",
            referencedColumnName = "objectid", insertable = false, updatable = false)
    private SMSTrack smsTrack;
}
