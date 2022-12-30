package com.key.password_manager.key;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.key.password_manager.key.types.AESKeyType;
import com.key.password_manager.key.types.RSAKeyType;
import com.key.password_manager.stringgenerators.RandomStringGenerator;
import com.key.password_manager.utils.Helpers;

@Component
public class KeyFactory {

	@Autowired
	@Qualifier("passwordGenerator")
	private RandomStringGenerator passwordGenerator;

	@Autowired
	@Qualifier("simpleStringGenerator")
	private RandomStringGenerator simpleStringGenerator;

	@Autowired
	private KeyProperties keyProperties;

	private Logger LOG = LoggerFactory.getLogger(KeyFactory.class);

	public Key createPassword(String password) throws Exception {
		if (Objects.isNull(password)) {
			password = passwordGenerator.generate(keyProperties.keyLength());
		}
		return new AESKeyBuilder().setKey(password).setSalt(simpleStringGenerator.generate(8))
				.setIV(Helpers.NByteString(16)).setAESKeyType(AESKeyType.PASSWORD).build();
	}

	public Key createEncryptionKey(String encryptionKey) throws Exception {
		if (Objects.isNull(encryptionKey)) {
			encryptionKey = passwordGenerator.generate(keyProperties.keyLength());
		}
		return new AESKeyBuilder().setKey(encryptionKey).setSalt(simpleStringGenerator.generate(8))
				.setIV(Helpers.NByteString(16)).setAESKeyType(AESKeyType.ENCYPTION_KEY).build();
	}

	public Key createRSAKey(String key, RSAKeyType subKeyType) {
		if (Objects.isNull(key) || Objects.isNull(subKeyType)) {
			throw new NullPointerException("RSA key or Key type is null. Cannot create object.");
		}
		return new RSAKey(key, subKeyType);
	}

	public Key createRSAKey(java.security.Key key, RSAKeyType subKeyType) {
		return createRSAKey(Helpers.keyToString(key), subKeyType);
	}

	public Key clone(Key key) {
		switch (key.type()) {
			case AES:
				return new AESKey((AESKey) key);
			case RSA:
				return new RSAKey((RSAKey) key);
			default:
				break;
		}
		return null;
	}
}
