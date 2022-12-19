package com.key.password_manager.otpverification;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.key.password_manager.stringgenerators.otpgenerator.OtpGenerator;
import com.key.password_manager.stringgenerators.passwordgenerators.PasswordGenerator;

@Component
public class OtpFactory {

	/**
	 * Current PasswordGenerator is being used to create OTP. Any other implementation can be used
	 * later by implementing PassworGenerator interface.
	 */
	@Autowired
	@Qualifier("defaultOtpGenerator")
	private OtpGenerator otpGenerator;

	@Value("${com.keys.otp.length}")
	private int OTP_LENGTH;

	@Value("${com.keys.otp.refid.length}")
	private int OTP_REF_ID_LENGTH;

	@Value("${com.keys.otp.expiry}")
	private int EXPIRY_TIME;

	public Otp createOtp(String recipientEmail) {
		Otp otp = new Otp(otpGenerator.generate(OTP_LENGTH));
		otp.setUserEmail(recipientEmail);
		Date currentDate = new Date();
		otp.setCreationDate(currentDate);
		otp.setExpiryDate(Date.from(currentDate.toInstant().plusSeconds(60 * EXPIRY_TIME)));
		return otp;
	}

	public Otp refresh(Otp otp) {
		Otp refreshedOtp = createOtp(otp.getUserEmail());
		refreshedOtp.setId(otp.getId());
		return refreshedOtp;
	}
}
