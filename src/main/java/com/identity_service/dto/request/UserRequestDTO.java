  package com.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

  public record UserRequestDTO(@NotBlank(message = "Nombre llega vacio a UserRequestDTO") String name, @NotNull Integer dni, @NotBlank(message = "email llega vacio a UserRequestDTO") @Email(message = "Formato de email no válido")
  String email, @NotBlank(message = "contraseña llegavacia a UserRequestDTO") @Size(min = 10, message = "Contraseña debe ser superior a 10 caracteres") String password){}
