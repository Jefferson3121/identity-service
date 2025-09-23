package com.identity_service.controller;

import com.identity_service.dtos.LoginRequestDTO;
import com.identity_service.dtos.TokenResponseDTO;
import com.identity_service.service.AuthService;
import com.identity_service.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

       String token = jwtService.generateToken( authService.login(loginRequestDTO));

        return ResponseEntity.status(HttpStatus.OK).body(new TokenResponseDTO(token));
    }
}
