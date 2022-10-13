package com.key.password_manager.credential;

import java.security.KeyException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.key.password_manager.constants.CredentialConstants;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.AESKeyType;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.Lock;
import com.key.password_manager.keyservices.KeyFactory;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@Service
public class CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private KeyFactory keyFactory;

    @Autowired
    private Lock lock;

    @Autowired
    private RSAKeyPairStore rsaKeyStore;

    @Autowired
    private UserService userService;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public String addCredential(Credential credential, Long userId, String lockedPassword) {
        try {
            User credentialOwner = userService.getUser(userId);
            if (Objects.isNull(credentialOwner))
                throw new NullPointerException("Credential owner not found.");
            credential.setUser(credentialOwner);
            credentialOwner.getPassword().setKey(decryptPassword(
                    rsaKeyStore.getRSAKeyPair(userId).getPrivateKey(), lockedPassword));
            credential.setPassword(encryptCredential(credentialOwner.getPassword(),
                    credentialOwner.getEncryptionKey(), credential.getPassword()));
            credentialRepository.save(credential);
        } catch (Exception e) {
            LOG.error("Unable to save credential. Error message: {}", e.getMessage());
            return "Failed";
        }
        return "Success";
    }

    public Credential retriveCredential(Long userId, Long credentialId) {
        Credential credential = null;
        try {
            User credentialOwner = userService.getUser(userId);
            credential = credentialRepository.findByUserIdAndCredentialId(userId, credentialId);
            credential.setPassword(decryptCredential(credentialOwner.getPassword(),
                    credentialOwner.getEncryptionKey(), credential.getPassword()));
        } catch (DecryptionException | KeyException e) {
            LOG.error("Cannot get credential. Error message: {}", e.getMessage());
        }
        return credential;
    }

    private String encryptCredential(Key password, Key encryptionKey, String credential)
            throws DecryptionException, KeyException {
        encryptionKey.setKey(lock.unlock(password, encryptionKey.getKey()));
        String encryptedCredential = lock.lock(encryptionKey, credential);
        encryptionKey.setKey(lock.lock(password, encryptionKey.getKey()));
        return encryptedCredential;
    }

    private String decryptCredential(Key password, Key encryptionKey, String credential)
            throws DecryptionException, KeyException {
        encryptionKey.setKey(lock.unlock(password, encryptionKey.getKey()));
        String decryptedCredential = lock.unlock(encryptionKey, credential);
        encryptionKey.setKey(lock.lock(password, encryptionKey.getKey()));
        return decryptedCredential;
    }

    private String decryptPassword(Key key, String password)
            throws EncryptionException, KeyException, Exception {
        String unlockedPassword = lock.unlock(key, password);
        return lock.lock(keyFactory.createAESKey(unlockedPassword, CredentialConstants.DEFAULT_SALT,
                CredentialConstants.DEFAULT_IV, AESKeyType.PASSWORD), unlockedPassword);
    }
}
