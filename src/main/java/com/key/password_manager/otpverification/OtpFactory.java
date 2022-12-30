package com.key.password_manager.otpverification;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.KeyFactory;
import com.key.password_manager.key.KeyProperties;
import com.key.password_manager.locks.Lock;
import com.key.password_manager.stringgenerators.RandomStringGenerator;

@Component
public class OtpFactory {

	@Autowired
	@Qualifier("otpGenerator")
	private RandomStringGenerator otpGenerator;

	@Autowired
	private Lock lock;

	@Autowired
	private OtpProperties otpProperties;

	@Autowired
	private KeyProperties keyProperties;

	// replace KeyFactory
	@Autowired
	private KeyFactory keyFactory;

	public Otp createOtp(String recipientEmail) {
		Otp otp = new Otp(otpGenerator.generate(otpProperties.otpLength()));
		otp.setUserEmail(recipientEmail);
		otp.setExpiryDate(
				Date.from(new Date().toInstant().plusSeconds(60 * otpProperties.timeToLive())));
		return otp;
	}

	public Otp createOtpWithEncryptedEmail(String recipientEmail) {
		Otp otp = new Otp(otpGenerator.generate(otpProperties.otpLength()));
		otp.setUserEmail(lock.lock(keyFactory.createAESKey(otp.getOtp(), keyProperties.salt(),
				keyProperties.iv(), null), recipientEmail));
		otp.setExpiryDate(
				Date.from(new Date().toInstant().plusSeconds(60 * otpProperties.timeToLive())));
		return otp;
	}

	public Otp refresh(Otp otp) {
		Otp refreshedOtp = createOtp(otp.getUserEmail());
		refreshedOtp.setId(otp.getId());
		return refreshedOtp;
	}

	public Otp createEmptyOtp(String recipientEmail) {
		Otp otp = new Otp("");
		otp.setUserEmail(recipientEmail);
		return otp;
	}
}
