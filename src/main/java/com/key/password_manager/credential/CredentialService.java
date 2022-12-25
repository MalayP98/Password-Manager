package com.key.password_manager.credential;

import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.key.password_manager.locks.CredentialLock;
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
	 * Fetch the owner of the credential by {@param ownerId}. After this supplied credential is
	 * locked using {@link CredentialLock}. Finally the new password and fetched owner is set to the
	 * supplied credential and the credential is saved. If the credential is saved the
	 * {@code Success} is returned else {@code Failed}.
	 * 
	 * @param credential     : suppiled credential to be added.
	 * @param ownerId        : credential added to this user id.
	 * @param lockedPassword : RSA locked password.
	 * @return : status of the method.
	 */
	public String addCredential(Credential credential, Long ownerId, String lockedPassword) {
		try {
			User credentialOwner = userService.getUser(ownerId);
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

	/**
	 * The credential is fetched using the {@param ownerId} and {@param credentialId}. After this
	 * fetched credential is locked using {@link CredentialLock} and the credential is returned.
	 * 
	 * @param ownerId        : credential added to this user id.
	 * @param credentialId   : credential to be fetched.
	 * @param lockedPassword : RSA locked password.
	 * @return
	 */
	public Credential retriveCredential(Long ownerId, Long credentialId, String lockedPassword) {
		Credential credential = null;
		try {
			credential = credentialRepository.findByIdAndOwnerId(credentialId, ownerId);
			credential.setPassword(credentialLock.unlock(credential.getOwner(), lockedPassword,
					credential.getPassword()));
		} catch (Exception e) {
			LOG.error("Cannot get credential. Error message: {}", e.getMessage());
		}
		return credential;
	}

	/**
	 * Identical to {@link #retriveCredential(Long, Long, String)}, just done for multiple
	 * credentials.
	 * 
	 * @param from           : Serial number of the record to start from.
	 * @param pageSize       : Number of records to be shown on a single call.
	 * @param ownerId        : credential added to this user id.
	 * @param lockedPassword : RSA locked password.
	 * @return
	 */
	public List<Credential> retriveCredentials(Integer from, Integer pageSize, Long ownerId,
			String lockedPassword) {
		List<Credential> credentials =
				credentialRepository.findAllByOwnerId(ownerId, PageRequest.of(from, pageSize));
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
