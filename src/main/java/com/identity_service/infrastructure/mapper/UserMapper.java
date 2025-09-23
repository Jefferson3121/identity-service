package com.identity_service.infrastructure.mapper;

import com.identity_service.dtos.UserRequestDTO;
import com.identity_service.dtos.UserResponseDTO;
import com.identity_service.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
            )
public interface UserMapper {

   UserEntity toUserEntity(UserRequestDTO userRequestDTO);

   UserResponseDTO toUserResponseDTO(UserEntity userEntity);

   List<UserResponseDTO> toUserResponseDTOList(List<UserEntity> userEntities);
}
