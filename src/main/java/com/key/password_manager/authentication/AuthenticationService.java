package com.key.password_manager.authentication;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// import com.key.password_manager.encryption.EncryptionService;
import com.key.password_manager.security.JWTGenerator;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(AuthenticationModel registrationData) throws Exception {
        if (alreadyExist(registrationData.getEmail())) {
            throw new Exception("User already exists.");
        }
        User user = new User(registrationData.getEmail(),
                passwordEncoder.encode(registrationData.getPassword()));
        userService.registerUser(user);
        return jwtGenerator.generate(registrationData.getEmail());
    }

    public String loginUser(AuthenticationModel authModel) throws Exception {
        if (authenticate(authModel)) {
            return jwtGenerator.generate(authModel.getEmail());
        }
        throw new UsernameNotFoundException(
                "No user by email " + authModel.getEmail() + " exists.");
    }

    private Boolean authenticate(AuthenticationModel authModel) {
        UserDetails userDetails = userService.loadUserByUsername(authModel.getEmail());
        return passwordEncoder.matches(userDetails.getPassword(), authModel.getPassword());
    }

    private Boolean alreadyExist(String email) {
        return Objects.nonNull(userService.getUser(email));
    }
}
