package com.key.password_manager.key.keypair;

import java.util.Objects;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.Key;

public class PasswordEncryptionKeyPair implements KeyPair {

    private Key password;

    private Key encryptionKey;

    private PasswordEncryptionKeyPair() {
    }

    public PasswordEncryptionKeyPair(AESKey password, AESKey encryptionKey) {
        if (Objects.isNull(password) || Objects.isNull(encryptionKey)) {
            throw new NullPointerException(
                    "Either one or both keys are empty. Pair cannot be created!");
        }
        this.password = password;
        this.encryptionKey = encryptionKey;
    }

    public Key getPassword() {
        return firstKey();
    }

    public Key getEncryptionKey() {
        return secondKey();
    }

    @Deprecated
    @Override
    public Key firstKey() {
        return password;
    }

    @Deprecated
    @Override
    public Key secondKey() {
        return encryptionKey;
    }
}
