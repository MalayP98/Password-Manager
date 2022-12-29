package com.key.password_manager.otpverification.otpservices;

import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.key.password_manager.email.Email;
import com.key.password_manager.key.KeyFactory;
import com.key.password_manager.locks.Lock;
import com.key.password_manager.otpverification.Otp;
import com.key.password_manager.otpverification.OtpConstants;

@Service("nodatabaseOTPService")
public class NoDatabaseOtpService extends AbstractOtpService {

	// delete when KeyFactory is replaced
	@Value("${com.keys.default.aeskey.salt}")
	private String salt;

	// delete when KeyFactory is replaced
	@Value("${com.keys.default.aeskey.iv}")
	private String iv;

	// replace KeyFactory
	@Autowired
	private KeyFactory keyFactory;

	@Autowired
	private Lock lock;

	@Override
	public Otp sendOTP(String recipientEmail) {
		try {
			Otp otp = otpFactory.createOtpWithEncryptedEmail(recipientEmail);
			emailService.sendHTMLMail(new Email(recipientEmail, OtpConstants.SUBJECT,
					String.format(OtpConstants.OTP_MESSAGE, EXPIRY_TIME, otp.getOtp())));
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
			return userService.enableUserWithEmail(lock.unlock(
					keyFactory.createAESKey(otp.getOtp(), salt, iv, null), otp.getUserEmail()));
		} catch (Exception e) {
			e.printStackTrace();
			// LOG
		}
		return false;
	}
}
