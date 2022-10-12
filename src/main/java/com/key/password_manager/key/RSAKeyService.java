package com.key.password_manager.key;

import java.security.KeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.key.password_manager.encryption.EncryptionStrategy;
import com.key.password_manager.encryption.exceptions.EncryptionException;

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
}
