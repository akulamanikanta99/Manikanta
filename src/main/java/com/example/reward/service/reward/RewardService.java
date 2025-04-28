package com.example.reward.service.reward;

import com.example.reward.entity.RewardPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface RewardService {

    Map<Long, List<Map<String, Integer>>> getCustomerMonthlyRewardSummary(Long customerId, LocalDate endDate);

    Map<Long, List<RewardPoint>> getAllRewardPointsGroupedByCustomer();
}
