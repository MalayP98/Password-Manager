package com.key.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.key.RSAKeyService;
import com.key.password_manager.key.keypair.PrivatePublicKeyPair;

@SpringBootApplication
public class PasswordManagerApplication implements CommandLineRunner {

	@Autowired
	private RSAKeyPairStore rsaKeyPairStore;

	@Autowired
	private RSAKeyService rsaKeyService;

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
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


