package com.key.password_manager.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.encryption.helpers.AESUtil;
import com.key.password_manager.utils.Helpers;

@Component("AES")
public class AESEncrypter implements EncryptionStrategy {

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

    private boolean verifyKeyDetails(Map<String, String> keyDetails) {
        return (keyDetails.containsKey("salt") && keyDetails.containsKey("iv")
                && keyDetails.containsKey("key"));
    }

    @Override
    public String encrypt(Map<String, String> keyDetails, String data) throws KeyException {
        if (!verifyKeyDetails(keyDetails)) {
            throw new KeyException("Required details to create not found!");
        }
        try {
            return encrypt(
                    AESUtil.getKeyFromStringForAES(keyDetails.get("key"), keyDetails.get("salt")),
                    AESUtil.convertStringToIv(keyDetails.get("iv")), data);
        } catch (Exception e) {
            throw new EncryptionException("Unable to encrypt data!");
        }
    }

    @Override
    public String decrypt(Map<String, String> keyDetails, String data) throws KeyException {
        if (!verifyKeyDetails(keyDetails)) {
            throw new KeyException("Required details to create not found!");
        }
        try {
            return decrypt(
                    AESUtil.getKeyFromStringForAES(keyDetails.get("key"), keyDetails.get("salt")),
                    AESUtil.convertStringToIv(keyDetails.get("iv")), data);
        } catch (Exception e) {
            throw new EncryptionException("Unable to encrypt data!");
        }
    }
}
