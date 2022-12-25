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

	private Logger LOG = LoggerFactory.getLogger(KeyFactory.class);

	public Key createAESKey(String key, String salt, String iv, AESKeyType type) {
		if (Objects.isNull(key) || key.isEmpty()) {
			LOG.debug("Key is null or empty, creating random key.");
			key = passwordGenerator.generate(12);
		}
		if (Objects.isNull(salt) || salt.isEmpty()) {
			LOG.debug("Salt is null or empty, creating random salt.");
			salt = Helpers.randomString();
		}
		if (Objects.isNull(iv) || iv.isEmpty()) {
			LOG.debug("IV is null or empty, creating random IV.");
			iv = Helpers.NByteString(16);
		}
		if (Objects.isNull(type)) {
			type = AESKeyType.PASSWORD;
		}
		return new AESKey(key, salt, iv, type);
	}

	public Key createPassword(String password) {
		return createAESKey(password, null, null, AESKeyType.PASSWORD);
	}

	public Key createEncryptionKey(String encryptionKey) {
		return createAESKey(encryptionKey, null, null, AESKeyType.ENCYPTION_KEY);
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
