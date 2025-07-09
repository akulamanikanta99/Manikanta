package com.example.reward.controllerTest;

import com.example.reward.controller.TransactionController;
import com.example.reward.entity.CustomerTransaction;
import com.example.reward.service.transaction.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setTransactionId(1L);
        transaction.setAmount(new BigDecimal("150.00"));
        transaction.setSpentDetails("Purchase at store");
        transaction.setDate(LocalDateTime.of(2024, 11, 1, 10, 30));

        doNothing().when(transactionService).processTransaction(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Transaction processed"));

        verify(transactionService).processTransaction(transaction);
    }
}
