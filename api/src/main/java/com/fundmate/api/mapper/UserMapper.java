package com.fundmate.api.mapper;

import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    User toEntity(UserRequest userRequest);
}
