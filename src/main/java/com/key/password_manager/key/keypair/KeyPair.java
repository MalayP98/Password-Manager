package com.key.password_manager.key.keypair;

import java.util.Objects;
import com.key.password_manager.key.Key;

public abstract class KeyPair {

    private Key firstKey;

    private Key secondKey;

    public KeyPair() {
    }

    public KeyPair(Key firstKey, Key secondKey) {
        setFirstKey(firstKey);
        setSecondKey(secondKey);
    }

    protected Key firstKey() {
        return firstKey;
    }

    protected Key secondKey() {
        return secondKey;
    }

    public void setFirstKey(Key firstKey) {
        if (Objects.isNull(firstKey))
            throw new NullPointerException("One of the key pair is null.");
        this.firstKey = firstKey;
    }

    public void setSecondKey(Key secondKey) {
        if (Objects.isNull(secondKey))
            throw new NullPointerException("One of the key pair is null.");
        this.secondKey = secondKey;
    }

    @Override
    public String toString() {
        return "(" + this.firstKey() + ", " + this.secondKey() + ")";
    }
}
