package com.key.password_manager.encryption;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import javax.crypto.Cipher;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.encryption.helpers.RSAUtil;
import com.key.password_manager.utils.Helpers;

@Component("RSA")
public class RSAEncrypter implements EncryptionStrategy {

    @Override
    public String encrypt(Map<String, String> keyDetails, String data) throws KeyException {
        if (!keyDetails.containsKey("publicKey")) {
            throw new KeyException("Cannot encrypt/decrypt data as key not found");
        }
        String publicKey = keyDetails.get("publicKey");
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE,
                    RSAUtil.getKeyFromStringForRSA(publicKey, "public"));
            return Helpers
                    .Base64encoder(encryptCipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new EncryptionException("Unable to encrypt data!");
        }
    }

    @Override
    public String decrypt(Map<String, String> keyDetails, String data) throws KeyException {
        if (!keyDetails.containsKey("privateKey")) {
            throw new KeyException("Cannot encrypt/decrypt data as key not found");
        }
        String privateKey = keyDetails.get("privateKey");
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE,
                    RSAUtil.getKeyFromStringForRSA(privateKey, "private"));
            return new String(decryptCipher.doFinal(Helpers.Base64decoder(data)),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptionException("Unable to decrypt data!");
        }
    }
}
