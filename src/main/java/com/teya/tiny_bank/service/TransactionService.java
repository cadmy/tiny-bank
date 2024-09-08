package com.teya.tiny_bank.service;

import com.teya.tiny_bank.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    UUID saveTransaction(TransactionDto transactionDto);
    List<TransactionDto> getHistoryByAccount(UUID id);
}