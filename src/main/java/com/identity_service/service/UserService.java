package com.identity_service.service;

import com.identity_service.dto.request.ChangeEmailRequestDTO;
import com.identity_service.dto.request.ChangedPasswordRequestDTO;
import com.identity_service.dto.response.UserResponseDTO;
import com.identity_service.exceptions.UserNotFoundException;
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
    public void deleteUser(int dni) {
        int deleted = userRepository.deleteByDni(dni);
        if (deleted == 0) {
            throw new UserNotFoundException("No se encontró el usuario con DNI: " + dni);
        }
    }

    @Transactional
    public void changeEmail(ChangeEmailRequestDTO changeEmailRequestDTO) {

         UserEntity userEntity = userRepository.findUserByEmail(changeEmailRequestDTO.currentEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(changeEmailRequestDTO.password(), userEntity.getPassword())){
            throw new BadCredentialsException("Contraseña invalida");
        }

        userEntity.setEmail(changeEmailRequestDTO.newEmail());
    }


    @Transactional
    public void changePassword(ChangedPasswordRequestDTO change){
        UserEntity userEntity = userRepository.findUserByEmail(change.email())
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(change.currentPasswor(), userEntity.getPassword())){
            throw new BadCredentialsException("Contraseña invalida");
        }
        userEntity.setPassword(passwordEncoder.encode(change.newPassword()));
    }



    @Transactional
    public void enableUser(String email){

        UserEntity userEntity = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        userEntity.setEnabled(true);
    }
}
