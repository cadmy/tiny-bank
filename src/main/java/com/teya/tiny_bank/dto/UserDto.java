package com.teya.tiny_bank.dto;

import com.teya.tiny_bank.model.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserDto {

    private UUID id;

    private UserStatus status;

    private String name;

    private String surname;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}