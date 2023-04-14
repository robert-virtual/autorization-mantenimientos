package com.example.autorizationmantenimientos.utils;

public enum RoleValues {
    QUERY_AUTHORIZER,
    QUERY_CREATOR,
    USER_CREATOR;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
