package com.identity_service.service;

import com.identity_service.dto.UserResponseDTO;
import com.identity_service.infrastructure.mapper.UserMapper;
import com.identity_service.model.UserEntity;
import com.identity_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {


    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAllUsers(){
        return   userMapper.toUserResponseDTOList(userRepository.findAll());
    }



    public Optional<UserResponseDTO> getUserByEmail(String email){
        return userRepository.findUserByEmail(email)
                .map(userEntity -> userMapper.toUserResponseDTO(userEntity))
                .or(()-> Optional.empty());
    }


    @Transactional
    public boolean deleteUser(int dni) {

        if (userRepository.deleteByDni(dni) > 0){
            return true;
        }

        return false;
    }



    @Transactional
    public boolean changeEmail(String currentEmail, String newEmail,String password) {

        UserEntity userEntity = userRepository.findUserByEmail(currentEmail).orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, userEntity.getPassword())){
            throw new BadCredentialsException("Contraseña invalida");
        }

        userEntity.setEmail(newEmail);
        userRepository.save(userEntity);
        return true;
    }


    @Transactional
    public boolean changePassword(String email, String currentPasswor,String newPassword){

        UserEntity userEntity = userRepository.findUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPasswor, userEntity.getPassword())){
            throw new BadCredentialsException("Contraseña invalida");
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
        return true;
    }


    @Transactional
    public void enableUser(String email){

        UserEntity userEntity = userRepository.findUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }
}
