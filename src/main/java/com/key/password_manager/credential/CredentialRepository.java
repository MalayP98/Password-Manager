package com.key.password_manager.credential;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    public List<Credential> findByUserId(Long userId);

    public Credential findByIdAndUserId(Long credentialId, Long userId);

    public List<Credential> findAllByUserId(Long userId, Pageable pageable);
}
