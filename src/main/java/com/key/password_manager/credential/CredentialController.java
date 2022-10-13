package com.key.password_manager.credential;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.key.password_manager.constants.CredentialConstants;

@RestController
@RequestMapping("/api" + "/credentials")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> addCredential(@RequestBody Credential credential,
            @PathVariable Long userId, @RequestParam(required = true) String password) {
        return new ResponseEntity<String>(
                credentialService.addCredential(credential, userId, password), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Credential> getCredentials(@RequestParam Long credentialId,
            @RequestParam Long userId) {
        return new ResponseEntity<Credential>(
                credentialService.retriveCredential(userId, credentialId), HttpStatus.OK);
    }

    @GetMapping("/metadata")
    public ResponseEntity<String> defaultKeyMetadata() {
        return new ResponseEntity<String>("{\n salt : " + CredentialConstants.DEFAULT_SALT
                + ", iv : " + CredentialConstants.DEFAULT_IV + "}", HttpStatus.OK);
    }
}
