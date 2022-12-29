package com.key.password_manager.otpverification.otpservices;

import com.key.password_manager.otpverification.Otp;

public interface OtpService {

	public Otp sendOTP(String recipientEmail);

	public boolean verifyOTP(Otp otp);
}
