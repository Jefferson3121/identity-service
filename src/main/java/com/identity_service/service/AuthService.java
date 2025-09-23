package com.identity_service.service;

import com.identity_service.dtos.LoginRequestDTO;
import com.identity_service.dtos.RequestTokenDTO;
import com.identity_service.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private  final AuthenticationManager authenticationManager;


    public RequestTokenDTO login(LoginRequestDTO loginRequestDTO) {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                );

        Authentication authResult = authenticationManager.authenticate(authInputToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        UserEntity user = (UserEntity) authResult.getPrincipal();

        return new RequestTokenDTO(user.getId(), user.getEmail(),user.getUserType());
    }
}
