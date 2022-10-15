package com.key.password_manager.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.exceptions.DecryptionException;
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
            return encrypt(getKeyFromStringForAES(aesKey.getKey(), aesKey.getSalt()),
                    convertStringToIv(aesKey.getIv()), data);
        } catch (Exception e) {
            throw new EncryptionException("Unable to encrypt data! " + e.getMessage());
        }
    }

    @Override
    public String decrypt(Key key, String data) throws KeyException {
        try {
            AESKey aesKey = (AESKey) key;
            return decrypt(getKeyFromStringForAES(aesKey.getKey(), aesKey.getSalt()),
                    convertStringToIv(aesKey.getIv()), data);
        } catch (Exception e) {
            throw new DecryptionException("Unable to decrypt data! " + e.getMessage());
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

    private SecretKey getKeyFromStringForAES(String key, String salt)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }

    private IvParameterSpec convertStringToIv(String ivString) {
        return new IvParameterSpec(Helpers.Base64decoder(ivString));
    }
}
