package com.registry.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(@Param("username") String username);
}
