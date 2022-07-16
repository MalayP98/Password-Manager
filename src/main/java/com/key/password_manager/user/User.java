package com.key.password_manager.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.key.password_manager.utils.BaseModel;

@Entity
@Table(name = "users")
public class User extends BaseModel {

    private String username;

    private String email;

    private String phoneNumber;

    @JsonProperty(access = Access.READ_ONLY)
    private String password;

    private boolean isEnabled;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.isEnabled = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
