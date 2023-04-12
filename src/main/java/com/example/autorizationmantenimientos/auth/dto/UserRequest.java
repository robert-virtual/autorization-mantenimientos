package com.example.autorizationmantenimientos.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRequest {
    private String name;
    private String lastname;
    private String password;
    private String email;

    private List<Integer> roles;
    private List<Integer> apps;
}
