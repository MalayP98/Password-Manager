package com.key.password_manager.credential;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CredentialRepository extends CrudRepository<Credential, Long> {

    public List<Credential> findByUserId(Long userId);

    public Credential findByIdAndUserId(Long credentialId, Long userId);
}
