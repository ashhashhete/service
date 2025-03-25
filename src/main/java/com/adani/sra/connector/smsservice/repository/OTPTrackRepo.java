package com.adani.sra.connector.smsservice.repository;

import com.adani.sra.connector.smsservice.Entity.OTPTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPTrackRepo extends JpaRepository<OTPTrack, Integer> {

//    OTPTrack findByTransactionId(@Param("transaction_id") String transactionId);

    OTPTrack findBySmsTrackId(Long objectId);
}