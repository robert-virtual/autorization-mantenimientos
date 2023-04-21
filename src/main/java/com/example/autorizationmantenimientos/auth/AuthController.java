package com.example.autorizationmantenimientos.auth;

import com.example.autorizationmantenimientos.auth.dto.AuthCredentials;
import com.example.autorizationmantenimientos.auth.dto.LoginResponse;
import com.example.autorizationmantenimientos.auth.dto.UpdateRoles;
import com.example.autorizationmantenimientos.auth.dto.UserRequest;
import com.example.autorizationmantenimientos.dto.BasicResponse;
import com.example.autorizationmantenimientos.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/user")
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(
                authService.getUser()
        );
    }

    @PutMapping("/user/{user_id}/roles")
    public ResponseEntity<BasicResponse<User>> addRoles(@PathVariable int user_id, @RequestBody UpdateRoles updateRoles) {
        return ResponseEntity.ok(
                authService.addRoles(user_id, updateRoles)
        );
    }

    @GetMapping("/check-token")
    public ResponseEntity<Boolean> checkToken() {
        return ResponseEntity.ok(
                true
        );
    }

    @PostMapping("/register")
    public ResponseEntity<BasicResponse<User>> register(
            @RequestBody UserRequest user
    ) {
        try {
            return ResponseEntity.ok(
                    BasicResponse.<User>builder()
                            .data(authService.register(user))
                            .build()
            );
        } catch (Exception e) {
            log.error("Register error: %s", e.getMessage());
            return new ResponseEntity<>(
                    BasicResponse.<User>builder()
                            .error(e.getMessage())
                            .build(), HttpStatus.BAD_REQUEST
            );

        }
    }

    @PostMapping("/login")
    public ResponseEntity<BasicResponse<LoginResponse>> login(
            @RequestBody AuthCredentials authCredentials
    ) {
        return ResponseEntity.ok(
                BasicResponse.<LoginResponse>builder().data(authService.login(authCredentials)).build()
        );
    }

}
