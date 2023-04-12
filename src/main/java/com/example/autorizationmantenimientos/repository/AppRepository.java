package com.example.autorizationmantenimientos.repository;

import com.example.autorizationmantenimientos.model.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppRepository extends JpaRepository<App,Integer> {
    List<App> findByNameIn(List<String> appsNames);
}
