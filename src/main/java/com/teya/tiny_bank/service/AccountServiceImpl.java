package com.teya.tiny_bank.service;

import com.teya.tiny_bank.dao.AccountDao;
import com.teya.tiny_bank.model.Account;
import com.teya.tiny_bank.model.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    @Override
    public UUID createAccountForUser(UUID userId) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setUser(userId);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(0L);
        account.setDateCreated(LocalDateTime.now());
        account.setDateUpdated(LocalDateTime.now());
        return accountDao.save(account);
    }

    @Override
    public void changeStatus(UUID id, AccountStatus status) {
        Account account = accountDao.get(id);
        if (Objects.isNull(account)) {
            throw new RuntimeException("Account not found");
        }
        account.setStatus(status);
        account.setDateUpdated(LocalDateTime.now());
        accountDao.save(account);
    }

    @Override
    public List<UUID> getAccountsIdsByUserId(UUID userId) {
        return accountDao.getByUserId(userId).stream().map(Account::getId).collect(Collectors.toList());
    }

    @Override
    public Long getBalance(UUID id) {
        Account account = accountDao.get(id);
        if (Objects.nonNull(account)) {
            if (AccountStatus.ACTIVE == account.getStatus()) {
                return account.getBalance();
            }
            throw new RuntimeException("Account is not active");
        }
        throw new RuntimeException("Account not found");
    }

    @Override
    //@Transactional
    public void deposit(UUID id, Long amount) {
        Account account = accountDao.get(id);
        if (Objects.isNull(account)) {
            throw new RuntimeException("Account not found");
        }
        if (AccountStatus.ACTIVE == account.getStatus()) {
            long result = account.getBalance() + amount;
            account.setBalance(result);
            account.setDateUpdated(LocalDateTime.now());
            accountDao.save(account);
        } else {
            throw new RuntimeException("Account is not active");
        }
    }

    @Override
    //@Transactional
    public void withdraw(UUID id, Long amount) {
        Account account = accountDao.get(id);
        if (Objects.isNull(account)) {
            throw new RuntimeException("Account not found");
        }
        if (account.getBalance() < amount) {
            throw new RuntimeException("Not enough money");
        }
        if (AccountStatus.ACTIVE == account.getStatus()) {
            long result = account.getBalance() - amount;
            account.setBalance(result);
            account.setDateUpdated(LocalDateTime.now());
            accountDao.save(account);
        } else {
            throw new RuntimeException("Account is not active");
        }
    }
}