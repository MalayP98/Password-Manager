package com.key.password_manager.encryption.exceptions;

public class DecryptionException extends RuntimeException {

    public DecryptionException(String errorMsg) {
        super(errorMsg);
    }
}
