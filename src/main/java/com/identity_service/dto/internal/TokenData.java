package com.identity_service.dto.internal;

import com.identity_service.model.UsersTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO interno usado para solicitar la generación de un token JWT.
 * No se expone en los endpoints públicos.
 */

public record TokenData(@NotNull Integer id, @NotBlank String email, @NotNull UsersTypes userType) {}
