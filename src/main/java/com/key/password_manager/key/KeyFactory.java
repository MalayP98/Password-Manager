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

	/**
	 * Creates an AESKey on the data provided. If the {@param key} is null then a random key is
	 * generated. Salt and IV are also randomly generated. To have more control over these
	 * properties use {@link AESKeyBuilder}.
	 * 
	 * @param key        : AES key is created on top of this key.
	 * @param aesKeyType : type of AES key to be produced. Know more about AES key type here,
	 *                   {@link AESKeyType}.
	 * @return AESKey
	 * @apiNode This function creates a {@link AESKey} with random salt and IV. This class is not
	 *          responsible for saving the key. Save the key if it is being required for more than
	 *          one transaction.
	 * @throws Exception
	 */
	private Key createAESKey(String key, AESKeyType aesKeyType) throws Exception {
		if (Objects.isNull(key)) {
			key = passwordGenerator.generate(keyProperties.keyLength());
		}
		return new AESKeyBuilder().setKey(key).setSalt(simpleStringGenerator.generate(8))
				.setIV(Helpers.NByteString(16)).setAESKeyType(aesKeyType).build();
	}

	/**
	 * Creates AES key with type {@code PASSWORD}.
	 * 
	 * @apiNode If {@code null} is passed into this function then a random key is generated. This
	 *          class is not responsible for saving the randomly generated key. Save the returned
	 *          key if the key will be used for more than one transaction. Only to be used if the
	 *          user is going to save the generated key.
	 */
	public Key createPassword(String password) throws Exception {
		return createAESKey(password, AESKeyType.PASSWORD);
	}

	/**
	 * Creates AES key with type {@code ENCRYPTION}.
	 * 
	 * @apiNode If {@code null} is passed into this function then a random key is generated. This
	 *          class is not responsible for saving the randomly generated key. Save the returned
	 *          key if the key will be used for more than one transaction. Only to be used if the
	 *          user is going to save the generated key.
	 */
	public Key createEncryptionKey(String encryptionKey) throws Exception {
		return createAESKey(encryptionKey, AESKeyType.ENCYPTION_KEY);
	}

	/**
	 * Create and {@link RSAKey} with supplied type. Know more about key type here,
	 * {@link RSAKeyType}.
	 * 
	 * @param key        : this is non null paramter. Will result in a {@link NullPointerException}
	 *                   is null is supplied.
	 * @param subKeyType : this is non null paramter. Will result in a {@link NullPointerException}
	 *                   is null is supplied.
	 */
	public Key createRSAKey(String key, RSAKeyType subKeyType) {
		if (Objects.isNull(key) || Objects.isNull(subKeyType)) {
			throw new NullPointerException("RSA key or Key type is null. Cannot create object.");
		}
		return new RSAKey(key, subKeyType);
	}

	/**
	 * Takes {@link java.security.Key} type key and convert it into string using the {@link Helpers}
	 * class. Creates a {@link RSAKey} using the {@link #createRSAKey(String, RSAKeyType)} method.
	 * 
	 * @return a RSA key
	 */
	public Key createRSAKey(java.security.Key key, RSAKeyType subKeyType) {
		return createRSAKey(Helpers.keyToString(key), subKeyType);
	}
}
