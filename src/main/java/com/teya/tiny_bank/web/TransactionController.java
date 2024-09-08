package com.teya.tiny_bank.web;

import com.teya.tiny_bank.dto.TransactionDto;
import com.teya.tiny_bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<UUID> saveTransaction(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.saveTransaction(transactionDto));
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionDto>> getTransactionHistory(@RequestParam UUID accountId) {
        return ResponseEntity.ok(transactionService.getHistoryByAccount(accountId));
    }
}