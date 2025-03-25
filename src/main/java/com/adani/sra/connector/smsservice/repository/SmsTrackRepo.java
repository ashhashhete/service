package com.adani.sra.connector.smsservice.repository;

import com.adani.sra.connector.smsservice.Entity.SMSTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SmsTrackRepo extends JpaRepository<SMSTrack, Integer> {
    SMSTrack findByTransactionId(@Param("transaction_id") String transactionId);

//    @Transactional
//    @Modifying
//    @Query("UPDATE SMSTrack s SET s.receivedOtp = :receivedOtp, s.isOTPVerified = :isOTPVerified , s.attemptNo = :attemptNo+1 WHERE s.transactionId = :transactionId")
//    int updateOtpAndVerificationStatusByTransactionId(String receivedOtp, boolean isOTPVerified, String transactionId ,int attemptNo);

    boolean existsByTransactionId(String transactionId);
}
