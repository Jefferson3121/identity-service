package com.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangedPasswordRequestDTO(@NotBlank(message = "No se ingreso el email") @Email(message = "Formato de email no válido")
                                       String email, @NotBlank(message = "No se ingreso la contraseña") String currentPasswor, @NotBlank(message = "No se ingreso la nueva contraseña") @Size(min = 10, message = "La nueva contraseña debe tener al menos 10 caracteres")
                                       String newPassword) { }
