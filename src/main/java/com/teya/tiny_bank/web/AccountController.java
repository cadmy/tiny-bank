package com.teya.tiny_bank.web;

import com.teya.tiny_bank.model.enums.AccountStatus;
import com.teya.tiny_bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable UUID id, @RequestParam AccountStatus status) {
        accountService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<Long> getBalance(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    @GetMapping
    public ResponseEntity<List<UUID>> getAccountsIdsByUserId(@RequestParam UUID userId) {
        return ResponseEntity.ok(accountService.getAccountsIdsByUserId(userId));
    }
}