package com.key.password_manager.credential;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.key.password_manager.encryption.Lock;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.KeyFactory;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@Service
public class CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private Lock lock;

    @Autowired
    private RSAKeyPairStore rsaKeyStore;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyFactory keyFactory;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public String addCredential(Credential credential, Long userId, String lockedPassword) {
        try {
            User credentialOwner = userService.getUser(userId);
            if (Objects.isNull(credentialOwner))
                throw new NullPointerException("Credential owner not found.");
            credential.setUser(credentialOwner);
            credential.setPassword(toggleCredentialLock(credential, userId, lockedPassword, true));
            credentialRepository.save(credential);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Unable to save credential. Error message: {}", e.getMessage());
            return "Failed";
        }
        return "Success";
    }

    public Credential retriveCredential(Long userId, Long credentialId, String lockedPassword) {
        Credential credential = null;
        try {
            credential = credentialRepository.findByIdAndUserId(credentialId, userId);
            credential.setPassword(toggleCredentialLock(credential, userId, lockedPassword, false));
        } catch (Exception e) {
            LOG.error("Cannot get credential. Error message: {}", e.getMessage());
        }
        return credential;
    }

    public List<Credential> retriveCredential(Integer from, Integer pageSize, Long userId,
            String lockedPassword) {
        List<Credential> credentials =
                credentialRepository.findAllByUserId(userId, PageRequest.of(from, pageSize));
        for (Credential credential : credentials) {
            try {
                credential.setPassword(
                        toggleCredentialLock(credential, userId, lockedPassword, false));
            } catch (Exception e) {
                LOG.error("Unable to unlock credential with id " + credential.getId() + ". ",
                        e.getMessage());
            }
        }
        return credentials;
    }

    private String encryptCredential(Key password, Key encryptionKey, String credential)
            throws DecryptionException, KeyException {
        List<Key> keys = new ArrayList<>();
        keys.add(password);
        keys.add(encryptionKey);
        return lock.squentialLock(keys, credential);
    }

    private String decryptCredential(Key password, Key encryptionKey, String credential)
            throws DecryptionException, KeyException {
        List<Key> keys = new ArrayList<>();
        keys.add(password);
        keys.add(encryptionKey);
        return lock.lock(password, lock.squentialUnlock(keys, credential));
    }

    private Key getUnlockedPassword(Key rsaPrivateKey, String lockedPassword, Key password) {
        String unlockedPassword = lock.unlock(rsaPrivateKey, lockedPassword);
        Key clonedKey = keyFactory.clone(password);
        clonedKey.setKey(unlockedPassword);
        return clonedKey;
    }

    private String toggleCredentialLock(Credential credential, Long userId, String lockedPassword,
            Boolean lock) throws Exception {
        User credentialOwner = credential.getUser();
        Key unlockedPassword =
                getUnlockedPassword(rsaKeyStore.getRSAKeyPair(userId).getPrivateKey(),
                        lockedPassword, credentialOwner.getPassword());
        if (lock)
            return encryptCredential(unlockedPassword,
                    keyFactory.clone(credentialOwner.getEncryptionKey()), credential.getPassword());
        else
            return decryptCredential(unlockedPassword,
                    keyFactory.clone(credentialOwner.getEncryptionKey()), credential.getPassword());
    }
}
