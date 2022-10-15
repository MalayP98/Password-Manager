package com.key.password_manager.keypair;

import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.Lock;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.KeyFactory;
import com.key.password_manager.key.types.KeyType;
import com.key.password_manager.key.types.RSAKeyType;

@Component
public class KeyPairFactory {

    @Autowired
    private KeyFactory keyFactory;

    @Autowired
    private Lock lock;

    public PasswordEncryptionKeyPair createPasswordEncryptionKeyPair(String password,
            String encryptionKey) throws EncryptionException, KeyException {
        Key passwordKey_ = keyFactory.createPassword(password);
        Key encryptionKey_ = keyFactory.createEncryptionKey(encryptionKey);
        encryptionKey_.setKey(lock.lock(passwordKey_, encryptionKey_.getKey()));
        return new PasswordEncryptionKeyPair(passwordKey_, encryptionKey_);
    }

    public PasswordEncryptionKeyPair createPasswordEncryptionKeyPair(Key password,
            Key encryptionKey) throws Exception {
        if (!KeyType.AES.equals(password.type()) || !KeyType.AES.equals(encryptionKey.type())) {
            throw new Exception("Only AES key type can form Password-Encryption key pair.");
        }
        return new PasswordEncryptionKeyPair(password, encryptionKey);
    }

    public PrivatePublicKeyPair createPrivatePublicKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.genKeyPair();
        return new PrivatePublicKeyPair(
                keyFactory.createRSAKey(pair.getPrivate(), RSAKeyType.PRIVATE),
                keyFactory.createRSAKey(pair.getPublic(), RSAKeyType.PUBLIC));
    }
}
