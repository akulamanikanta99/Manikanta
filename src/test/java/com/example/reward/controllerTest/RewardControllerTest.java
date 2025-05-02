package com.example.reward.controllerTest;

import com.example.reward.controller.RewardController;
import com.example.reward.dto.RewardPointDTO;
import com.example.reward.entity.RewardPoint;
import com.example.reward.exception.ResourceNotFoundException;
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

    @Test
    public void testCalculateRewardPointsForCustomer() {
        Long customerId = 1001L;
        String startDate = "2025-01-01";
        String endDate = "2025-03-31";

        List<RewardPointDTO> rewardPointDTOList = new ArrayList<>();

        RewardPointDTO january = new RewardPointDTO();
        january.setMonth("January");
        january.setPoints(50);
        rewardPointDTOList.add(january);

        RewardPointDTO february = new RewardPointDTO();
        february.setMonth("February");
        february.setPoints(80);
        rewardPointDTOList.add(february);

        RewardPointDTO march = new RewardPointDTO();
        march.setMonth("March");
        march.setPoints(70);
        rewardPointDTOList.add(march);

        RewardPointDTO total = new RewardPointDTO();
        total.setMonth("Total");
        total.setPoints(200);
        rewardPointDTOList.add(total);

        Map<Long, List<RewardPointDTO>> mockResponse = new HashMap<>();
        mockResponse.put(customerId, rewardPointDTOList);

        when(rewardService.calculateRewardPointsForCustomer(eq(customerId), LocalDate.parse(eq(startDate)),
                LocalDate.parse(eq(endDate))))
                .thenReturn(mockResponse);

        Map<Long, List<RewardPointDTO>> response = rewardController.getRewardsForCustomer(customerId, startDate,
                        endDate)
                .getBody();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(mockResponse, response);
        verify(rewardService, times(1)).calculateRewardPointsForCustomer(eq(customerId), LocalDate.parse(eq(startDate)),
                LocalDate.parse(eq(endDate)));
    }

    @Test
    public void testGetRewardsForCustomer() {
        List<RewardPointDTO> mockRewardPoints = new ArrayList<>();
        RewardPointDTO rewardPoint1 = new RewardPointDTO(1L, 50, null, 123L, 456L, "January");
        mockRewardPoints.add(rewardPoint1);

        Map<Long, List<RewardPointDTO>> mockResponse = new HashMap<>();
        mockResponse.put(123L, mockRewardPoints);

        when(rewardService.getRewardsForCustomer(eq(123L))).thenReturn(mockResponse);

        ResponseEntity<Map<Long, List<RewardPointDTO>>> response = rewardController.getRewardsForCustomer(123L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());

        verify(rewardService, times(1)).getRewardsForCustomer(eq(123L));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRewardsForCustomerNoRewards() {
        when(rewardService.getRewardsForCustomer(eq(customerId))).thenReturn(new HashMap<>());
        rewardController.getRewardsForCustomer(customerId);
    }

    @Test
    public void testGetAllRewards() {
        List<RewardPointDTO> mockRewardPoints = new ArrayList<>();
        RewardPointDTO rewardPoint1 = new RewardPointDTO(1L, 100, null, 123L, 456L, "January");
        mockRewardPoints.add(rewardPoint1);

        Map<Long, List<RewardPointDTO>> mockResponse = new HashMap<>();
        mockResponse.put(123L, mockRewardPoints);

        when(rewardService.getAllRewardPointsGroupedByCustomer()).thenReturn(mockResponse);

        ResponseEntity<Map<Long, List<RewardPointDTO>>> response = rewardController.getAllRewards();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());

        verify(rewardService, times(1)).getAllRewardPointsGroupedByCustomer();
    }
}
