package com.key.password_manager.encryption;

import java.security.KeyException;
import java.util.Map;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;

public interface EncryptionStrategy {

    public String encrypt(Map<String, String> keyDetails, String data)
            throws EncryptionException, KeyException;

    public String decrypt(Map<String, String> keyDetails, String data)
            throws DecryptionException, KeyException;
}
