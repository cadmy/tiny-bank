package com.teya.tiny_bank.service;

import com.teya.tiny_bank.dto.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto create(UserDto userDto);

    void deactivate(UUID id);

    UserDto getById(UUID id);

    UUID createAccountForUser(UUID userId);
}