package com.key.password_manager.keyservices;

import java.security.KeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.key.password_manager.encryption.EncryptionStrategy;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.Lock;
import com.key.password_manager.keypair.PasswordEncryptionKeyPair;

@Service("AESKeyService")
public class AESKeyService {

    // @Autowired
    // @Qualifier("AES")
    // private EncryptionStrategy aseEncrypter;

    // @Autowired
    // private KeyFactory keyFactory;

    // public PasswordEncryptionKeyPair createPasswordEncryptionKeyPair(String password)
    // throws EncryptionException, KeyException {
    // Key passwordKey = keyFactory.createPassword(password);
    // Key encryptionKey = keyFactory.createEncryptionKey(null);
    // encryptionKey.setKey(lock(passwordKey, encryptionKey.getKey()));
    // return new PasswordEncryptionKeyPair(passwordKey, encryptionKey);
    // }
}
