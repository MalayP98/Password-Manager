package com.key.password_manager.locks;

import java.security.KeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.AESEncryptionStrategy;
import com.key.password_manager.encryption.EncryptionStrategy;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.types.KeyType;

@Component
public class Lock {

	@Autowired
	@Qualifier("AES")
	private EncryptionStrategy aseEncrypter;

	@Autowired
	@Qualifier("RSA")
	private EncryptionStrategy rsaEncrypter;

	private final Logger LOG = LoggerFactory.getLogger(Lock.class);

	/**
	 * Checks the type of the ({@link KeyType}). If type is {@link KeyType#AES} then
	 * {@link AESEncryptionStrategy} is used to lock the data. If type is {@link KeyType#RSA} then
	 * {@link RSEncryptionStrategy} is used to lock the data. If no key type is matched then a log
	 * is generated and printed.
	 * 
	 * @param key  : supplied {@link Key}.
	 * @param data : data to locked.
	 * @return locked data, if exception encountered then returns {@code null}.
	 */
	public String lock(Key key, String data) {
		try {
			switch (key.type()) {
				case AES:
					return aseEncrypter.encrypt(key, data);
				case RSA:
					return rsaEncrypter.encrypt(key, data);
				default:
					LOG.info("Unable to lock data. No matching algorithm found.");
			}
		} catch (EncryptionException | KeyException e) {
			LOG.error("Somthing went wrong while locking data.", e.getMessage());
		}
		return null;
	}

	/**
	 * Same as {@link #lock(Key, String)}.
	 * 
	 * @param key  : supplied {@link Key}.
	 * @param data : data to locked.
	 * @return unlocked data, if exception encountered then returns {@code null}.
	 */
	public String unlock(Key key, String data) {
		try {
			switch (key.type()) {
				case AES:
					return aseEncrypter.decrypt(key, data);
				case RSA:
					return rsaEncrypter.decrypt(key, data);
				default:
					LOG.info("Unable to unlock data. No matching algorithm found.");
			}
		} catch (DecryptionException | KeyException e) {
			e.printStackTrace();
			LOG.error("Somthing went wrong while unlocking data.", e.getMessage());
		}
		return null;
	}

	/**
	 * Take the arrays of Keys and unlocks them sequencially. From more information see
	 * {@link #sequentialUnlock(Key[])}.This method returns fully unlocked key which is the used to
	 * lock the data.
	 * 
	 * @param keys : arrays to {@link Key}.
	 * @param data : data to be locked.
	 * @return locked data.
	 */
	public String lock(Key[] keys, String data) {
		return lock(sequentialUnlock(keys), data);
	}

	/**
	 * Same as {@link #lock(Key[], String)}. This method returns fully unlocked key which is the
	 * used to unlock the data.
	 * 
	 * @param keys : arrays to {@link Key}.
	 * @param data : data to be locked.
	 * @return locked data.
	 */
	public String unlock(Key[] keys, String data) {
		return unlock(sequentialUnlock(keys), data);
	}

	/**
	 * Start from the second key in the array
	 * 
	 * * @param keys : arrays to {@link Key}.
	 */
	private Key sequentialUnlock(Key[] keys) {
		for (int i = 1; i < keys.length; i++) {
			keys[i].setKey(unlock(keys[i - 1], keys[i].getKey()));
		}
		return keys[keys.length - 1];
	}
}
