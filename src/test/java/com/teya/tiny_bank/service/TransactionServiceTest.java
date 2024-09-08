package com.teya.tiny_bank.service;

import com.teya.tiny_bank.dao.TransactionDao;
import com.teya.tiny_bank.dao.UserDao;
import com.teya.tiny_bank.dto.TransactionDto;
import com.teya.tiny_bank.mapper.UserMapper;
import com.teya.tiny_bank.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserMapper mapper;

    @MockBean
    private TransactionDao transactionDao;

    @MockBean
    private AccountService accountService;

    @Test
    public void saveTransaction_Success() {
        TransactionDto transactionDto = new TransactionDto();
        long amount = 150L;
        transactionDto.setAmount(amount);
        UUID accountId = UUID.randomUUID();
        transactionDto.setDeposit(accountId);

        UUID transactionId = UUID.randomUUID();
        when(transactionDao.save(any(Transaction.class))).thenReturn(transactionId);

        assertEquals(transactionId, transactionService.saveTransaction(transactionDto));

        verify(transactionDao).save(any(Transaction.class));
        verify(accountService).deposit(accountId, amount);
        verify(accountService, never()).withdraw(any(UUID.class), anyLong());
    }

    @Test
    public void saveTransaction_Invlaid() {
        TransactionDto transactionDto = new TransactionDto();
        long amount = 150L;
        transactionDto.setAmount(amount);

        assertThrows(RuntimeException.class, () -> {
            transactionService.saveTransaction(transactionDto);
        });

        verify(accountService, never()).deposit(any(UUID.class), anyLong());
        verify(accountService, never()).withdraw(any(UUID.class), anyLong());
    }

    @Test
    public void saveTransaction_Negative() {
        TransactionDto transactionDto = new TransactionDto();
        long amount = -150L;
        transactionDto.setAmount(amount);
        UUID accountId = UUID.randomUUID();
        transactionDto.setDeposit(accountId);

        assertThrows(RuntimeException.class, () -> {
            transactionService.saveTransaction(transactionDto);
        });

        verify(accountService, never()).deposit(any(UUID.class), anyLong());
        verify(accountService, never()).withdraw(any(UUID.class), anyLong());
    }

    @Test
    public void getHistoryByAccount_Success() {
        UUID accountId = UUID.randomUUID();
        long amount = 250L;

        Transaction transaction = new Transaction();
        UUID transactionId = UUID.randomUUID();
        transaction.setId(transactionId);
        transaction.setAmount(amount);

        when(transactionDao.getHistoryByAccount(accountId)).thenReturn(List.of(transaction));

        List<TransactionDto> history = transactionService.getHistoryByAccount(accountId);
        assertEquals(1, history.size());
        assertNotNull(history.get(0));
        assertEquals(transactionId, history.get(0).getId());
        assertEquals(amount, history.get(0).getAmount());

        verify(transactionDao).getHistoryByAccount(accountId);
    }
}
