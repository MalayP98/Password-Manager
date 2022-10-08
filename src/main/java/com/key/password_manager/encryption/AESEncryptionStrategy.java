package com.key.password_manager.encryption;

import java.security.KeyException;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.Key;

@Component("AES")
public class AESEncryptionStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(Key key, String data) throws KeyException {
        try {
            AESKey aesKey = (AESKey) key;
            return AESEncryption.encrypt(
                    AESEncryption.getKeyFromStringForAES(aesKey.getKey(), aesKey.getSalt()),
                    AESEncryption.convertStringToIv(aesKey.getIv()), data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptionException("Unable to encrypt data!");
        }
    }

    @Override
    public String decrypt(Key key, String data) throws KeyException {
        try {
            AESKey aesKey = (AESKey) key;
            return AESEncryption.decrypt(
                    AESEncryption.getKeyFromStringForAES(aesKey.getKey(), aesKey.getSalt()),
                    AESEncryption.convertStringToIv(aesKey.getIv()), data);
        } catch (Exception e) {
            throw new EncryptionException("Unable to decrypt data!  " + e.getMessage());
        }
    }
}
