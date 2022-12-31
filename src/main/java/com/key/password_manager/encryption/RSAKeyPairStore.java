package com.key.password_manager.encryption;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.key.password_manager.keypair.KeyPairFactory;
import com.key.password_manager.keypair.PrivatePublicKeyPair;

/**
 * Maps User ID against a RSA key pair. If a key pair not available for a user a fresh key pair is
 * created using {@link KeyPairFactory}.
 */
@Service
public class RSAKeyPairStore {

	@Autowired
	private KeyPairFactory keyPairFactory;

	private Logger LOG = LoggerFactory.getLogger(RSAKeyPairStore.class);

	private HashMap<Long, PrivatePublicKeyPair> rsaKeyRegistry = new HashMap<>();

	/**
	 * If a key pair is not present a new key pair is generated via {@link KeyPairFactory}, else the
	 * call returns.
	 * 
	 * @param userId : User ID against which a RSA key pair is issued.
	 */
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

	/**
	 * If the a key pair is present for a user it is returned else a new key pair is issued using
	 * {@link #register(Long)} for the user and that is returned.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public PrivatePublicKeyPair getRSAKeyPair(Long userId) throws Exception {
		if (rsaKeyRegistry.containsKey(userId)) {
			return rsaKeyRegistry.get(userId);
		}
		register(userId);
		return getRSAKeyPair(userId);
	}
}
