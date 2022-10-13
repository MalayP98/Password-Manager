package com.key.password_manager.keyservices;

import java.security.KeyException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.key.password_manager.encryption.EncryptionStrategy;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.AESKeyTypes;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.LockManager;
import com.key.password_manager.keypair.PasswordEncryptionKeyPair;
import com.key.password_manager.utils.Helpers;
import com.key.password_manager.utils.PasswordGenerator;

@Service("AESKeyService")
public class AESKeyService implements LockManager {

    @Autowired
    @Qualifier("AES")
    private EncryptionStrategy aseEncrypter;

    @Autowired
    private PasswordGenerator passwordGenerator;

    public AESKey createKey(String key, String salt, String iv, AESKeyTypes type) {
        return new AESKey(key, salt, iv, type);
    }

    public AESKey createKey(String key, AESKeyTypes type) {
        return new AESKey(key, Helpers.randomString(), Helpers.NByteString(16), type);
    }

    public AESKey createPassword(String password) {
        if (Objects.isNull(password))
            throw new NullPointerException("Password cannot be null.");
        return createKey(password, AESKeyTypes.PASSWORD);
    }

    public AESKey createEncryptionKey(String encryptionKey) {
        if (Objects.isNull(encryptionKey))
            throw new NullPointerException("Encryption key cannot be null.");
        return createKey(encryptionKey, AESKeyTypes.ENCYPTION_KEY);
    }

    public PasswordEncryptionKeyPair createPasswordEncryptionKeyPair(String password)
            throws EncryptionException, KeyException {
        AESKey passwordKey = createPassword(password);
        AESKey encryptionKey = createEncryptionKey(passwordGenerator.generate());
        encryptionKey.setKey(lock(passwordKey, encryptionKey.getKey()));
        return new PasswordEncryptionKeyPair(passwordKey, encryptionKey);
    }

    @Override
    public String lock(Key key, String data) throws EncryptionException, KeyException {
        return aseEncrypter.encrypt(key, data);
    }

    @Override
    public String unlock(Key key, String data) throws EncryptionException, KeyException {
        return aseEncrypter.decrypt(key, data);
    }
}
