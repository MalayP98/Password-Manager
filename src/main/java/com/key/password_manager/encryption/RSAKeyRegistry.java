package com.key.password_manager.encryption;

import java.util.HashMap;
import com.key.password_manager.key.keypair.PrivatePublicKeyPair;

public class RSAKeyRegistry {

    private static RSAKeyRegistry INSTANCE = null;

    private HashMap<Long, PrivatePublicKeyPair> rsaKeyRegistry = new HashMap<>();

    private RSAKeyRegistry() {
    }

    public static RSAKeyRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RSAKeyRegistry();
        }
        return INSTANCE;
    }

}
