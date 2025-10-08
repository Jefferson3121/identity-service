package com.identity_service.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank(message = "El email esta vacio , no puede inicrar sesion sin un email") String email, @NotBlank(message = "No ingreso ninguna contraseña") String password) {
}
