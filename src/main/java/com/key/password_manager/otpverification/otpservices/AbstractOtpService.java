package com.key.password_manager.otpverification.otpservices;

import org.springframework.beans.factory.annotation.Autowired;
import com.key.password_manager.email.EmailService;
import com.key.password_manager.otpverification.Otp;
import com.key.password_manager.otpverification.OtpFactory;
import com.key.password_manager.otpverification.OtpProperties;
import com.key.password_manager.otpverification.OtpRepository;
import com.key.password_manager.user.UserService;

public abstract class AbstractOtpService implements OtpService {

	@Autowired
	protected OtpProperties otpProperties;

	@Autowired
	protected OtpFactory otpFactory;

	@Autowired
	protected EmailService emailService;

	@Autowired
	protected OtpRepository otpRepository;

	@Autowired
	protected UserService userService;

	@Override
	public abstract Otp sendOTP(String recipientEmail);

	@Override
	public abstract boolean verifyOTP(Otp otp);
}
