package com.teya.tiny_bank.service;

import com.teya.tiny_bank.model.enums.AccountStatus;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    UUID createAccountForUser(UUID userId);

    void changeStatus(UUID id, AccountStatus status);

    List<UUID> getAccountsIdsByUserId(UUID userId);

    Long getBalance(UUID id);

    void deposit(UUID uuid, Long amount);

    void withdraw(UUID uuid, Long amount);
}