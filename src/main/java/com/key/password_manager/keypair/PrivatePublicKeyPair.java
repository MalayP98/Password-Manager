package com.key.password_manager.keypair;

import com.key.password_manager.key.Key;

public class PrivatePublicKeyPair extends KeyPair {

    private PrivatePublicKeyPair() {
    }

    public PrivatePublicKeyPair(Key privateKey, Key publicKey) {
        super(privateKey, publicKey);
    }

    public Key getPrivateKey() {
        return firstKey();
    }

    public Key getPublicKey() {
        return secondKey();
    }
}
