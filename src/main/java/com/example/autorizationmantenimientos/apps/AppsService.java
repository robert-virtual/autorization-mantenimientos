package com.example.autorizationmantenimientos.apps;

import com.example.autorizationmantenimientos.AuditLogService;
import com.example.autorizationmantenimientos.apps.dto.AppRequest;
import com.example.autorizationmantenimientos.dto.AddUserRequest;
import com.example.autorizationmantenimientos.model.App;
import com.example.autorizationmantenimientos.model.User;
import com.example.autorizationmantenimientos.repository.AppRepository;
import com.example.autorizationmantenimientos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppsService {
    private final AppRepository appRepo;
    private final UserRepository userRepo;
    private final AuditLogService auditLogService;

    public App create(AppRequest app) {
        App newApp = appRepo.save(
                App.builder()
                        .name(app.getName())
                        .endpoint(app.getEndpoint())
                        .status(App.STATUS_ACTIVE)
                        .build()
        );
        auditLogService.audit("create app", newApp);
        return newApp;
    }

    public List<App> all() {
        return appRepo.findAll();
    }

    public Optional<App> byId(int appId) {
        Optional<App> app = appRepo.findById(appId);
        System.out.println(app);
        return app;
    }

    public App addUser(AddUserRequest addUserRequest) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findOneByEmail(userEmail).orElseThrow();
        App app = appRepo.findById(addUserRequest.getApp_id()).orElseThrow();
        user.getApps().add(app);
        userRepo.save(user);
        auditLogService.audit("add user to app", user);
        return app;
    }
}
