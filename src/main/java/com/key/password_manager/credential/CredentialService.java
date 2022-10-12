package com.key.password_manager.credential;

import java.security.KeyException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.encryption.exceptions.DecryptionException;
import com.key.password_manager.encryption.exceptions.EncryptionException;
import com.key.password_manager.key.Key;
import com.key.password_manager.key.RSAKeyService;
import com.key.password_manager.key.AESKeyService;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@Service
public class CredentialService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private AESKeyService aesKeyService;

    @Autowired
    private RSAKeyPairStore rsaKeyRegistry;

    @Autowired
    private RSAKeyService rsaKeyService;

    @Autowired
    private UserService userService;

    public String addCredential(Credential credential, Long userId, String lockedPassword) {
        try {
            User credentialOwner = userService.getUser(userId);
            if (Objects.isNull(credentialOwner))
                throw new NullPointerException("Credential owner not found.");
            credential.setUser(credentialOwner);
            credentialOwner.getPassword().setKey(decryptPassword(userId, lockedPassword));
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
        encryptionKey.setKey(aesKeyService.unlock(password, encryptionKey.getKey()));
        String encryptedCredential = aesKeyService.lock(encryptionKey, credential);
        encryptionKey.setKey(aesKeyService.lock(password, encryptionKey.getKey()));
        return encryptedCredential;
    }

    private String decryptCredential(Key password, Key encryptionKey, String credential)
            throws DecryptionException, KeyException {
        encryptionKey.setKey(aesKeyService.unlock(password, encryptionKey.getKey()));
        String decryptedCredential = aesKeyService.unlock(encryptionKey, credential);
        encryptionKey.setKey(aesKeyService.lock(password, encryptionKey.getKey()));
        return decryptedCredential;
    }

    private String decryptPassword(Long userId, String key)
            throws EncryptionException, KeyException, Exception {
        return rsaKeyService.unlock(rsaKeyRegistry.getRSAKeyPair(userId).getPrivateKey(), key);
    }
}
