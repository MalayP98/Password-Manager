package com.key.password_manager.authentication;

import java.security.KeyException;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.key.password_manager.GlobalConstants;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.keypair.KeyPairFactory;
import com.key.password_manager.keypair.PasswordEncryptionKeyPair;
import com.key.password_manager.otpverification.OtpService;
import com.key.password_manager.security.JWTGenerator;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@Service
public class AuthenticationService {

	@Autowired
	private UserService userService;

	@Autowired
	private JWTGenerator jwtGenerator;

	@Autowired
	private KeyPairFactory keyPairFactory;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OtpService otpService;

	public String registerUser(AuthenticationModel registrationData) throws Exception {
		if (!isPasswordStrong(registrationData.getPassword()))
			return "Weak Password!";
		User user = userService.getUserWithRegistrationStatus(registrationData.getEmail(), true);
		if (Objects.isNull(user)) {
			user = userService.registerUser(new User(registrationData.getEmail(),
					getKeyPair(registrationData.getPassword()), false, true));
		}
		return user.isEnabled() ? "User alreay registered."
				: otpService.sentOTP(registrationData.getEmail());
	}

	public String loginUser(AuthenticationModel authModel) throws Exception {
		if (authenticate(authModel)) {
			return jwtGenerator.generate(authModel.getEmail());
		}
		throw new UsernameNotFoundException(
				"No user by email " + authModel.getEmail() + " exists.");
	}

	private Boolean authenticate(AuthenticationModel authModel) {
		UserDetails userDetails = userService.loadUserByUsername(authModel.getEmail());
		return passwordEncoder.matches(authModel.getPassword(), userDetails.getPassword());
	}

	private PasswordEncryptionKeyPair getKeyPair(String password)
			throws EncryptionException, KeyException {
		PasswordEncryptionKeyPair passwordEncryptionKeyPair =
				keyPairFactory.createPasswordEncryptionKeyPair(password, null);
		passwordEncryptionKeyPair.getPassword().setKey(passwordEncoder.encode(password));
		return passwordEncryptionKeyPair;
	}

	private Boolean isPasswordStrong(String password) {
		return (Objects.nonNull(password) && Pattern.compile(GlobalConstants.STRONG_PASSWORD_REGEX)
				.matcher(password).matches());
	}
}
