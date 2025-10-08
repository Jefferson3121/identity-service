package com.identity_service.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequestDTO(@NotBlank(message = "no se ingreso el email actual") String currentEmail, @NotBlank(message = "No se ingreso el  nuevo email") String newEmail, @NotBlank(message = "No se ingreso ninguna cotraseña") String password){}
