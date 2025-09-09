package com.identity_service.service;

import com.identity_service.dtos.LoginRequest;
import com.identity_service.dtos.TokenResponse;
import com.identity_service.model.UserEntity;
import com.identity_service.repository.UserRepository;
import com.identity_service.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;



    public TokenResponse login(@Valid LoginRequest loginRequest){

        // Aquí validamos credenciales con Spring Security
//        Authentication authentication = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );

        UserEntity userEntity = userRepository.findUserByEmail(loginRequest.email())
                .orElseThrow(()-> new RuntimeException("El usuario no existe"));


        if (!userEntity.getPassword().equals(loginRequest.password())){
            throw new RuntimeException("La contraseña es incorrecta");
        }

        return new TokenResponse(jwtService.generateToken(userEntity));
    }
}
