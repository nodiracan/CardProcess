package com.example.demo.service.impl;

import com.example.demo.constant.StaticMessages;
import com.example.demo.dto.authUser.AuthUserDTO;
import com.example.demo.dto.authUser.AuthUserLoginDTO;
import com.example.demo.dto.authUser.SessionDTO;
import com.example.demo.entity.AuthUser;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.AuthUserMapper;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.service.AuthUserService;
import com.example.demo.service.base.AbstractService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl extends AbstractService<AuthUserRepository, AuthUserMapper> implements AuthUserService {


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthUserRepository repository, AuthUserMapper mapper, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtService jwtService) {
        super(repository, mapper);
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public Long register(AuthUserDTO dto) {
        Optional<AuthUser> authUser = repository.findByUsernameAndIsActive(dto.username(), Boolean.TRUE);
        if (authUser.isPresent()){
            throw new BadRequestException(StaticMessages.USER_ALREADY_EXIST + dto.username());
        }

        AuthUser user = AuthUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .isActive(true)
                .role(dto.role())
                .build();
        return repository.save(user).getId();
    }

    @Override
    public SessionDTO login(AuthUserLoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        AuthUser user = repository.findByUsername(dto.username())
                .orElseThrow(() -> new NotFoundException(StaticMessages.USER_NOT_FOUND + dto.username()));
        System.out.println("user.getUsername() = " + user.getUsername());
        return jwtService.createSessionDTO(user);
    }

    public  static String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
