package com.key.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.key.password_manager.encryption.EncryptionStrategy;

@SpringBootApplication
public class PasswordManagerApplication implements CommandLineRunner {

	@Autowired
	@Qualifier("AES")
	private EncryptionStrategy aes;

	@Autowired
	@Qualifier("RSA")
	private EncryptionStrategy rsa;

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// KeyPair pair = RSAUtil.KeyPairGenerator();
		// Map<String, String> rsaDetail = new HashMap<>();
		// rsaDetail.put("publicKey", RSAUtil.convertKeyToString(pair.getPublic()));
		// rsaDetail.put("privateKey", RSAUtil.convertKeyToString(pair.getPrivate()));

		// System.out.println("\n\n " + rsaDetail + " \n\n");

		// String data = "Hello world";

		// System.out.println(" --------------------- ");
		// System.out.println("Data to be encrypted = " + data);
		// String enc = rsa.encrypt(rsaDetail, data);
		// String dec = rsa.decrypt(rsaDetail, enc);
		// System.out.println("Encrypted = " + enc);
		// System.out.println("Decrypted = " + dec);
		// System.out.println(" ----------------------- ");
	}
}
