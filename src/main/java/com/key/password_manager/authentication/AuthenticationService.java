package com.key.password_manager.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.key.password_manager.security.JWTGenerator;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTGenerator jwtGenerator;

    public String registerUser(RegistrationModel registrationData) throws Exception {
        userService.registerUser(
                new User(registrationData.getEmail(), registrationData.getPassword()));
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
        return (userDetails.getPassword().equals(authModel.getPassword()));
    }
}
