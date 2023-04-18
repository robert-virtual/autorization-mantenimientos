package com.example.autorizationmantenimientos.apps;

import com.example.autorizationmantenimientos.apps.dto.AppRequest;
import com.example.autorizationmantenimientos.model.App;
import com.example.autorizationmantenimientos.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppsService {
    private final AppRepository appRepo;

    public App create(AppRequest app) {
        return appRepo.save(App.builder().name(app.getName()).status(App.STATUS_ACTIVE).build());
    }

    public List<App> all() {
        return appRepo.findAll();
    }

    public Optional<App> byId(int appId) {
        Optional<App> app = appRepo.findById(appId);
        System.out.println(app);
        return app;
    }
}
