package com.key.password_manager.credential;

import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@Service
public class CredentialService {

	@Autowired
	private CredentialRepository credentialRepository;

	@Autowired
	private CredentialLock credentialLock;

	@Autowired
	private UserService userService;

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param credential     : suppiled credential to added.
	 * @param userId         : credential added to this user id.
	 * @param lockedPassword : RSA locked password.
	 * @return : status of the method.
	 */
	public String addCredential(Credential credential, Long userId, String lockedPassword) {
		try {
			User credentialOwner = userService.getUser(userId);
			if (Objects.isNull(credentialOwner))
				throw new NullPointerException("Credential owner not found.");
			credential.setPassword(
					credentialLock.lock(credentialOwner, lockedPassword, credential.getPassword()));
			credential.setOwner(credentialOwner);
			credentialRepository.save(credential);
		} catch (Exception e) {
			LOG.error("Unable to save credential. Error message: {}", e.getMessage());
			return "Failed";
		}
		return "Success";
	}

	public Credential retriveCredential(Long userId, Long credentialId, String lockedPassword) {
		Credential credential = null;
		try {
			credential = credentialRepository.findByIdAndUserId(credentialId, userId);
			credential.setPassword(credentialLock.unlock(credential.getOwner(), lockedPassword,
					credential.getPassword()));
		} catch (Exception e) {
			LOG.error("Cannot get credential. Error message: {}", e.getMessage());
		}
		return credential;
	}

	public List<Credential> retriveCredentials(Integer from, Integer pageSize, Long userId,
			String lockedPassword) {
		List<Credential> credentials =
				credentialRepository.findAllByUserId(userId, PageRequest.of(from, pageSize));
		for (Credential credential : credentials) {
			try {
				credential.setPassword(credentialLock.unlock(credential.getOwner(), lockedPassword,
						credential.getPassword()));
			} catch (Exception e) {
				LOG.error("Unable to unlock credential with id " + credential.getId() + ". ",
						e.getMessage());
			}
		}
		return credentials;
	}
}
