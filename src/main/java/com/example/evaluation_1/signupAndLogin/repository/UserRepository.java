package com.example.evaluation_1.signupAndLogin.repository;

import com.example.evaluation_1.signupAndLogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 *  Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);     //Find User Using Email

}
