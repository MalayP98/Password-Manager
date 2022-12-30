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

	private Key createAESKey(String key, AESKeyType aesKeyType) throws Exception {
		if (Objects.isNull(key)) {
			key = passwordGenerator.generate(keyProperties.keyLength());
		}
		return new AESKeyBuilder().setKey(key).setSalt(simpleStringGenerator.generate(8))
				.setIV(Helpers.NByteString(16)).setAESKeyType(aesKeyType).build();
	}

	public Key createPassword(String password) throws Exception {
		return createAESKey(password, AESKeyType.PASSWORD);
	}

	public Key createEncryptionKey(String encryptionKey) throws Exception {
		return createAESKey(encryptionKey, AESKeyType.ENCYPTION_KEY);
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
}
