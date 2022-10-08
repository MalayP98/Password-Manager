package com.key.password_manager.encryption;

import java.nio.charset.StandardCharsets;
import java.security.KeyException;
import javax.crypto.Cipher;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;
import com.key.password_manager.utils.Helpers;

@Component("RSA")
public class RSAEncryptionStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(Key publicKey, String data) throws KeyException {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE,
                    RSAEncryption.getKeyFromStringForRSA(publicKey.getKey(), "public"));
            return Helpers
                    .Base64encoder(encryptCipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new EncryptionException("Unable to encrypt data!");
        }
    }

    @Override
    public String decrypt(Key privateKey, String data) throws KeyException {
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE,
                    RSAEncryption.getKeyFromStringForRSA(privateKey.getKey(), "private"));
            return new String(decryptCipher.doFinal(Helpers.Base64decoder(data)),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecryptionException("Unable to decrypt data!");
        }
    }
}
