package com.key.password_manager.credential;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

	public List<Credential> findByOwnerId(Long ownerId);

	public Credential findByIdAndOwnerId(Long credentialId, Long ownerId);

	public List<Credential> findAllByOwnerId(Long ownerId, Pageable pageable);
}
