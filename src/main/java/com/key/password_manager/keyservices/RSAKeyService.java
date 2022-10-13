package com.key.password_manager.keyservices;

import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.key.password_manager.encryption.EncryptionStrategy;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.LockManager;
import com.key.password_manager.key.RSAKey;
import com.key.password_manager.keypair.PrivatePublicKeyPair;
import com.key.password_manager.utils.Helpers;

@Service("RSAKeyService")
public class RSAKeyService implements LockManager {

    @Autowired
    @Qualifier("RSA")
    private EncryptionStrategy rsaEncrypter;



    @Override
    public String lock(Key key, String data) throws EncryptionException, KeyException {
        return rsaEncrypter.encrypt(key, data);
    }

    @Override
    public String unlock(Key key, String data) throws EncryptionException, KeyException {
        return rsaEncrypter.decrypt(key, data);
    }

    public PrivatePublicKeyPair createPrivatePublicKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.genKeyPair();
        return new PrivatePublicKeyPair(new RSAKey(Helpers.keyToString(pair.getPrivate())),
                new RSAKey(Helpers.keyToString(pair.getPublic())));
    }
}
