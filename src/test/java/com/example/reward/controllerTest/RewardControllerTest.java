package com.example.reward.controllerTest;

import com.example.reward.controller.RewardController;
import com.example.reward.dto.MonthlyPointDTO;
import com.example.reward.dto.RewardSummaryDTO;
import com.example.reward.dto.TransactionDTO;
import com.example.reward.exception.ResourceNotFoundException;
import com.example.reward.service.reward.RewardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    private List<RewardSummaryDTO> rewardSummaryList;

    @Before
    public void setup() {
        TransactionDTO transaction = new TransactionDTO(1L, 1L, BigDecimal.valueOf(120.0), LocalDateTime.of(2024, 11, 1, 10, 30));
        MonthlyPointDTO monthlyPoint = new MonthlyPointDTO(2024, "NOVEMBER", 90);

        RewardSummaryDTO summary = new RewardSummaryDTO();
        summary.setId(1L);
        summary.setCustomerName("Alice");
        summary.setTransactions(Collections.singletonList(transaction));
        summary.setMonthlyPoints(Collections.singletonList(monthlyPoint));

        rewardSummaryList = Collections.singletonList(summary);
    }

    @Test
    public void testGetRewardSummarySuccess() {
        when(rewardService.getRewardSummary(eq(1L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(rewardSummaryList);

        ResponseEntity<List<RewardSummaryDTO>> response = rewardController.getRewardSummary(
                1L, "2024-11-01", "2024-11-30");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Alice", response.getBody().get(0).getCustomerName());

        verify(rewardService, times(1)).getRewardSummary(eq(1L), any(LocalDate.class), any(LocalDate.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRewardSummaryNoData() {
        when(rewardService.getRewardSummary(eq(1L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        rewardController.getRewardSummary(1L, "2024-11-01", "2024-11-30");
    }
}
