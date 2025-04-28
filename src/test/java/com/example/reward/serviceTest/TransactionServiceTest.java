package com.example.reward.serviceTest;

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
import java.time.LocalDate;

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
    public void initTransactionData() {
        transaction = new CustomerTransaction();
        transaction.setAmount(new BigDecimal("150"));
        transaction.setDate(LocalDate.now().atStartOfDay());
        transaction.setSpentDetails("Test transaction");
    }

    @Test
    public void shouldReturnDoublePointsForAmountAbove100() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("150"));

        assertEquals(100, points);  // (150-100)*2
    }

    @Test
    public void shouldReturnSinglePointsForAmountBetween50And100() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("75"));

        assertEquals(25, points);
    }

    @Test
    public void shouldReturnZeroPointsForAmountBelow50() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("30"));

        assertEquals(0, points);
    }

    @Test
    public void shouldSaveTransactionAndRewardPointsCorrectly() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        transactionService.processTransaction(transaction);

        verify(rewardPointRepository, times(1)).save(any(RewardPoint.class));
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void shouldReturnZeroPointsForZeroAmount() {
        int points = transactionService.calculateRewardPoints(BigDecimal.ZERO);

        assertEquals(0, points);
    }

    @Test
    public void shouldReturnZeroPointsForExactly100() {
        int points = transactionService.calculateRewardPoints(new BigDecimal("100"));

        assertEquals(0, points);
    }
}

