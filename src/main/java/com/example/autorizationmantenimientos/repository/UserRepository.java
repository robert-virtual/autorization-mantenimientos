package com.example.autorizationmantenimientos.repository;

import com.example.autorizationmantenimientos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findOneByEmail(String email);
}
