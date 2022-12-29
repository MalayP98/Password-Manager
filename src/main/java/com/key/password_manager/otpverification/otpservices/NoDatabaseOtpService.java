package com.key.password_manager.otpverification.otpservices;

import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.key.password_manager.email.Email;
import com.key.password_manager.email.EmailService;
import com.key.password_manager.key.KeyFactory;
import com.key.password_manager.locks.Lock;
import com.key.password_manager.otpverification.Otp;
import com.key.password_manager.otpverification.OtpFactory;
import com.key.password_manager.user.UserService;

@Service("nodatabaseOTPService")
public class NoDatabaseOtpService implements OtpService {

	private final String OTP_MESSAGE =
			"<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\"><div style=\"margin:50px auto;width:70vw;padding:20px 0\"><div style=\"border-bottom:1px solid #eee\"><a href=\"\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">Keys</a></div> <p style=\"font-size:1.1em\">Hi,</p> <p>Thank you for choosing Keys. Use the following OTP to complete your Sign Up procedures. OTP is valid for %d minutes</p> <h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\"> %s </h2> <p style=\"font-size:0.9em;\">Regards,<br />Keys</p> <hr style=\"border:none;border-top:1px solid #eee\" /> <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\"> <p>Keys Inc</p> <p>1600 Amphitheatre Parkway</p> <p>California</p> </div> </div> </div>";

	private final String SUBJECT = "Keys-OTP";

	@Value("${com.keys.otp.expiry}")
	private int EXPIRY_TIME;

	@Autowired
	private OtpFactory otpFactory;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

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
			emailService.sendHTMLMail(new Email(recipientEmail, SUBJECT,
					String.format(OTP_MESSAGE, EXPIRY_TIME, otp.getOtp())));
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
