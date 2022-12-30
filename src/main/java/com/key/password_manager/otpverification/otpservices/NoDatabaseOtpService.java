package com.key.password_manager.otpverification.otpservices;

import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.key.password_manager.email.Email;
import com.key.password_manager.key.AESKeyBuilder;
import com.key.password_manager.key.KeyProperties;
import com.key.password_manager.locks.Lock;
import com.key.password_manager.otpverification.Otp;
import com.key.password_manager.otpverification.OtpConstants;

@Service("nodatabaseOTPService")
public class NoDatabaseOtpService extends AbstractOtpService {

	@Autowired
	private KeyProperties keyProperties;

	@Autowired
	private Lock lock;

	@Override
	public Otp sendOTP(String recipientEmail) {
		try {
			Otp otp = otpFactory.createOtpWithEncryptedEmail(recipientEmail);
			emailService.sendHTMLMail(new Email(recipientEmail, OtpConstants.SUBJECT, String
					.format(OtpConstants.OTP_MESSAGE, otpProperties.timeToLive(), otp.getOtp())));
			return otp;
		} catch (Exception e) {
			e.printStackTrace();
			// add log
		}
		return otpFactory.createEmptyOtp(recipientEmail);
	}

	@Override
	public boolean verifyOTP(Otp otp) {
		if (Objects.isNull(otp) || new Date().compareTo(otp.getExpiryDate()) > 0) {
			return false;
		}
		try {
			return userService.enableUserWithEmail(lock
					.unlock(new AESKeyBuilder().setKey(otp.getOtp()).setSalt(keyProperties.salt())
							.setIV(keyProperties.iv()).build(), otp.getUserEmail()));
		} catch (Exception e) {
			e.printStackTrace();
			// LOG
		}
		return false;
	}
}
