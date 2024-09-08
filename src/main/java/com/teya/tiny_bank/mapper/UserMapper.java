package com.teya.tiny_bank.mapper;

import com.teya.tiny_bank.dto.UserDto;
import com.teya.tiny_bank.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto mapToDto(User source);

    User mapToDao(UserDto source);
}