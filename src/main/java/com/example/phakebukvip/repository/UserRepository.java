package com.example.phakebukvip.repository;

import com.example.phakebukvip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByFullNameContainingIgnoreCase(String keyword);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String token);


}
