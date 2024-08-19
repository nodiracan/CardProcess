package com.example.demo.service;

import com.example.demo.dto.authUser.AuthUserDTO;
import com.example.demo.dto.authUser.AuthUserLoginDTO;
import com.example.demo.dto.authUser.SessionDTO;
import com.example.demo.service.base.BaseService;

public interface AuthUserService extends BaseService {
    Long register(AuthUserDTO dto);

    SessionDTO login(AuthUserLoginDTO dto);
}
