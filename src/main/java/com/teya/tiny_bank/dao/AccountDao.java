package com.teya.tiny_bank.dao;

import com.teya.tiny_bank.model.Account;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AccountDao {

    private Map<UUID, Account> accounts = new HashMap<>();

    public UUID save(Account account) {
        UUID id = account.getId();
        accounts.put(id, account);
        return id;
    }

    public Account get(UUID id) {
        return accounts.get(id);
    }

    //the implementation of getAccountsIdsByUser() operation is determined by the task conditions
    //this implementation has slow performance
    public List<Account> getByUserId(UUID userId) {
        return accounts.values().stream().filter(a -> a.getUser().equals(userId)).collect(Collectors.toList());
    }
}