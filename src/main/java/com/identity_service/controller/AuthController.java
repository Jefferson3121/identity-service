package com.identity_service.controller;

import com.identity_service.dto.request.LoginRequestDTO;
import com.identity_service.dto.response.TokenResponseDTO;
import com.identity_service.dto.request.UserRequestDTO;
import com.identity_service.service.AuthService;
import com.identity_service.security.TokenManager;
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
    private final TokenManager tokenManager;



    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
       String token = tokenManager.generateToken( authService.login(loginRequestDTO));
        return ResponseEntity.status(HttpStatus.OK).body(new TokenResponseDTO(token));
    }


    @PostMapping("/register-admin")
    public ResponseEntity<TokenResponseDTO> registerAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO){

       TokenResponseDTO token = authService.registerAdmin(userRequestDTO);

       return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }



    @PostMapping("/register-employee")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody UserRequestDTO userRequestDTO){
        authService.registerEmployee(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Empleado registrado");
    }
}
