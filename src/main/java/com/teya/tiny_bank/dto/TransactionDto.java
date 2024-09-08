package com.teya.tiny_bank.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransactionDto {

    private UUID id;

    private UUID deposit;

    private UUID credit;

    @NonNull
    private Long amount;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}