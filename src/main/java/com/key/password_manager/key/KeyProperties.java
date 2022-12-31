package com.key.password_manager.key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Stores several key properties which is being used throughout the project so that there is not
 * need to create a varible everytime someone wants to used these properties.
 */
@Component
public class KeyProperties {

	@Value("${com.keys.default.aeskey.salt}")
	private String salt;

	@Value("${com.keys.default.aeskey.iv}")
	private String iv;

	@Value("${com.keys.default.aeskey.secret}")
	private String secret;

	@Value("${com.keys.default.aeskey.length}")
	private int keyLength;

	public String salt() {
		return salt;
	}

	public String iv() {
		return iv;
	}

	public String secret() {
		return secret;
	}

	public int keyLength() {
		return keyLength;
	}
}
