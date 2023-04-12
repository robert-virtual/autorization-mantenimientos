package com.example.autorizationmantenimientos.repository;

import com.example.autorizationmantenimientos.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    List<Role> findByNameIn(List<String> names);
}
