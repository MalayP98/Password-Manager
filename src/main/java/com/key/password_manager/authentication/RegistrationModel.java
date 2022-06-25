package com.key.password_manager.authentication;

public class RegistrationModel extends AuthenticationModel {

    private String confirmationPassword;

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }
}
