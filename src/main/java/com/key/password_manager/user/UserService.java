package com.key.password_manager.user;

import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.key.Key;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RSAKeyPairStore rsaKeyPairStore;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User loadedUser = userRepository.findByEmailAndIsEnabled(email, true);
		return new org.springframework.security.core.userdetails.User(loadedUser.getEmail(),
				loadedUser.getPassword().getKey(), new ArrayList<GrantedAuthority>());
	}

	public User registerUser(User user) {
		User savedUser = userRepository.save(user);
		return savedUser;
	}

	public User getUser(Long userId) {
		User user = userRepository.findByIdAndIsEnabled(userId, true);
		if (Objects.isNull(user))
			throw new NullPointerException("No user found with id:" + userId);
		return user;
	}

	public boolean enableUserWithEmail(String email) {
		User user = getUserWithEnableStatus(email, false);
		if (Objects.isNull(user))
			return false;
		user.setEnabled(true);
		registerUser(user);
		return true;
	}

	public User getUser(String email) {
		return userRepository.findByEmail(email);
	}

	public User getUserWithEnableStatus(String email, boolean enableStatus) {
		return userRepository.findByEmailAndIsEnabled(email, enableStatus);
	}

	public User getUserWithRegistrationStatus(String email, boolean registrationStatus) {
		return userRepository.findByEmailAndIsRegistered(email, registrationStatus);
	}

	public Key publicKeyForUser(Long userId) throws Exception {
		return rsaKeyPairStore.getRSAKeyPair(userId).getPublicKey();
	}
}
