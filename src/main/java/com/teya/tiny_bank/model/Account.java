package com.teya.tiny_bank.model;

import com.teya.tiny_bank.model.enums.AccountStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    private UUID id;

    private Long balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private UUID user;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}