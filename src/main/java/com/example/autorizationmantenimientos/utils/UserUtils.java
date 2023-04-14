package com.example.autorizationmantenimientos.utils;

import com.example.autorizationmantenimientos.model.User;

import java.util.Objects;

public class UserUtils {
    public static void checkUserRole(User user, RoleValues role) throws Exception {
        if (user.getRoles().stream().noneMatch(x -> Objects.equals(x.getName(), role.toString())))
            throw new Exception("you do not have permission to authorize queries");
    }
}
