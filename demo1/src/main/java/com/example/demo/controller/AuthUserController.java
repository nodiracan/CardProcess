package com.example.demo.controller;
import com.example.demo.controller.base.AbstractController;
import com.example.demo.dto.authUser.AuthUserDTO;
import com.example.demo.dto.authUser.AuthUserLoginDTO;
import com.example.demo.dto.authUser.SessionDTO;
import com.example.demo.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.controller.base.AbstractController.PATH;

@Tag(name = "AuthUser")
@Slf4j
@RestController
@RequestMapping(PATH+"/auth")
public class AuthUserController extends AbstractController<AuthServiceImpl> {

    public AuthUserController(AuthServiceImpl service) {
        super(service);
    }


    @PostMapping(value = "/register")
    public ResponseEntity<Long> register( @RequestBody AuthUserDTO dto){
        log.info("REST: [POST] register() method  AuthUser : {}  ", dto.username());
        Long id = service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<SessionDTO> login(@RequestBody AuthUserLoginDTO dto){
        log.info("REST: [POST] login() method  AuthUser : {}  ", dto.username());
        SessionDTO dto1 = service.login(dto);
        return  ResponseEntity.ok(dto1);

    }

}
