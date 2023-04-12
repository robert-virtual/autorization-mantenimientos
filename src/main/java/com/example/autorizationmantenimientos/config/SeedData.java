package com.example.autorizationmantenimientos.config;

import com.example.autorizationmantenimientos.model.App;
import com.example.autorizationmantenimientos.model.Role;
import com.example.autorizationmantenimientos.model.User;
import com.example.autorizationmantenimientos.repository.AppRepository;
import com.example.autorizationmantenimientos.repository.RoleRepository;
import com.example.autorizationmantenimientos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SeedData {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final AppRepository appRepo;
    @Value("${app.admin.lastname}")
    private String adminLastname;
    @Value("${app.admin.name}")
    private String adminName;
    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.password}")
    private String adminPassword;
    private List<Integer> roleId;
    private List<Integer> apps;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoles();
        seedApps();
        seedUsers();
    }

    public void seedApps() {
        List<String> appsNames = List.of("ocb_example", "vaucher_example");
        List<App> appsFound = appRepo.findByNameIn(appsNames);
        if (new HashSet<>(appsFound.stream().map(App::getName).collect(Collectors.toList())).containsAll(appsNames)) {
            apps = appsFound.stream().map(App::getId).collect(Collectors.toList());
            return;
        }
        appsNames = appsNames.stream().filter(x -> appsFound.stream().noneMatch(y -> Objects.equals(y.getName(), x))).collect(Collectors.toList());
        roleId = appRepo.saveAll(appsNames.stream().map(x -> App.builder().name(x).build()).collect(Collectors.toList())).stream().map(App::getId).collect(Collectors.toList());
    }

    public void seedRoles() {
        List<String> roles = List.of(Role.QUERY_AUTHORIZER, Role.QUERY_CREATOR, Role.USER_CREATOR);
        List<Role> role = roleRepo.findByNameIn(roles);
        if (new HashSet<>(role.stream().map(Role::getName).collect(Collectors.toList())).containsAll(roles)) {
            roleId = role.stream().map(Role::getId).collect(Collectors.toList());
            return;
        }
        roles = roles.stream().filter(x -> role.stream().noneMatch(y -> Objects.equals(y.getName(), x))).collect(Collectors.toList());
        roleId = roleRepo.saveAll(roles.stream().map(x -> Role.builder().name(x).build()).collect(Collectors.toList())).stream().map(Role::getId).collect(Collectors.toList());
    }

    public void seedUsers() {
        Optional<User> user = userRepo.findOneByEmail(adminEmail);
        if (user.isPresent()) {
            return;
        }
        userRepo.save(
                User
                        .builder()
                        .name(adminName)
                        .lastname(adminLastname)
                        .email(adminEmail)
                        .password(new BCryptPasswordEncoder()
                                .encode(adminPassword))
                        .roles(roleId.stream().map(x -> Role.builder().id(x).build()).collect(Collectors.toList()))
                        .status(User.STATUS_ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .enabled(true)
                        .build()
        );
    }
}
