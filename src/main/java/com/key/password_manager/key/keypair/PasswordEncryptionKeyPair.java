package com.key.password_manager.key.keypair;

import com.key.password_manager.key.Key;

public class PasswordEncryptionKeyPair extends KeyPair {

    private PasswordEncryptionKeyPair() {
    }

    public PasswordEncryptionKeyPair(Key password, Key encryptionKey) {
        super(password, encryptionKey);
    }

    public Key getPassword() {
        return firstKey();
    }

    public Key getEncryptionKey() {
        return secondKey();
    }
}
