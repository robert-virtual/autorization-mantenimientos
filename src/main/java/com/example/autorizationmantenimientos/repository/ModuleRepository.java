package com.example.autorizationmantenimientos.repository;

import com.example.autorizationmantenimientos.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module,Integer> {
    List<Module> findByNameIn(List<String> names);
}
