package com.key.password_manager.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public User findByEmail(String email);

	public User findByEmailAndIsEnabled(String email, Boolean isEnabled);

	public User findByEmailAndIsRegistered(String email, Boolean isRegistered);

	public User findByIdAndIsEnabled(Long userId, Boolean isEnabled);
}
