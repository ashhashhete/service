package com.adani.sra.connector.smsservice.service;

import com.adani.sra.connector.smsservice.Entity.SMSTrack;
import com.adani.sra.connector.smsservice.repository.SmsTrackRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsInfoService {

    private final SmsTrackRepo smsTrackRepo;

    // Create
    public SMSTrack saveSMSInfo(SMSTrack smsTrack) {
        return smsTrackRepo.save(smsTrack);
    }

//    public boolean updateOtpAndVerificationStatus(String receivedOtp, boolean isOTPVerified, String transactionId ,int attemptNo) {
//        if (smsTrackRepo.existsByTransactionId(transactionId)) {
//            return smsTrackRepo.updateOtpAndVerificationStatusByTransactionId(receivedOtp, isOTPVerified, transactionId,attemptNo) > 0;
//        }
//        return false;
//    }
    // Read
    public List<SMSTrack> getAllSMSInfo() {
        return smsTrackRepo.findAll();
    }

    public Optional<SMSTrack> getSMSInfoById(int objectId) {
        return smsTrackRepo.findById(objectId);
    }

    public SMSTrack getSMSInfoByTransactionId(String transactionId) {
        return smsTrackRepo.findByTransactionId(transactionId);
    }

    // Update
    public SMSTrack updateSMSInfo(SMSTrack smsTrack) {
        // Assuming the object already exists in the database
        return smsTrackRepo.save(smsTrack);
    }

    // Delete
    public void deleteSMSInfo(int objectId) {
        smsTrackRepo.deleteById(objectId);
    }
}
