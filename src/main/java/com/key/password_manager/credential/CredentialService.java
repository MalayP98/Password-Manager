package com.key.password_manager.credential;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import com.key.password_manager.encryption.EncryptionService;
import com.key.password_manager.user.UserService;

@Service
public class CredentialService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CredentialRepository credentialRepository;

    // @Autowired
    // private EncryptionService encryptionService;

    @Autowired
    private UserService userService;

    public String addCredential(Credential credential, Long userId) {
        try {
            credential.setUser(userService.getUser(userId));
            credentialRepository.save(credential);
        } catch (Exception e) {
            LOG.error("Unable to save credential. Error message {}: ", e.getMessage());
            return "Failed";
        }
        return "Success";
    }

}
