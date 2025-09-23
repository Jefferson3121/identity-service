package com.identity_service.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(@NotBlank(message = "No se ingreso el email") String email, @NotBlank(message = "No se ingreso la contraseña") String currentPasswor, @NotBlank(message = "No se ingreso la nueva contraseña") String newPassword) { }
