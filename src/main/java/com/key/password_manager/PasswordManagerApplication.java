package com.key.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.key.password_manager.authentication.AuthenticationModel;
import com.key.password_manager.authentication.AuthenticationService;
import com.key.password_manager.credential.Credential;
import com.key.password_manager.credential.CredentialService;
import com.key.password_manager.encryption.Lock;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.keypair.PrivatePublicKeyPair;

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

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// AuthenticationModel am = new AuthenticationModel();
		// am.setEmail("malay123@gmail.com");
		// am.setPassword("Malay@123");

		// authenticationService.registerUser(am);


		// ------------ CRED LOCK ON REGISTER TEST ----------
		// PrivatePublicKeyPair keyPair = rsaKeyPairStore.getRSAKeyPair(11L);
		// String lockedPassword = lock.lock(keyPair.getPublicKey(), "Malay@123");
		// Credential credential = new Credential();
		// credential.setPassword("password");
		// credential.setId(67L);
		// String status = credentialService.addCredential(credential, 11L, lockedPassword);
		// System.out.println("\n\n ----> " + status + "\n\n");


		// -------- TEST FOR PASSWORD ENCRYPTION DECRYPTION --------
		// PrivatePublicKeyPair ppkp = rsaKeyPairStore.getRSAKeyPair(1L);
		// String locked = rsaKeyService.lock(ppkp.getPublicKey(), "data");
		// String unlocked = rsaKeyService.unlock(ppkp.getPrivateKey(), locked);
		// System.out.println("\n\n " + ppkp + "\n\n");
		// System.out.println("\n\n ---------------");
		// System.out.println("locked => " + locked);
		// System.out.println("unlocked => " + unlocked);
		// System.out.println("\n\n --------------- \n\n");
	}
}


