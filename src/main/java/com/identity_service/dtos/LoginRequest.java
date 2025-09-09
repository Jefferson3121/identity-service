package com.identity_service.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "El email esta vacio , no puede inicrar sesion sin un email") String email, @NotBlank(message = "No ingreso ninguna contraseña") String password) {
}
