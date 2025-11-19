package com.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank(message = "El email esta vacio , no puede inicrar sesion sin un email") @Email(message = "Formato de email no válido")
                              String email, @NotBlank(message = "No ingreso ninguna contraseña") String password) {
}
