package com.teya.tiny_bank.service;

import com.teya.tiny_bank.dao.AccountDao;
import com.teya.tiny_bank.model.Account;
import com.teya.tiny_bank.model.enums.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountDao accountDao;

    @Test
    public void createAccountForUser_Success() {
        when(accountDao.save(any(Account.class))).thenReturn(UUID.randomUUID());
        UUID userId = UUID.randomUUID();
        assertNotNull(accountService.createAccountForUser(userId));
        verify(accountDao).save(any(Account.class));
    }

    @Test
    public void changeStatus_Success() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        when(accountDao.get(accountId)).thenReturn(account);
        when(accountDao.save(any(Account.class))).thenReturn(accountId);

        accountService.changeStatus(accountId, AccountStatus.FROZEN);

        verify(accountDao).get(accountId);
        verify(accountDao).save(any(Account.class));
    }

    @Test
    public void changeStatus_AccountNotFound() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        when(accountDao.get(accountId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
                accountService.changeStatus(accountId, AccountStatus.FROZEN);
            });

        verify(accountDao).get(accountId);
    }

    @Test
    public void getAccountsIdsByUserId_Success() {
        UUID userId = UUID.randomUUID();

        Account account1 = new Account();
        UUID accountId1 = UUID.randomUUID();
        account1.setId(accountId1);

        Account account2 = new Account();
        UUID accountId2 = UUID.randomUUID();
        account2.setId(accountId2);

        when(accountDao.getByUserId(userId)).thenReturn(List.of(account1, account2));

        List<UUID> accountIds = accountService.getAccountsIdsByUserId(userId);

        assertTrue(accountIds.containsAll(List.of(accountId1, accountId2)));
        verify(accountDao).getByUserId(userId);
    }

    @Test
    public void getBalance_Success() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        long balance = 500L;
        account.setBalance(balance);
        when(accountDao.get(accountId)).thenReturn(account);

        Long result = accountService.getBalance(accountId);
        assertEquals(balance, result);
        verify(accountDao).get(accountId);
    }

    @Test
    public void getBalance_AccountNotActive() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setStatus(AccountStatus.CLOSED);
        long balance = 500L;
        account.setBalance(balance);
        when(accountDao.get(accountId)).thenReturn(account);
        assertThrows(RuntimeException.class, () -> {
                    accountService.getBalance(accountId);
                });
        verify(accountDao).get(accountId);
    }

    @Test
    public void getBalance_NotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountDao.get(accountId)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> {
                accountService.getBalance(accountId);
            });
        verify(accountDao).get(accountId);
    }

    @Test
    public void deposit_Success() {
        UUID accountId = UUID.randomUUID();
        long amount = 1200L;

        Account account = new Account();
        account.setId(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        long balance = 800L;
        account.setBalance(balance);

        when(accountDao.get(accountId)).thenReturn(account);

        accountService.deposit(accountId, amount);

        verify(accountDao).get(accountId);
    }

    @Test
    public void deposit_AccountNotActive() {
        UUID accountId = UUID.randomUUID();
        long amount = 1200L;

        Account account = new Account();
        account.setId(accountId);
        account.setStatus(AccountStatus.FROZEN);
        long balance = 800L;
        account.setBalance(balance);

        when(accountDao.get(accountId)).thenReturn(account);

        assertThrows(RuntimeException.class, () -> {
                    accountService.deposit(accountId, amount);
                });

        verify(accountDao).get(accountId);
    }

    @Test
    public void withdraw_Success() {
        UUID accountId = UUID.randomUUID();
        long amount = 1200L;

        Account account = new Account();
        account.setId(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        long balance = 7300L;
        account.setBalance(balance);

        when(accountDao.get(accountId)).thenReturn(account);

        accountService.withdraw(accountId, amount);

        verify(accountDao).get(accountId);
    }

    @Test
    public void withdraw_Not_Enough_Money() {
        UUID accountId = UUID.randomUUID();
        long amount = 1200L;

        Account account = new Account();
        account.setId(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        long balance = 400L;
        account.setBalance(balance);

        when(accountDao.get(accountId)).thenReturn(account);
        assertThrows(RuntimeException.class, () -> {
                    accountService.withdraw(accountId, amount);
                });
        verify(accountDao).get(accountId);
    }
}