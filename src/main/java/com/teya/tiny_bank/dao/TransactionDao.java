package com.teya.tiny_bank.dao;

import com.teya.tiny_bank.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class TransactionDao {

    private Map<UUID, Transaction> transactions = new HashMap<>();
    private Map<UUID, List<Transaction>> transactionHistory = new HashMap<>();
    public UUID save(Transaction transaction) {
        UUID id = transaction.getId();

        transactions.put(id, transaction);

        if (Objects.nonNull(transaction.getDeposit())) {
            saveHistory(transaction.getDeposit(), transaction);
        }
        if (Objects.nonNull(transaction.getCredit())) {
            saveHistory(transaction.getCredit(), transaction);
        }

        return id;
    }

    private void saveHistory(UUID accountId, Transaction transaction) {
        if (transactionHistory.containsKey(accountId)) {
            List<Transaction> history = transactionHistory.get(accountId);
            List<Transaction> updatedHistory = new ArrayList<>(history);
            updatedHistory.add(transaction);
            transactionHistory.put(accountId, updatedHistory);
        } else {
            transactionHistory.put(accountId, List.of(transaction));
        }
    }

    public Transaction get(UUID id) {
        return transactions.get(id);
    }

    public List<Transaction> getHistoryByAccount(UUID accountId) {
        return transactionHistory.get(accountId);
    }
}