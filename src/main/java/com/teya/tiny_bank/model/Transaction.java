package com.teya.tiny_bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    private UUID id;

    @ManyToOne
    private UUID deposit;

    @ManyToOne
    private UUID credit;

    @NotNull
    private Long amount;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}