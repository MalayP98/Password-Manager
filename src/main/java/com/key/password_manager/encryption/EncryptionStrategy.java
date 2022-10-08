package com.key.password_manager.encryption;

import java.security.KeyException;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;

public interface EncryptionStrategy {

        public String encrypt(Key key, String data) throws EncryptionException, KeyException;

        public String decrypt(Key key, String data) throws DecryptionException, KeyException;
}
