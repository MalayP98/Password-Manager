package com.key.password_manager.encryption;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import com.key.password_manager.key.RSAKey;
import com.key.password_manager.key.keypair.PrivatePublicKeyPair;
import com.key.password_manager.utils.Helpers;

public class RSAEncryption {

    public static PrivatePublicKeyPair createKeyPair() throws NoSuchAlgorithmException {
        KeyPair privatePublicKeyPair = keyPairGenerator();
        return new PrivatePublicKeyPair(
                new RSAKey(convertKeyToString(privatePublicKeyPair.getPrivate())),
                new RSAKey(convertKeyToString(privatePublicKeyPair.getPublic())));
    }

    public static KeyPair keyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.genKeyPair();
        return pair;
    }

    public static String convertKeyToString(Key key) {
        return Helpers.Base64encoder(key.getEncoded());
    }

    public static Key getKeyFromStringForRSA(String key, String type) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        if (type.equals("public")) {
            return keyFactory.generatePublic(new X509EncodedKeySpec(Helpers.Base64decoder(key)));
        } else if (type.equals("private")) {
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Helpers.Base64decoder(key)));
        }
        throw new Exception("Invalid key type.");
    }
}
