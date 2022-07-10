package com.key.password_manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    @Autowired
    private PasswordEncoder passwordEncoder;

}
