package com.key.password_manager.credential;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import com.key.password_manager.user.User;
import com.key.password_manager.utils.BaseModel;

@Entity
public class Credential extends BaseModel {

    private String username;

    private String email;

    private String password;

    private String associatedPhoneNumber;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinTable(name = "user_cred", joinColumns = @JoinColumn(name = "cred_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAssociatedPhoneNumber() {
        return associatedPhoneNumber;
    }

    public void setAssociatedPhoneNumber(String associatedPhoneNumber) {
        this.associatedPhoneNumber = associatedPhoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
