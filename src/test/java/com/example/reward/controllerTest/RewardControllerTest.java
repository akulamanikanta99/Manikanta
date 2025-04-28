package com.example.reward.controllerTest;

import com.example.reward.controller.RewardController;
import com.example.reward.entity.RewardPoint;
import com.example.reward.service.reward.RewardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    private Long customerId;
    private LocalDate endDate;

    @Before
    public void initTestData() {
        customerId = 1L;
        endDate = LocalDate.now();
    }

    @Test
    public void shouldReturnCustomerRewardSummary_whenValidCustomerIdAndEndDateProvided() {
        // Arrange
        Map<Long, List<Map<String, Integer>>> expectedRewardSummary = new HashMap<>();
        List<Map<String, Integer>> rewardBreakdown = new ArrayList<>();

        rewardBreakdown.add(Collections.singletonMap("January", 50));
        rewardBreakdown.add(Collections.singletonMap("February", 80));
        rewardBreakdown.add(Collections.singletonMap("March", 70));
        rewardBreakdown.add(Collections.singletonMap("Total", 200));

        expectedRewardSummary.put(customerId, rewardBreakdown);

        when(rewardService.getCustomerMonthlyRewardSummary(eq(customerId), eq(endDate)))
                .thenReturn(expectedRewardSummary);

        ResponseEntity<Map<Long, List<Map<String, Integer>>>> response =
                rewardController.getRewardsForCustomerForThreeMonths(customerId, endDate.toString());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedRewardSummary, response.getBody());
        verify(rewardService, times(1)).getCustomerMonthlyRewardSummary(customerId, endDate);
    }

    @Test
    public void shouldReturnAllCustomerRewardSummaries() {
        RewardPoint reward = new RewardPoint();
        reward.setPoints(100);

        Map<Long, List<RewardPoint>> expectedRewards = new HashMap<>();
        expectedRewards.put(customerId, Collections.singletonList(reward));

        when(rewardService.getAllRewardPointsGroupedByCustomer()).thenReturn(expectedRewards);

        ResponseEntity<Map<Long, List<RewardPoint>>> response = rewardController.getAllRewards();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedRewards, response.getBody());
        verify(rewardService, times(1)).getAllRewardPointsGroupedByCustomer();
    }
}

