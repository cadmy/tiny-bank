package com.teya.tiny_bank.service;


import com.teya.tiny_bank.dao.TransactionDao;
import com.teya.tiny_bank.dto.TransactionDto;
import com.teya.tiny_bank.mapper.TransactionMapper;
import com.teya.tiny_bank.model.Transaction;
import com.teya.tiny_bank.util.TransactionUtil;
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
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;

    private final TransactionMapper transactionMapper;

    private final AccountService accountService;

    @Override
    //@Transactional
    public UUID saveTransaction(TransactionDto transactionDto) {
        if (!TransactionUtil.isValid(transactionDto)) {
            throw new RuntimeException("Transaction is invalid");
        }
        Transaction transaction = transactionMapper.mapToDao(transactionDto);
        UUID id = UUID.randomUUID();
        transaction.setDateCreated(LocalDateTime.now());
        transaction.setId(id);
        transaction.setDateUpdated(LocalDateTime.now());
        if (Objects.nonNull(transactionDto.getDeposit())) {
            accountService.deposit(transactionDto.getDeposit(), transactionDto.getAmount());
        }
        if (Objects.nonNull(transactionDto.getCredit())) {
            accountService.withdraw(transactionDto.getCredit(), transactionDto.getAmount());
        }
        return transactionDao.save(transaction);
    }

    @Override
    public List<TransactionDto> getHistoryByAccount(UUID accountId) {
        return transactionDao.getHistoryByAccount(accountId).stream().map(transactionMapper::mapToDto).collect(Collectors.toList());
    }
}