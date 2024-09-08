package com.teya.tiny_bank.web;

import com.teya.tiny_bank.model.enums.AccountStatus;
import com.teya.tiny_bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void changeStatus() throws Exception {
        UUID accountId = UUID.randomUUID();
        doNothing().when(accountService).changeStatus(accountId, AccountStatus.FROZEN);

        mockMvc.perform(put("/api/v1/account/" + accountId)
                        .param("status", AccountStatus.FROZEN.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void getBalance() throws Exception {
        UUID accountId = UUID.randomUUID();
        long amount = 340L;
        when(accountService.getBalance(accountId)).thenReturn(amount);

        mockMvc.perform(get("/api/v1/account/balance/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(amount)));
    }

    @Test
    public void getAccountsIdsByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        when(accountService.getAccountsIdsByUserId(userId)).thenReturn(List.of(accountId));

        mockMvc.perform(get("/api/v1/account")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"" + String.valueOf(accountId.toString()) + "\"]"));
    }
}