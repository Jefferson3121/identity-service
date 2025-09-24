package com.identity_service.service;

import com.identity_service.dtos.UserResponseDTO;
import com.identity_service.infrastructure.mapper.UserMapper;
import com.identity_service.model.UserEntity;
import com.identity_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {


    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    public boolean addUser(UserEntity userEntity){
        if (userRepository.existsByEmail(userEntity.getUsername())){
            return false;
        }

        userRepository.save(userEntity);
        return true;
    }


    public List<UserResponseDTO> getAllUsers(){
        return   userMapper.toUserResponseDTOList(userRepository.findAll());
    }



    public Optional<UserResponseDTO> getUserByEmail(String email){
        return userRepository.findUserByEmail(email)
                .map(userEntity -> userMapper.toUserResponseDTO(userEntity))
                .or(()-> Optional.empty());
        //ojito aqui
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

        return userRepository.findUserByEmail(currentEmail)
                .map(userEntity -> {
                    if (!userEntity.getPassword().equalsIgnoreCase(password)){
                        return false;
                        //Se debe lanzar una excepcion porque el metodo es boolean y el cliente no sabe porque fallo
                    }

                    userEntity.setEmail(newEmail);
                    return true;
                })
                .orElse(false);
    }



    @Transactional
    public boolean changePassword(String email, String currentPasswor,String newPassword){

        return  userRepository.findUserByEmail(email)
                .map(userEntity -> {
                    if (!userEntity.getPassword().equalsIgnoreCase(currentPasswor)){
                        return false;
                    }

                    userEntity.setPassword(newPassword);
                    return true;
                }).orElse(false);
    }











}
