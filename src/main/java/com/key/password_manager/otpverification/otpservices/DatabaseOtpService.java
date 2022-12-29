package com.key.password_manager.otpverification.otpservices;

import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.key.password_manager.email.Email;
import com.key.password_manager.otpverification.Otp;
import com.key.password_manager.otpverification.OtpConstants;
import com.key.password_manager.otpverification.OtpRepository;

@Service("databaseOTPService")
public class DatabaseOtpService extends AbstractOtpService {

	@Autowired
	private OtpRepository otpRepository;

	@Override
	public Otp sendOTP(String recipientEmail) {
		try {
			Otp otp = otpRepository.findByUserEmail(recipientEmail);
			if (Objects.isNull(otp))
				otp = otpFactory.createOtp(recipientEmail);
			else if (new Date().compareTo(otp.getExpiryDate()) > 0)
				otp = otpFactory.refresh(otp);
			else
				return otp;
			emailService.sendHTMLMail(new Email(recipientEmail, OtpConstants.SUBJECT,
					String.format(OtpConstants.OTP_MESSAGE, EXPIRY_TIME, otp.getOtp())));
			return otpRepository.save(otp);
		} catch (Exception e) {
			e.printStackTrace();
			// add log
		}
		return otpFactory.createEmptyOtp(recipientEmail);
	}

	@Override
	public boolean verifyOTP(Otp otp) {
		otp = otpRepository.findByOtp(otp.getOtp());
		if (Objects.isNull(otp) || new Date().compareTo(otp.getExpiryDate()) > 0) {
			return false;
		} else {
			if (userService.enableUserWithEmail(otp.getUserEmail())) {
				otpRepository.delete(otp);
				return true;
			}
		}
		return false;
	}
}
