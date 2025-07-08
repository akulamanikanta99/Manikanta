package com.example.reward.service.reward;

import com.example.reward.dto.RewardSummaryDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface RewardService {

    List<RewardSummaryDTO> getRewardSummary(Long customerId, LocalDate startDate, LocalDate endDate);
}

