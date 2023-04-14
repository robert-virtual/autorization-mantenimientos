package com.example.autorizationmantenimientos.apps;

import com.example.autorizationmantenimientos.apps.dto.AppRequest;
import com.example.autorizationmantenimientos.model.App;
import com.example.autorizationmantenimientos.dto.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("apps")
@RequiredArgsConstructor
public class AppsController {
    private final AppsService appsService;

    @GetMapping("all")
    public ResponseEntity<BasicResponse<List<App>>> create(
    ) {
        return ResponseEntity.ok(
                BasicResponse
                        .<List<App>>builder()
                        .data(appsService.all())
                        .build()
        );
    }

    @PostMapping("create")
    public ResponseEntity<BasicResponse<App>> create(
            @RequestBody AppRequest app
    ) {
        return ResponseEntity.ok(
                BasicResponse
                        .<App>builder()
                        .data(appsService.create(app))
                        .build()
        );
    }
}
