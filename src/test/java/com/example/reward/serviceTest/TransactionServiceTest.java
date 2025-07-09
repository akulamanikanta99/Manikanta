package com.example.reward.serviceTest;

import com.example.reward.entity.Customer;
import com.example.reward.entity.CustomerTransaction;
import com.example.reward.entity.RewardPoint;
import com.example.reward.repository.CustomerTransactionRepository;
import com.example.reward.repository.RewardPointRepository;
import com.example.reward.service.transaction.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private CustomerTransactionRepository transactionRepository;

    @Mock
    private RewardPointRepository rewardPointRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private CustomerTransaction transaction;

    @Before
    public void setUp() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John");

        transaction = new CustomerTransaction();
        transaction.setTransactionId(1L);
        transaction.setAmount(new BigDecimal("150"));
        transaction.setDate(LocalDateTime.of(2024, 11, 10, 10, 0));
        transaction.setSpentDetails("Test transaction");
        transaction.setCustomer(customer);
    }

    @Test
    public void calculateRewardPointsReturnCorrectPointsWhenAmountIsAbove100() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("150"));
        assertEquals(100, points); // (150 - 100) * 2 = 100 points
    }

    @Test
    public void calculateRewardPointsReturnCorrectPointsWhenAmountIsBetween50And100() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("75"));
        assertEquals(25, points); // 75 - 50 = 25 points
    }

    @Test
    public void calculateRewardPointsReturnZeroWhenAmountIsBelow50() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("30"));
        assertEquals(0, points);
    }

    @Test
    public void calculateRewardPointsReturnZeroWhenAmountIsExactly100() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("100"));
        assertEquals(0, points);
    }

    @Test
    public void calculateRewardPointsReturnZeroWhenAmountIsZero() {
        int points = transactionService.calculateRewardPoints(BigDecimal.ZERO);
        assertEquals(0, points);
    }

    @Test
    public void processTransactionSaveTransactionAndRewardPoint() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        transactionService.processTransaction(transaction);

        verify(transactionRepository, times(1)).save(transaction);
        verify(rewardPointRepository, times(1)).save(any(RewardPoint.class));
        verifyNoMoreInteractions(transactionRepository, rewardPointRepository);
    }
}
