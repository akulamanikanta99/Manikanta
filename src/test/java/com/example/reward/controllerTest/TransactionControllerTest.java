package com.example.reward.controllerTest;

import com.example.reward.controller.TransactionController;
import com.example.reward.entity.CustomerTransaction;
import com.example.reward.service.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUpMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTransactionSuccessfully_whenValidTransactionProvided() {
        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setAmount(new BigDecimal("150.00"));
        transaction.setSpentDetails("Purchase at store");

        ResponseEntity<String> response = transactionController.addTransaction(transaction);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Transaction processed", response.getBody());
        verify(transactionService, times(1)).processTransaction(transaction);
    }
}
