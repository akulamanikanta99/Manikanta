package com.example.reward.controller;

import com.example.reward.entity.RewardPoint;
import com.example.reward.exception.ResourceNotFoundException;
import com.example.reward.service.reward.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/customer-rewards/{customerId}/{endDate}")
    public ResponseEntity<Map<Long, List<Map<String, Integer>>>> getRewardsForCustomerForThreeMonths(@PathVariable Long customerId, @PathVariable String endDate) {
        LocalDate start = LocalDate.parse(endDate);
        Map<Long, List<Map<String, Integer>>> rewardPoints = rewardService.getCustomerMonthlyRewardSummary(customerId, start);
        return ResponseEntity.ok(rewardPoints);
    }

    @GetMapping("/total-rewards")
    public ResponseEntity<Map<Long, List<RewardPoint>>> getAllRewards() {
        Map<Long, List<RewardPoint>> allRewards = rewardService.getAllRewardPointsGroupedByCustomer();
        return ResponseEntity.ok(allRewards);
    }
}

