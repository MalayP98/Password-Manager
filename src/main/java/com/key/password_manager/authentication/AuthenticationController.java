package com.key.password_manager.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.key.password_manager.otpverification.Otp;
import com.key.password_manager.otpverification.otpservices.DatabaseOtpService;
import com.key.password_manager.utils.GenericReponse;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private DatabaseOtpService otpService;

	@PostMapping("/login")
	public ResponseEntity<GenericReponse> login(@RequestBody AuthenticationModel authenticationData)
			throws Exception {
		return new ResponseEntity<GenericReponse>(
				new GenericReponse().setJwt(authenticationService.loginUser(authenticationData)),
				HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<GenericReponse> register(
			@RequestBody AuthenticationModel registrationData) throws Exception {
		return new ResponseEntity<GenericReponse>(
				authenticationService.registerUser(registrationData), HttpStatus.CREATED);
	}

	@PostMapping("/verify" + "/otp")
	public ResponseEntity<String> verifyOTP(@RequestBody Otp otp) {
		return new ResponseEntity<String>(otpService.verifyOTP(otp) ? "User successfully validated."
				: "Unable to verfiy user. Try again!", HttpStatus.OK);
	}
}
