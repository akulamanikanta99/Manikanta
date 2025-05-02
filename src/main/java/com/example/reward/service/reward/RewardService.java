package com.example.reward.service.reward;

import com.example.reward.dto.RewardPointDTO;
import com.example.reward.entity.RewardPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface RewardService {

    Map<Long, List<RewardPointDTO>> calculateRewardPointsForCustomer(Long customerId, LocalDate parseStartDate,
                                                                     LocalDate parseEndDate);

    Map<Long, List<RewardPointDTO>> getRewardsForCustomer(Long customerId);

    Map<Long, List<RewardPointDTO>> getAllRewardPointsGroupedByCustomer();
}

