package com.example.autorizationmantenimientos.repository;

import com.example.autorizationmantenimientos.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
}
