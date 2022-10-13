package com.key.password_manager.key;

import java.util.Objects;

public class RSAKey extends AbstarctKey {

    private RSAKeyType type;

    public RSAKey(String key, RSAKeyType type) {
        super(key, KeyType.RSA);
        setSubKeyType(type);
    }

    public boolean isPrivateKey() {
        return RSAKeyType.PRIVATE.equals(type);
    }

    public boolean isPublicKey() {
        return RSAKeyType.PUBLIC.equals(type);
    }

    public void setSubKeyType(RSAKeyType type) {
        if (Objects.isNull(type)) {
            throw new NullPointerException(
                    "Public/Private key type not provided while creation of RSA Key.");
        }
        this.type = type;
    }
}
