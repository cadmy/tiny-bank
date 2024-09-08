package com.teya.tiny_bank.web;

import com.teya.tiny_bank.dto.TransactionDto;
import com.teya.tiny_bank.service.TransactionService;
import com.teya.tiny_bank.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void saveTransaction() throws Exception {
        UUID accountId = UUID.randomUUID();
        long amount = 6200L;

        UUID transactionId = UUID.randomUUID();
        when(transactionService.saveTransaction(any(TransactionDto.class))).thenReturn(transactionId);
        final String json = "{\"deposit\": \"" + accountId+ "\", \"amount\":\"" + amount + "\"}";

        mockMvc.perform(post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + transactionId + "\""));
    }

    @Test
    public void getTransactionHistory() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transactionId);
        long amount = 4200L;
        transactionDto.setAmount(amount);
        transactionDto.setDeposit(accountId);

        when(transactionService.getHistoryByAccount(accountId)).thenReturn(List.of(transactionDto));

        String resultJson = "[{\"id\":\"" + transactionId + "\",\"deposit\":\"" + accountId + "\",\"credit\":null,\"amount\":" + amount + ",\"dateCreated\":null,\"dateUpdated\":null}]";

        mockMvc.perform(get("/api/v1/transaction/history")
                        .param("accountId", accountId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(resultJson));
    }
}
