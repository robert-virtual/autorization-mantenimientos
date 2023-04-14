package com.example.autorizationmantenimientos.apps;

import com.example.autorizationmantenimientos.apps.dto.AppRequest;
import com.example.autorizationmantenimientos.model.App;
import com.example.autorizationmantenimientos.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
