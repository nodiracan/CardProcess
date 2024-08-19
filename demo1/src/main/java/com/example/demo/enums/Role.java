package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String name;

    public static Role getByName(String roleName) {
        if (Objects.isNull(roleName)) return Role.USER;
        for (Role value : Role.values()) {
            if (value.name.equalsIgnoreCase(roleName)) return value;
        }
        throw new IllegalArgumentException("Invalid roleName : " + roleName);
    }
}
