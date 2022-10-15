package com.key.password_manager.key;

import java.util.Objects;
import com.key.password_manager.key.types.KeyType;
import com.key.password_manager.key.types.RSAKeyType;

public class RSAKey extends AbstarctKey {

    private RSAKeyType type;

    public RSAKey(String key, RSAKeyType type) {
        super(key, KeyType.RSA);
        setSubKeyType(type);
    }

    public RSAKey(RSAKey copyObject) {
        this(copyObject.getKey(), copyObject.getSubKeyType());
    }

    public boolean isPrivateKey() {
        return RSAKeyType.PRIVATE.equals(type);
    }

    public boolean isPublicKey() {
        return RSAKeyType.PUBLIC.equals(type);
    }

    public RSAKeyType getSubKeyType() {
        return type;
    }

    public void setSubKeyType(RSAKeyType type) {
        if (Objects.isNull(type)) {
            throw new NullPointerException(
                    "Public/Private key type not provided while creation of RSA Key.");
        }
        this.type = type;
    }
}
