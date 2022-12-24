package com.key.password_manager.credential;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.Lock;
import com.key.password_manager.key.Key;
import com.key.password_manager.user.User;

@Component
public class CredentialLock {

	@Autowired
	private Lock lock;

	public String lock(Key privateKey, Key password, Key encryptionKey, String data) {
		return lock.lock(new Key[] {privateKey, password, encryptionKey}, data);
	}


	public String unlock(Key privateKey, Key password, Key encryptionKey, String data) {
		return lock.lock(password,
				lock.unlock(new Key[] {privateKey, password, encryptionKey}, data));
	}
}
