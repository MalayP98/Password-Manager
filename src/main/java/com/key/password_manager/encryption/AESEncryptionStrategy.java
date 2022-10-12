package com.key.password_manager.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.Key;
import com.key.password_manager.utils.Helpers;

@Component("AES")
public class AESEncryptionStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(Key key, String data) throws KeyException {
        try {
            AESKey aesKey = (AESKey) key;
            return encrypt(AESEncryption.getKeyFromStringForAES(aesKey.getKey(), aesKey.getSalt()),
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
            return decrypt(AESEncryption.getKeyFromStringForAES(aesKey.getKey(), aesKey.getSalt()),
                    AESEncryption.convertStringToIv(aesKey.getIv()), data);
        } catch (Exception e) {
            throw new EncryptionException("Unable to decrypt data!  " + e.getMessage());
        }
    }

    private String encrypt(SecretKey key, IvParameterSpec iv, String rawData)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(rawData.getBytes());
        return Helpers.Base64encoder(cipherText);
    }

    private String decrypt(SecretKey key, IvParameterSpec iv, String encryptedData)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Helpers.Base64decoder(encryptedData));
        return new String(plainText);
    }
}
