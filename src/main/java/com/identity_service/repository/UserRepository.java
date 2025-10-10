package com.identity_service.repository;

import com.identity_service.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Integer deleteByDni(int dni);
    Optional<UserEntity> findUserByEmail(String email);
    boolean existsByEmail(String email);
}
