package com.example.autorizationmantenimientos.auth;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public LoginResponse login(AuthCredentials authCredentials) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authCredentials.getEmail(),
                        authCredentials.getPassword()
                )
        );
        User user = userRepo.findOneByEmail(authCredentials.getEmail()).orElseThrow();
        user.setLastLogin(LocalDateTime.now());
        userRepo.save(user);
        return LoginResponse.builder().token(jwtService.generateToken(user)).user(user).build();
    }


    public User register(UserRequest user) throws Exception {
        if (
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().noneMatch(x -> Objects.equals(x.getAuthority(), RoleValues.USER_CREATOR.toString()))
        ) throw new Exception(User.MISSING_USER_CREATOR_ROLE_ERROR);
        Optional<User> userExist = userRepo.findOneByEmail(user.getEmail());
        if (userExist.isPresent()) throw new Exception("Email taken");
        return userRepo.save(
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
    }

    public User getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findOneByEmail(email).orElseThrow();
    }

    public BasicResponse<User> addRoles(int user_id, UpdateRoles updateRoles) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> loggedUser = userRepo.findOneByEmail(userEmail);
        if (loggedUser.isEmpty()) return BasicResponse.<User>builder().error("Bad Request").build();
        Optional<User> user = userRepo.findById(user_id);
        if (user.isEmpty()) return BasicResponse.<User>builder().error("User not found").build();
        if (loggedUser.get().getRoles().stream().noneMatch(r -> Objects.equals(r.getName(), RoleValues.USER_CREATOR.toString())))
            return BasicResponse.<User>builder().error("You do not have the required role to update roles").build();
        Set<Integer> setOfRoles = user.get().getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        setOfRoles.addAll(updateRoles.getRoles());
        user.map(user_ -> {
            user_.setRoles(setOfRoles.stream().map(
                    roleId -> Role.builder().id(roleId).build()
            ).collect(Collectors.toList()));
            return userRepo.save(user_);
        });
        return BasicResponse.<User>builder().data(user.get()).build();
    }
}
