package com.key.password_manager.credential;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CredentialRepository extends CrudRepository<Credential, Long> {

    List<Credential> findByUserId(Long userId);
}
