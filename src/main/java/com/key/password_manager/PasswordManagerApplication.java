package com.key.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.key.password_manager.authentication.AuthenticationService;
import com.key.password_manager.credential.CredentialRepository;
import com.key.password_manager.credential.CredentialService;
import com.key.password_manager.encryption.Lock;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.keypair.PrivatePublicKeyPair;
import com.key.password_manager.otpverification.Otp;
import com.key.password_manager.otpverification.OtpFactory;
import com.key.password_manager.otpverification.OtpRepository;
import com.key.password_manager.user.UserService;

@SpringBootApplication
public class PasswordManagerApplication implements CommandLineRunner {

	@Autowired
	private RSAKeyPairStore rsaKeyPairStore;

	@Autowired
	private Lock lock;

	@Autowired
	private CredentialService credentialService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserService userService;

	@Autowired
	private CredentialRepository credentialRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private OtpFactory otpFactory;

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		PrivatePublicKeyPair keyPair = rsaKeyPairStore.getRSAKeyPair(1L);
		String lockedPassword = lock.lock(keyPair.getPublicKey(), "Malay@123");
		System.out.println(
				"\nLocked password for user 1 is : " + lockedPassword.replace("+", "%2B") + "\n");

		// ------------ CRED LOCK ON REGISTER TEST ----------
		// PrivatePublicKeyPair keyPair = rsaKeyPairStore.getRSAKeyPair(1L);
		// String lockedPassword = lock.lock(keyPair.getPublicKey(), "Malay@123");
		// Credential credential = new Credential();
		// credential.setPassword("password");
		// // credential.setId(67L);
		// String status = credentialService.addCredential(credential, 1L, lockedPassword);
		// System.out.println("\n\n ----> " + status + "\n\n");

		// ------------- RETRIVAL ------------------
		// User user = userService.getUser(11L);
		// user.getPassword().setKey("Malay@123");
		// String lockedCred =
		// credentialService.retriveCredential(11L, 18L, lockedPassword).getPassword();
		// System.out.println(lock.unlock(user.getPassword(), lockedCred));
	}
}


