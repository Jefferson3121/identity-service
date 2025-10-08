package com.identity_service.controller;

import com.identity_service.dto.LoginRequestDTO;
import com.identity_service.dto.TokenResponseDTO;
import com.identity_service.dto.UserRequestDTO;
import com.identity_service.infrastructure.mapper.UserMapper;
import com.identity_service.model.UserEntity;
import com.identity_service.model.UsersTypes;
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
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
       String token = tokenManager.generateToken( authService.login(loginRequestDTO));

        return ResponseEntity.status(HttpStatus.OK).body(new TokenResponseDTO(token));
    }


    @PostMapping("/register-admin")
    public ResponseEntity<TokenResponseDTO> registerAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO){



        UserEntity user = userMapper.toUserEntity(userRequestDTO);
        user.setUserType(UsersTypes.ADMIN);
        user.setEnabled(true);

       TokenResponseDTO token = authService.register(user);

       return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }



    @PostMapping("/register-employee")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody UserRequestDTO userRequestDTO){

        TokenResponseDTO token =  authService.register(userMapper.toUserEntity(userRequestDTO));

        return ResponseEntity.ok("Usuario habilitado con exito");

    }
}
