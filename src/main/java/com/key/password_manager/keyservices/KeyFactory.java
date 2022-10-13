package com.key.password_manager.keyservices;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.AESKeyType;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.KeyType;
import com.key.password_manager.key.RSAKey;
import com.key.password_manager.key.RSAKeyType;
import com.key.password_manager.utils.Helpers;
import com.key.password_manager.utils.PasswordGenerator;

@Component
public class KeyFactory {

    @Autowired
    private PasswordGenerator passwordGenerator;

    private Logger LOG = LoggerFactory.getLogger(KeyFactory.class);

    public Key createAESKey(String key, String salt, String iv, AESKeyType type) {
        if (Objects.isNull(key) || key.isEmpty()) {
            LOG.debug("Key is null or empty creating random key.");
            key = passwordGenerator.generate();
        }
        if (Objects.isNull(salt) || salt.isEmpty()) {
            LOG.debug("Salt is null or empty creating random salt.");
            salt = Helpers.randomString();
        }
        if (Objects.isNull(iv) || iv.isEmpty()) {
            LOG.debug("IV is null or empty creating random IV.");
            salt = Helpers.NByteString(16);
        }
        if (Objects.isNull(type)) {
            type = AESKeyType.PASSWORD;
        }
        return new AESKey(key, salt, iv, type);
    }

    public Key createPassword(String password) {
        return createAESKey(password, null, null, AESKeyType.PASSWORD);
    }

    public Key createEncryptionKey(String encryptionKey) {
        return createAESKey(encryptionKey, null, null, AESKeyType.ENCYPTION_KEY);
    }

    public Key createRSAKey(String key, RSAKeyType subKeyType) {
        if (Objects.isNull(key) || Objects.isNull(subKeyType)) {
            throw new NullPointerException("RSA key or Key type is null. Cannot create object.");
        }
        return new RSAKey(key, subKeyType);
    }

    public Key createRSAKey(java.security.Key key, RSAKeyType subKeyType) {
        return createRSAKey(Helpers.keyToString(key), subKeyType);
    }
}
