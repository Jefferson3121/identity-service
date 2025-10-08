package com.identity_service.dto;

import com.identity_service.model.UsersTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestTokenDTO(@NotNull Integer id, @NotBlank String email, @NotNull UsersTypes userType) {}
