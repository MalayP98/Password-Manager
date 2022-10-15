package com.key.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.key.password_manager.authentication.AuthenticationService;
import com.key.password_manager.credential.Credential;
import com.key.password_manager.credential.CredentialRepository;
import com.key.password_manager.credential.CredentialService;
import com.key.password_manager.encryption.Lock;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.keypair.PrivatePublicKeyPair;
import com.key.password_manager.user.User;
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

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// ------------ CRED LOCK ON REGISTER TEST ----------
		// PrivatePublicKeyPair keyPair = rsaKeyPairStore.getRSAKeyPair(11L);
		// String lockedPassword = lock.lock(keyPair.getPublicKey(), "Malay@123");
		// Credential credential = new Credential();
		// credential.setPassword("password");
		// credential.setId(67L);
		// String status = credentialService.addCredential(credential, 11L, lockedPassword);
		// System.out.println("\n\n ----> " + status + "\n\n");

		// ------------- RETRIVAL ------------------
		// User user = userService.getUser(11L);
		// user.getPassword().setKey("Malay@123");
		// String lockedCred =
		// credentialService.retriveCredential(11L, 18L, lockedPassword).getPassword();
		// System.out.println(lock.unlock(user.getPassword(), lockedCred));
	}
}


