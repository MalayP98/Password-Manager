package com.key.password_manager.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class Helpers {

    public static String randomString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String NByteString(int bytes) {
        byte[] randomBytes = new byte[bytes];
        new SecureRandom().nextBytes(randomBytes);
        return Base64encoder(randomBytes);
    }

    public static String Base64encoder(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    public static byte[] Base64decoder(String str) {
        return Base64.getDecoder().decode(str);
    }
}
