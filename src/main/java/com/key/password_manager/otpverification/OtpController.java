package com.key.password_manager.otpverification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.key.password_manager.otpverification.otpservices.OtpService;


@RestController
@RequestMapping("/public" + "/otp")
public class OtpController {

	@Autowired
	@Qualifier("nodatabaseOTPService")
	private OtpService otpService;

	@GetMapping("/generate")
	public ResponseEntity<Otp> generateOTP(@RequestParam String recipientEmail) {
		return new ResponseEntity<Otp>(otpService.sendOTP(recipientEmail), HttpStatus.OK);
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyOTP(@RequestBody Otp otp) {
		return new ResponseEntity<String>(otpService.verifyOTP(otp) ? "OTP verified. User enabled."
				: "Unable to verify user. Try again.", HttpStatus.OK);
	}
}
