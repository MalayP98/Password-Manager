package com.key.password_manager.key.keypair;

import java.util.Objects;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.RSAKey;

public class PrivatePublicKeyPair implements KeyPair {

    private Key privateKey;

    private Key publicKey;

    private PrivatePublicKeyPair() {
    }

    public PrivatePublicKeyPair(RSAKey privateKey, RSAKey publicKey) {
        if (Objects.isNull(privateKey) || Objects.isNull(publicKey)) {
            throw new NullPointerException(
                    "Either one or both keys are empty. Pair cannot be created!");
        }
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public Key getPrivateKey() {
        return firstKey();
    }

    public Key getPublicKey() {
        return secondKey();
    }

    @Deprecated
    @Override
    public Key firstKey() {
        return privateKey;
    }

    @Deprecated
    @Override
    public Key secondKey() {
        return publicKey;
    }
}
