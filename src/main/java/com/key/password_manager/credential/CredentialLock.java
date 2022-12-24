package com.key.password_manager.credential;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.Lock;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.Key;
import com.key.password_manager.user.User;

@Component
public class CredentialLock {

	@Autowired
	private Lock lock;

	@Autowired
	private RSAKeyPairStore rsaKeyPairStore;

	public String lock(Key privateKey, Key password, Key encryptionKey, String data) {
		return lock.lock(new Key[] {privateKey, password, encryptionKey}, data);
	}


	public String unlock(Key privateKey, Key password, Key encryptionKey, String data) {
		return lock.lock(password,
				lock.unlock(new Key[] {privateKey, password, encryptionKey}, data));
	}

	public String lock(User owner, String lockedPassword, String data) throws Exception {
		return lock(rsaKeyPairStore.getRSAKeyPair(owner.getId()).getPrivateKey(),
				createKeyWithLockedPassword(owner, lockedPassword),
				new AESKey((AESKey) owner.getEncryptionKey()), data);
	}

	public String unlock(User owner, String lockedPassword, String data) throws Exception {
		return unlock(rsaKeyPairStore.getRSAKeyPair(owner.getId()).getPrivateKey(),
				createKeyWithLockedPassword(owner, lockedPassword),
				new AESKey((AESKey) owner.getEncryptionKey()), data);
	}

	private Key createKeyWithLockedPassword(User owner, String lockedPassword) {
		Key password = new AESKey((AESKey) owner.getPassword());
		password.setKey(lockedPassword);
		return password;
	}
}
