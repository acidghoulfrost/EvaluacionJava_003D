package com.techstore.techstore_api.controller;

import com.techstore.techstore_api.dto.LoginRequest;
import com.techstore.techstore_api.dto.LoginResponse;
import com.techstore.techstore_api.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest loginRequest) {

        if ("admin@techstore.cl".equals(
                loginRequest.getUsername())
                &&
                "Admin1234".equals(
                        loginRequest.getPassword()
                )) {

            String token =
                    jwtUtil.generateToken(
                            loginRequest.getUsername()
                    );

            return new LoginResponse(
                    token,
                    "Bearer",
                    "3600"
            );
        }

        throw new RuntimeException(
                "Credenciales inválidas"
        );
    }
}
