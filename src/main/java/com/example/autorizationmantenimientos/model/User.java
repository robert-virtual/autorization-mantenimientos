package com.example.autorizationmantenimientos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "t_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    public static final String STATUS_ACTIVE = "active";
    public static final String MISSING_USER_CREATOR_ROLE_ERROR = "You do not have the required role to create users. Contact with the person that created your user to update your roles";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String lastname;
    @Column(columnDefinition = "nvarchar(320) unique not null")
    private String email;
    @JsonIgnore
    private String password;
    @Column(columnDefinition = "datetime default (sysdatetime())")
    private LocalDateTime passwordUpdatedAt;
    private LocalDateTime lastLogin;
    @Column(columnDefinition = "datetime default (sysdatetime())")
    private LocalDateTime createdAt;
    private int failedLoginAttempts;
    @Column(columnDefinition = "nvarchar(50) default 'active'")
    private String status;
    @Column(columnDefinition = "bit default 1")
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "t_user_app",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "app_id")
    )
    private List<App> apps = new ArrayList<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(x -> new SimpleGrantedAuthority(x.getName())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
