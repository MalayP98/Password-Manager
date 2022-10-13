package com.key.password_manager.encryption;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.key.password_manager.keypair.KeyPairFactory;
import com.key.password_manager.keypair.PrivatePublicKeyPair;

@Service
public class RSAKeyPairStore {

    @Autowired
    private KeyPairFactory keyPairFactory;

    private Logger LOG = LoggerFactory.getLogger(RSAKeyPairStore.class);

    private HashMap<Long, PrivatePublicKeyPair> rsaKeyRegistry = new HashMap<>();

    public void register(Long userId) {
        if (!rsaKeyRegistry.containsKey(userId)) {
            try {
                rsaKeyRegistry.put(userId, keyPairFactory.createPrivatePublicKeyPair());
            } catch (NoSuchAlgorithmException e) {
                LOG.error("Unable to register user with id " + userId);
            }
        }
        LOG.info("User id already pair with a RSA key pair.");
    }

    public PrivatePublicKeyPair getRSAKeyPair(Long userId) throws Exception {
        if (rsaKeyRegistry.containsKey(userId)) {
            return rsaKeyRegistry.get(userId);
        }
        register(userId);
        return getRSAKeyPair(userId);
    }
}
