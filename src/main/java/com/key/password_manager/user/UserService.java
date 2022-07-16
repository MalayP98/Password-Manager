package com.key.password_manager.user;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User loadedUser = userRepository.findUserByEmailAndIsEnabled(email, true);
        return new org.springframework.security.core.userdetails.User(loadedUser.getEmail(),
                loadedUser.getPassword(), new ArrayList<GrantedAuthority>());
    }

    public User registerUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User getUser(Long userId) {
        return userRepository.findByIdAndIsEnabled(userId, true);
    }

    public User getUser(String email) {
        return userRepository.findUserByEmailAndIsEnabled(email, true);
    }
}
