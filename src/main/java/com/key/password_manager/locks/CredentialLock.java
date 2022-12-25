package com.key.password_manager.locks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.Key;
import com.key.password_manager.user.User;

/**
 * Algorithm which is reponsible for locking and unclocking the credential.
 */
@Component
public class CredentialLock {

	@Autowired
	private Lock lock;

	@Autowired
	private RSAKeyPairStore rsaKeyPairStore;

	/**
	 * Creates a {@link Key} array from {@param privateKey}, {@param password} and
	 * {@param encryptionKey}. Here ordereding matters. See {@link Lock#lock(Key, String)}.
	 * 
	 * @param privateKey    : RSA private key taken from the {@link RSAKeyPairStore}.
	 * @param password      : AES password key, most probably adultrated with a RSA locked password.
	 * @param encryptionKey : AES encrytion key.
	 * @param data          : data to be locked.
	 * @return locked data.
	 */
	public String lock(Key privateKey, Key password, Key encryptionKey, String data) {
		return lock.lock(new Key[] {privateKey, password, encryptionKey}, data);
	}

	/**
	 * Same as {@link #lock(Key, Key, Key, String)}, only diffrence being it unlocks the data
	 * ofcourse, and also locks the data again with password as a key so that the data is not
	 * travelling naked over the network.
	 * 
	 * @param privateKey    : RSA private key taken from the {@link RSAKeyPairStore}.
	 * @param password      : AES password key, most probably adultrated with a RSA locked password.
	 * @param encryptionKey : AES encrytion key.
	 * @param data          : data to be unlocked.
	 * @return locked data which is AES locked with the password.
	 */
	public String unlock(Key privateKey, Key password, Key encryptionKey, String data) {
		return lock.lock(password,
				lock.unlock(new Key[] {privateKey, password, encryptionKey}, data));
	}

	/**
	 * Fetches the RSA {@link KeyPair} from the the {@link RSAKeyPairStore}. Creates a new password
	 * from the owner's password and replaces the key with {@param lockedPassword}. Creates a copy
	 * of encryption key present in the owner. Passes all these {@link Key}s with the data to
	 * {@link #lock(Key, Key, Key, String)}.
	 * 
	 * @param owner          : {@link User} of the credential is being locked.
	 * @param lockedPassword : RSA pubic key encrypted password.
	 * @param data           : data to be locked.
	 * @return locked data.
	 * @throws Exception
	 */
	public String lock(User owner, String lockedPassword, String data) throws Exception {
		return lock(rsaKeyPairStore.getRSAKeyPair(owner.getId()).getPrivateKey(),
				createKeyWithLockedPassword(owner, lockedPassword),
				new AESKey((AESKey) owner.getEncryptionKey()), data);
	}

	/**
	 * Same as {@link #lock(User, String, String)}.
	 * 
	 * @param owner          : {@link User} of the credential is being locked.
	 * @param lockedPassword : RSA pubic key encrypted password.
	 * @param data           : data to be locked.
	 * @return unlocked data.
	 * @throws Exception
	 */
	public String unlock(User owner, String lockedPassword, String data) throws Exception {
		return unlock(rsaKeyPairStore.getRSAKeyPair(owner.getId()).getPrivateKey(),
				createKeyWithLockedPassword(owner, lockedPassword),
				new AESKey((AESKey) owner.getEncryptionKey()), data);
	}

	/**
	 * Creates a copy of {@param owner}'s password using
	 * {@link AESKey#AESKey(String, String, String, AESKeyType)} copy constructor, and replaces the
	 * key with {@param lockedPassword}.
	 * 
	 * @param owner          : {@link User} of the credential is being locked.
	 * @param lockedPassword : RSA pubic key encrypted password.
	 * @return newly created Key with the password as lockedPassword.
	 */
	private Key createKeyWithLockedPassword(User owner, String lockedPassword) {
		Key password = new AESKey((AESKey) owner.getPassword());
		password.setKey(lockedPassword);
		return password;
	}
}
