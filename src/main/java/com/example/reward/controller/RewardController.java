package com.example.reward.controller;

import com.example.reward.dto.RewardSummaryDTO;
import com.example.reward.exception.ResourceNotFoundException;
import com.example.reward.service.reward.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/customer-reward-summary/{customerId}")
    public ResponseEntity<List<RewardSummaryDTO>> getRewardSummary(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate fromDate;
        LocalDate toDate;

        try {
            fromDate = LocalDate.parse(startDate);
            toDate = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }

        return Optional.of(rewardService.getRewardSummary(customerId, fromDate, toDate))
                .filter(list -> !list.isEmpty())
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No rewards found for the provided customer ID"));
    }
}