package com.example.autorizationmantenimientos;

import com.example.autorizationmantenimientos.model.AuditLog;
import com.example.autorizationmantenimientos.model.User;
import com.example.autorizationmantenimientos.repository.AuditLogRepository;
import com.example.autorizationmantenimientos.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepo;
    private final UserRepository userRepository;

    public void audit(
            String action,
            Object data,
            User user
    ) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            auditLogRepo.save(
                    AuditLog
                            .builder()
                            .userId(user.getId())
                            .action(action)
                            .data_json(objectMapper.writeValueAsString(data))
                            .date(LocalDateTime.now())
                            .build()
            );
        } catch (JsonProcessingException e) {
            auditLogRepo.save(
                    AuditLog
                            .builder()
                            .userId(user.getId())
                            .action(action)
                            .data_json(data.toString())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
    }

    public void audit(
            String action,
            Object data
    ) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findOneByEmail(userEmail).orElseThrow();
        audit(action, data, user);
    }

}
