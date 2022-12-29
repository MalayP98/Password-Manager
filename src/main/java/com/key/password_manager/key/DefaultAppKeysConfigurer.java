package com.key.password_manager.key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Creates a bean of type {@link Key} which is avaliable across the app. It can be used to AES lock
 * and unlock any data if required.
 */
@Configuration
public class DefaultAppKeysConfigurer {

	@Autowired
	private KeyFactory keyFactory;

	@Value("${com.keys.default.aeskey.salt}")
	private String defaultKeySalt;

	@Value("${com.keys.default.aeskey.iv}")
	private String defaultKeyIv;

	@Value("${com.keys.default.aeskey.secret}")
	private String appSecret;

	@Bean
	public Key defaultAESAppKey() {
		return keyFactory.createAESKey(appSecret, defaultKeySalt, defaultKeyIv, null);
	}

	/**
	 * This key will only contain salt and iv. User can manually set any key of their choice to lock
	 * and unlock a data.
	 * <h4>NOTE : Using this key can be dangreous as life cycle of this key is not monitored or
	 * saved by the app in any form. Always first set your key before locking or unlocking your
	 * data. This key shared amoung everyone. Maybe removed later.</h4>
	 * 
	 * @return multipurpose AES key.
	 */
	// @Bean
	// public Key multipurposeAESKey() {
	// return keyFactory.createAESKey(null, defaultKeySalt, defaultKeyIv, null);
	// }
}
