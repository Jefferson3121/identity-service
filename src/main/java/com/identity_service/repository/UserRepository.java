package com.identity_service.repository;

import com.identity_service.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA que maneja las operaciones de persistencia
 * relacionadas con la entidad {@link UserEntity}.
 *
 * Extiende JpaRepository para heredar operaciones CRUD básicas
 * y define consultas personalizadas por DNI y email.
 */


public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    int deleteByDni(int dni);
    Optional<UserEntity> findUserByEmail(String email);
    boolean existsByEmail(String email);
}
