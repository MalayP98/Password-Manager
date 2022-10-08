package com.key.password_manager.key;

import java.security.KeyException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.EncryptionStrategy;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.keypair.PasswordEncryptionKeyPair;
import com.key.password_manager.utils.Helpers;

@Component
public class KeyService {

    @Autowired
    @Qualifier("AES")
    private EncryptionStrategy aseEncrypter;

    @Autowired
    private PasswordGenerator passwordGenerator;

    private AESKey createNewKey(String key, KeyTypes type) {
        return new AESKey(key, Helpers.randomString(), Helpers.NByteString(16), type);
    }

    public AESKey createNewPassword(String password) {
        if (Objects.isNull(password))
            throw new NullPointerException("Password cannot be null.");
        return createNewKey(password, KeyTypes.PASSWORD);
    }

    public AESKey createNewEncryptionKey(String encryptionKey) {
        if (Objects.isNull(encryptionKey))
            throw new NullPointerException("Encryption key cannot be null.");
        return createNewKey(encryptionKey, KeyTypes.ENCYPTION_KEY);
    }

    public String lockData(Key key, String data) throws EncryptionException, KeyException {
        return aseEncrypter.encrypt(key, data);
    }

    public String unlockData(Key key, String data) throws DecryptionException, KeyException {
        return aseEncrypter.decrypt(key, data);
    }

    public PasswordEncryptionKeyPair createPasswordEncryptionKeyPair(String password)
            throws EncryptionException, KeyException {
        AESKey passwordKey = createNewPassword(password);
        AESKey encryptionKey = createNewEncryptionKey(passwordGenerator.generate());
        encryptionKey.setKey(lockData(passwordKey, encryptionKey.getKey()));
        return new PasswordEncryptionKeyPair(passwordKey, encryptionKey);
    }
}