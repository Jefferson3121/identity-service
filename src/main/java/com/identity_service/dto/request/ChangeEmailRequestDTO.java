package com.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequestDTO(@NotBlank(message = "no se ingreso el email actual") @Email(message = "Formato de email no válido")
                                    String currentEmail, @NotBlank(message = "No se ingreso el  nuevo email") String newEmail, @NotBlank(message = "No se ingreso ninguna cotraseña") String password){}
