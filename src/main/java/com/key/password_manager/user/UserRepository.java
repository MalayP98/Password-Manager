package com.key.password_manager.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public User findUserByEmailAndIsEnabled(String email, Boolean isEnabled);

}
