package com.key.password_manager.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.key.password_manager.key.AESKey;
import com.key.password_manager.key.Key;
import com.key.password_manager.keypair.PasswordEncryptionKeyPair;
import com.key.password_manager.utils.BaseModel;

@Entity
@Table(name = "users")
public class User extends BaseModel {

    private String username;

    private String email;

    private String phoneNumber;

    private boolean isEnabled;

    @OneToOne(targetEntity = AESKey.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "password_id")
    private Key password;

    @OneToOne(targetEntity = AESKey.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "enc_key_id")
    private Key encryptionKey;

    public User() {

    }

    public User(String email, PasswordEncryptionKeyPair keyPair) {
        this(email, keyPair.getPassword(), keyPair.getEncryptionKey());
    }

    public User(String email, Key password, Key encryptionKey) {
        this.email = email;
        this.password = password;
        this.encryptionKey = encryptionKey;
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Key getPassword() {
        return password;
    }

    public void setPassword(Key password) {
        this.password = password;
    }

    public Key getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(Key encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
}
