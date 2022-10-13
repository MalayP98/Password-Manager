package com.key.password_manager.authentication;

import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.key.password_manager.user.UserService;

@Component
@Scope("singleton")
public class RegistrationVerification {

    private final String STRONG_PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;_',?/*~$^+=<>]).{8,20}$";

    private final Pattern PATTERN = Pattern.compile(STRONG_PASSWORD_REGEX);

    @Autowired
    private UserService userService;

    private Boolean userAlreadyExists(String email) throws Exception {
        return (Objects.nonNull(email) && Objects.nonNull(userService.getUser(email)));
    }

    private Boolean isPasswordStrong(String password) {
        return (Objects.nonNull(password) && PATTERN.matcher(password).matches());
    }

    public void isRegistrationValid(AuthenticationModel registrationData) throws Exception {
        if (userAlreadyExists(registrationData.getEmail())) {
            throw new Exception(
                    "User with email " + registrationData.getEmail() + " already exists.");
        }
        if (!isPasswordStrong(registrationData.getPassword())) {
            throw new Exception("Password is weak.");
        }
    }
}
