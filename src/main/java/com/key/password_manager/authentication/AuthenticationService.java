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

	/**
	 * Checks if user has a strong password or not. If the password is weak user is not registered.
	 * If the password is strong, we check if the user is already registered or not(if user is
	 * sending registeration request more that once they will not be saved again). If not then a new
	 * user is created and `isRegistered` field {@link User} is made true and new user is returned.
	 * Now we check if the retrived or created user are enabled or not. If user is enabled then send
	 * a message indicating the user is already registered else we send user an OTP for verifcation.
	 */
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

	/**
	 * Authenticate the user using {@link #authenticate(AuthenticationModel)} method. If successful
	 * then a JWT is created with user's email and returned. Else a message is returned indicating
	 * that user with given email does not exist.
	 */
	public String loginUser(AuthenticationModel authModel) throws Exception {
		if (authenticate(authModel)) {
			return jwtGenerator.generate(authModel.getEmail());
		}
		throw new UsernameNotFoundException(
				"No user by email " + authModel.getEmail() + " exists.");
	}

	/**
	 * @param authModel : Loads the user from database with the email present this param. If user
	 *                  fetched is NULL then false is returned else user password is matched against
	 *                  the password present in @param authMode. @return, if user is authenticated
	 *                  or not.
	 */
	private Boolean authenticate(AuthenticationModel authModel) {
		UserDetails userDetails = userService.loadUserByUsername(authModel.getEmail());
		if (Objects.isNull(userDetails)) {
			return false;
		}
		return passwordEncoder.matches(authModel.getPassword(), userDetails.getPassword());
	}

	/**
	 * @param password : generate a {@link KeyPair} using {@link KeyPairFactory} with this
	 *                 parameter. After generation of a key pair the password stored in this key
	 *                 pair is encoded with password encoder as this is being saved in the database.
	 * 
	 * @return : generated key pair which is used for credential lock and unlock.
	 * @throws EncryptionException
	 * @throws KeyException
	 */
	private PasswordEncryptionKeyPair getKeyPair(String password)
			throws EncryptionException, KeyException {
		PasswordEncryptionKeyPair passwordEncryptionKeyPair =
				keyPairFactory.createPasswordEncryptionKeyPairWithRandomEncryptionKey(password);
		passwordEncryptionKeyPair.getPassword().setKey(passwordEncoder.encode(password));
		return passwordEncryptionKeyPair;
	}

	/**
	 * Checks if password is strong or not by matching it against a strong password matcher defined
	 * in {@link GlobalConstants}
	 */
	private Boolean isPasswordStrong(String password) {
		return (Objects.nonNull(password) && Pattern.compile(GlobalConstants.STRONG_PASSWORD_REGEX)
				.matcher(password).matches());
	}
}
