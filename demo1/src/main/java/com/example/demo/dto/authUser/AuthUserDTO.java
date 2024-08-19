package com.example.demo.dto.authUser;

import com.example.demo.enums.Role;

public record AuthUserDTO(String username, String password, Role role) {
}
