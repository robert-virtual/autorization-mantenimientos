package com.example.autorizationmantenimientos.config;

import com.example.autorizationmantenimientos.model.Role;
import com.example.autorizationmantenimientos.model.User;
import com.example.autorizationmantenimientos.repository.RoleRepository;
import com.example.autorizationmantenimientos.repository.UserRepository;
import com.example.autorizationmantenimientos.utils.RoleValues;
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
    @Value("${app.admin.lastname}")
    private String adminLastname;
    @Value("${app.admin.name}")
    private String adminName;
    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.password}")
    private String adminPassword;
    private List<Integer> roleIds;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoles();
        seedUsers();
    }



    public void seedRoles() {
        List<String> roleNames = List.of(
                RoleValues.QUERY_AUTHORIZER.toString(),
                RoleValues.QUERY_CREATOR.toString(),
                RoleValues.USER_CREATOR.toString()
        );
        List<Role> roles = roleRepo.findByNameIn(roleNames);
        if (new HashSet<>(roles.stream().map(Role::getName).collect(Collectors.toList())).containsAll(roleNames)) {
            roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            return;
        }
        roleNames = roleNames.stream().filter(
                roleName -> roles.stream().noneMatch(role -> Objects.equals(role.getName(), roleName))
        ).collect(Collectors.toList());
        roleIds = roleRepo.saveAll(roleNames.stream().map(
                roleName -> Role.builder().name(roleName).build()
        ).collect(Collectors.toList())).stream().map(Role::getId).collect(Collectors.toList());
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
                        .roles(roleIds.stream().map(x -> Role.builder().id(x).build()).collect(Collectors.toList()))
                        .status(User.STATUS_ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .enabled(true)
                        .build()
        );
    }
}
