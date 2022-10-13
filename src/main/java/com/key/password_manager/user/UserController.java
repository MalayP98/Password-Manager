package com.key.password_manager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api" + "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/publickey/{userId}")
    public ResponseEntity<String> publicKey(@PathVariable Long userId) throws Exception {
        return new ResponseEntity<String>(userService.publicKeyForUser(userId).getKey(),
                HttpStatus.OK);
    }
}
