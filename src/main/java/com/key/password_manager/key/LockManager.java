package com.key.password_manager.key;

import java.security.KeyException;
import com.key.password_manager.encryption.exceptions.EncryptionException;

public interface LockManager {

    public String lock(Key key, String data) throws EncryptionException, KeyException;

    public String unlock(Key key, String data) throws EncryptionException, KeyException;
}
