package com.identity_service.service;

import com.identity_service.dto.*;
import com.identity_service.exceptions.UserAlreadyExistsException;
import com.identity_service.infrastructure.mapper.UserMapper;
import com.identity_service.model.UserEntity;
import com.identity_service.model.UsersTypes;
import com.identity_service.repository.UserRepository;
import com.identity_service.security.TokenManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private  final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Transactional
   public TokenResponseDTO registerAdmin(UserRequestDTO userRequestDTO){

        UserEntity userEntity = userMapper.toUserEntity(userRequestDTO);

        userEntity.setUserType(UsersTypes.ADMIN);
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setEnabled(true);

        try {
            userRepository.save(userEntity);
            return new TokenResponseDTO(tokenManager.generateToken(new RequestTokenDTO(userEntity.getId(), userEntity.getEmail(),userEntity.getUserType())));
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyExistsException("Usuario ya registrado");
        }
   }


   @Transactional
   public void registerEmployee(UserRequestDTO userRequestDTO){
        UserEntity userEntity = userMapper.toUserEntity(userRequestDTO);
        userEntity.setUserType(UsersTypes.EMPLOYEE);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

       try {
           userRepository.save(userEntity);
       } catch (DataIntegrityViolationException ex) {
           throw new UserAlreadyExistsException("Usuario ya registrado");
       }
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
