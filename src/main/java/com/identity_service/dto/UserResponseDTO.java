package com.identity_service.dto;
import com.identity_service.model.UsersTypes;

public record UserResponseDTO(int id, String name, int dni, String email, UsersTypes userType) {
}
