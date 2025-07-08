package com.example.reward.service.reward;

import com.example.reward.dto.RewardSummaryDTO;
import com.example.reward.dto.TransactionDTO;
import com.example.reward.entity.Customer;
import com.example.reward.entity.CustomerTransaction;
import com.example.reward.repository.CustomerRepository;
import com.example.reward.repository.CustomerTransactionRepository;
import com.example.reward.service.transaction.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RewardServiceTest {

    @InjectMocks
    private RewardServiceImpl rewardService;

    @Mock
    private CustomerTransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionService transactionService;

    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;

    private CustomerTransaction sampleTransaction;

    @Before
    public void setup() {
        customerId = 1L;
        startDate = LocalDate.of(2024, 11, 1);
        endDate = LocalDate.of(2024, 11, 30);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setName("Alice");

        sampleTransaction = new CustomerTransaction();
        sampleTransaction.setTransactionId(100L);
        sampleTransaction.setAmount(BigDecimal.valueOf(120.0));
        sampleTransaction.setDate(LocalDateTime.of(2024, 11, 10, 10, 30));
        sampleTransaction.setCustomer(customer);
    }

    @Test
    public void testGetRewardSummarySuccess() {
        when(transactionRepository.findTransactionsForCustomerBetweenDates(customerId, startDate, endDate))
                .thenReturn(List.of(sampleTransaction));

        when(transactionService.calculateRewardPoints(BigDecimal.valueOf(120.0)))
                .thenReturn(90);

        List<RewardSummaryDTO> result = rewardService.getRewardSummary(customerId, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());

        RewardSummaryDTO summary = result.get(0);
        assertEquals(customerId, summary.getId());
        assertEquals("Alice", summary.getCustomerName());
        assertEquals(1, summary.getTransactions().size());
        assertEquals(1, summary.getMonthlyPoints().size());

        TransactionDTO tx = summary.getTransactions().get(0);
        assertEquals(Long.valueOf(100L), tx.getId());
        assertEquals(BigDecimal.valueOf(120.0), tx.getAmount());

        verify(transactionRepository, times(1))
                .findTransactionsForCustomerBetweenDates(customerId, startDate, endDate);
        verify(transactionService, times(1))
                .calculateRewardPoints(BigDecimal.valueOf(120.0));
    }

    @Test
    public void testGetRewardSummaryEmptyTransactions() {
        when(transactionRepository.findTransactionsForCustomerBetweenDates(customerId, startDate, endDate))
                .thenReturn(Collections.emptyList());

        List<RewardSummaryDTO> result = rewardService.getRewardSummary(customerId, startDate, endDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionRepository, times(1))
                .findTransactionsForCustomerBetweenDates(customerId, startDate, endDate);
        verifyNoMoreInteractions(transactionService);
    }
}
