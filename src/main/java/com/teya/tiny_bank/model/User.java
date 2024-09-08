package com.teya.tiny_bank.model;

import com.teya.tiny_bank.model.enums.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private UUID id;

    private String name;

    private String surname;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @NotNull
    private LocalDateTime dateCreated;

    @NotNull
    private LocalDateTime dateUpdated;
}