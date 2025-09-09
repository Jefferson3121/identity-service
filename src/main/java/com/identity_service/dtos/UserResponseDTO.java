package com.identity_service.dtos;
import com.identity_service.model.UsersTypes;
import jakarta.validation.constraints.NotNull;

public record UserResponseDTO(int id, String name, int dni, String email,@NotNull(message = "El tipo de usuario esta llegando vacio a UserResponseDTO") UsersTypes userType) {
}
