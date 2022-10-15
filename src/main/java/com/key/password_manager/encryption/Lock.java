package com.key.password_manager.encryption;

import java.security.KeyException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;

@Component
public class Lock {

    @Autowired
    @Qualifier("AES")
    private EncryptionStrategy aseEncrypter;

    @Autowired
    @Qualifier("RSA")
    private EncryptionStrategy rsaEncrypter;

    private final Logger LOG = LoggerFactory.getLogger(Lock.class);

    public String lock(Key key, String data) {
        try {
            switch (key.type()) {
                case AES:
                    return aseEncrypter.encrypt(key, data);
                case RSA:
                    return rsaEncrypter.encrypt(key, data);
                default:
                    LOG.info("Unable to lock data. No matching algorithm found.");
            }
        } catch (EncryptionException | KeyException e) {
            LOG.error("Somthing went wrong while locking data.", e.getMessage());
        }
        return null;
    }

    public String unlock(Key key, String data) {
        try {
            switch (key.type()) {
                case AES:
                    return aseEncrypter.decrypt(key, data);
                case RSA:
                    return rsaEncrypter.decrypt(key, data);
                default:
                    LOG.info("Unable to unlock data. No matching algorithm found.");
            }
        } catch (DecryptionException | KeyException e) {
            LOG.error("Somthing went wrong while unlocking data.", e.getMessage());
        }
        return null;
    }

    private Key getSequentiallyUnlockedKey(List<Key> keys) {
        if (keys.size() < 2) {
            LOG.error("Sequential unlocking requires 2 or more keys.");
            return null;
        }
        for (int i = 0; i < keys.size() - 1; i++) {
            keys.get(i + 1).setKey(unlock(keys.get(i), keys.get(i + 1).getKey()));
        }
        return keys.get(keys.size() - 1);
    }

    public String squentialLock(List<Key> keys, String data) {
        return lock(getSequentiallyUnlockedKey(keys), data);
    }

    public String squentialUnlock(List<Key> keys, String data) {
        return unlock(getSequentiallyUnlockedKey(keys), data);
    }
}
