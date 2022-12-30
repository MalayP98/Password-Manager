package com.key.password_manager.otpverification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * These properties might be used in diffrent places in the project, so creating a seprate class to
 * get the value of the properties.
 */
@Component
public class OtpProperties {

	@Value("${com.keys.otp.length}")
	private int OTP_LENGTH;

	@Value("${com.keys.otp.expiry}")
	private int EXPIRY_TIME;

	public int otpLength() {
		return OTP_LENGTH;
	}

	public int timeToLive() {
		return EXPIRY_TIME;
	}
}
