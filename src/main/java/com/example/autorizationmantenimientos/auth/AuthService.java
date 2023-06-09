package com.example.autorizationmantenimientos.auth;

import com.example.autorizationmantenimientos.AuditLogService;
import com.example.autorizationmantenimientos.auth.dto.AuthCredentials;
import com.example.autorizationmantenimientos.auth.dto.LoginResponse;
import com.example.autorizationmantenimientos.auth.dto.UpdateRoles;
import com.example.autorizationmantenimientos.auth.dto.UserRequest;
import com.example.autorizationmantenimientos.config.JwtService;
import com.example.autorizationmantenimientos.dto.BasicResponse;
import com.example.autorizationmantenimientos.model.App;
import com.example.autorizationmantenimientos.model.Role;
import com.example.autorizationmantenimientos.model.User;
import com.example.autorizationmantenimientos.repository.UserRepository;
import com.example.autorizationmantenimientos.utils.RoleValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final AuditLogService auditLogService;

    public LoginResponse login(AuthCredentials authCredentials) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authCredentials.getEmail(),
                        authCredentials.getPassword()
                )
        );
        User user = userRepo.findOneByEmail(authCredentials.getEmail()).orElseThrow();
        user.setLastLogin(LocalDateTime.now());
        User updatedUser = userRepo.save(user);
        auditLogService.audit("login user", updatedUser, User.builder().name("no user").build());
        return LoginResponse.builder().token(jwtService.generateToken(user)).user(user).build();
    }


    public User register(UserRequest user) throws Exception {
        if (
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().noneMatch(x -> Objects.equals(x.getAuthority(), RoleValues.USER_CREATOR.toString()))
        ) throw new Exception(User.MISSING_USER_CREATOR_ROLE_ERROR);
        Optional<User> userExist = userRepo.findOneByEmail(user.getEmail());
        if (userExist.isPresent()) throw new Exception("Email taken");
        User newUser = userRepo.save(
                User.builder()
                        .name(user.getName())
                        .lastname(user.getLastname())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .roles(user.getRoles().stream().map(x -> Role.builder().id(x).build()).collect(Collectors.toList()))
                        .apps(user.getApps().stream().map(x -> App.builder().id(x).build()).collect(Collectors.toList()))
                        .failedLoginAttempts(0)
                        .status(User.STATUS_ACTIVE)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .passwordUpdatedAt(LocalDateTime.now())
                        .email(user.getEmail())
                        .build()
        );
        auditLogService.audit("register user", newUser);
        return newUser;
    }

    public User getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findOneByEmail(email).orElseThrow();
    }

    public User removeRoles(int user_id, UpdateRoles updateRoles) throws Exception {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userRepo.findOneByEmail(userEmail).orElseThrow();
        User user = userRepo.findById(user_id).orElseThrow();
        if (loggedUser.getRoles().stream().noneMatch(r -> Objects.equals(r.getName(), RoleValues.USER_CREATOR.toString())))
            throw new Exception("You do not have the required role to update roles");

        Set<Integer> setOfRoles = user.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        updateRoles.getRoles().forEach(setOfRoles::remove);
        user.setRoles(setOfRoles.stream().map(
                roleId -> Role.builder().id(roleId).build()
        ).collect(Collectors.toList()));
        userRepo.save(user);
        auditLogService.audit("remove roles to user", loggedUser);
        return user;
    }

    public BasicResponse<User> addRoles(
            int user_id, UpdateRoles updateRoles
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userRepo.findOneByEmail(userEmail).orElseThrow();
        User user = userRepo.findById(user_id).orElseThrow();
        if (loggedUser.getRoles().stream().noneMatch(r -> Objects.equals(r.getName(), RoleValues.USER_CREATOR.toString())))
            return BasicResponse.<User>builder().error("You do not have the required role to update roles").build();
        Set<Integer> setOfRoles = user.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        setOfRoles.addAll(updateRoles.getRoles());
        user.setRoles(setOfRoles.stream().map(
                roleId -> Role.builder().id(roleId).build()
        ).collect(Collectors.toList()));
        userRepo.save(user);
        auditLogService.audit("add roles to user", user);
        return BasicResponse.<User>builder().data(user).build();
    }
}
