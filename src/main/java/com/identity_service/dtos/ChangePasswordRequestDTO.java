package com.identity_service.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(@NotBlank(message = "Email esta llegando vacio a el changePasswordRequestDTO") String email, @NotBlank(message = "NO llego la contraseña actual a changePasswordRequestDTO") String currentPasswor, @NotBlank(message = "No se ingreso la nueva contraseña para changePasswordRequestDTO") String newPassword) {
}
