package com.identity_service.service;

import com.identity_service.dto.*;
import com.identity_service.model.UserEntity;
import com.identity_service.repository.UserRepository;
import com.identity_service.security.TokenManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private  final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;


    @Transactional
   public TokenResponseDTO register(UserEntity userEntity){

        if (!userRepository.existsByEmail(userEntity.getEmail())){
            throw new UsernameNotFoundException("Usuario con ese email no existe");
        }

        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);

       UserEntity user = userRepository.save(userEntity);

       return new TokenResponseDTO(tokenManager.generateToken(new RequestTokenDTO(user.getId(), user.getEmail(),user .getUserType())));
   }


    public RequestTokenDTO login(LoginRequestDTO loginRequestDTO) {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                );

        Authentication authResult = authenticationManager.authenticate(authInputToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        UserEntity user = (UserEntity) authResult.getPrincipal();
        user.setStateLogin(true);

        return new RequestTokenDTO(user.getId(), user.getEmail(),user.getUserType());
    }
}
