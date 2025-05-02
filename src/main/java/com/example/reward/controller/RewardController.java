package com.example.reward.controller;

import com.example.reward.dto.RewardPointDTO;
import com.example.reward.entity.RewardPoint;
import com.example.reward.exception.ResourceNotFoundException;
import com.example.reward.service.reward.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/customer-rewards/{customerId}")
    public ResponseEntity<Map<Long, List<RewardPointDTO>>> getRewardsForCustomer(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate parseStartDate = LocalDate.parse(startDate);
        LocalDate parseEndDate = LocalDate.parse(endDate);
        Map<Long, List<RewardPointDTO>> rewardPoints = rewardService.calculateRewardPointsForCustomer(customerId,
                parseStartDate, parseEndDate);
        return ResponseEntity.ok(rewardPoints);
    }

    @GetMapping("rewards/{customerId}")
    public ResponseEntity<Map<Long, List<RewardPointDTO>>> getRewardsForCustomer(@PathVariable Long customerId) {
        Map<Long, List<RewardPointDTO>> customerRewards = rewardService.getRewardsForCustomer(customerId);
        if (customerRewards.isEmpty()) {
            throw new ResourceNotFoundException("No rewards found for the provided customer ID");
        }
        return ResponseEntity.ok(customerRewards);
    }

    @GetMapping("/total-rewards")
    public ResponseEntity<Map<Long, List<RewardPointDTO>>> getAllRewards() {
        Map<Long, List<RewardPointDTO>> allRewards = rewardService.getAllRewardPointsGroupedByCustomer();
        return ResponseEntity.ok(allRewards);
    }
}


