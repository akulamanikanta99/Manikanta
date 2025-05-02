package com.example.reward.service.reward;

import com.example.reward.dto.RewardPointDTO;
import com.example.reward.entity.CustomerTransaction;
import com.example.reward.entity.RewardPoint;
import com.example.reward.repository.CustomerRepository;
import com.example.reward.repository.CustomerTransactionRepository;
import com.example.reward.repository.RewardPointRepository;
import com.example.reward.service.transaction.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private CustomerTransactionRepository transactionRepository;

    @Autowired
    private RewardPointRepository rewardPointRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public Map<Long, List<RewardPointDTO>> calculateRewardPointsForCustomer(Long customerId, LocalDate startDate,
                                                                            LocalDate endDate) {
        List<CustomerTransaction> transactions = transactionRepository.findTransactionsForCustomerBetweenDates(
                customerId, startDate, endDate);

        Map<Long, List<RewardPointDTO>> customerRewardPoints = new HashMap<>();
        Map<YearMonth, Integer> monthlyPointsMap = new HashMap<>();

        for (CustomerTransaction transaction : transactions) {
            int points = transactionService.calculateRewardPoints(transaction.getAmount());
            YearMonth yearMonth = YearMonth.from(transaction.getDate());
            monthlyPointsMap.put(yearMonth, monthlyPointsMap.getOrDefault(yearMonth, 0) + points);
        }

        int totalPoints = monthlyPointsMap.values().stream().mapToInt(Integer::intValue).sum();
        List<RewardPointDTO> monthlyRewardPointsList = new ArrayList<>();

        YearMonth current = YearMonth.from(startDate);
        YearMonth end = YearMonth.from(endDate);

        while (!current.isAfter(end)) {
            RewardPointDTO rewardPointDTO = new RewardPointDTO();
            rewardPointDTO.setMonth(current.getMonth().name().substring(0, 1).toUpperCase() +
                    current.getMonth().name().substring(1).toLowerCase());
            rewardPointDTO.setPoints(monthlyPointsMap.getOrDefault(current, 0));
            monthlyRewardPointsList.add(rewardPointDTO);

            current = current.plusMonths(1);
        }

        RewardPointDTO totalRewardDTO = new RewardPointDTO();
        totalRewardDTO.setPoints(totalPoints);
        monthlyRewardPointsList.add(totalRewardDTO);

        customerRewardPoints.put(customerId, monthlyRewardPointsList);
        return customerRewardPoints;
    }

    public Map<Long, List<RewardPointDTO>> getRewardsForCustomer(Long customerId) {
        Map<Long, List<RewardPointDTO>> customerRewardsMap = new HashMap<>();

        List<RewardPoint> rewards = rewardPointRepository.findRewardsByCustomerId(customerId);

        if (rewards != null && !rewards.isEmpty()) {
            List<RewardPointDTO> rewardPointDTOs = rewards.stream()
                    .map(rewardPoint -> new RewardPointDTO(
                            rewardPoint.getRewardPointId(),
                            rewardPoint.getPoints(),
                            rewardPoint.getCreatedAt(),
                            rewardPoint.getCustomer().getCustomerId(),
                            rewardPoint.getCustomerTransaction() != null ?
                                    rewardPoint.getCustomerTransaction().getTransactionId() :
                                    null
                    ))
                    .collect(Collectors.toList());

            customerRewardsMap.put(customerId, rewardPointDTOs);
        } else {
            customerRewardsMap.put(customerId, Collections.emptyList());
        }

        return customerRewardsMap;
    }

    public Map<Long, List<RewardPointDTO>> getAllRewardPointsGroupedByCustomer() {
        List<RewardPoint> allRewards = rewardPointRepository.findAll();

        return allRewards.stream()
                .map(rewardPoint -> new RewardPointDTO(
                        rewardPoint.getRewardPointId(),
                        rewardPoint.getPoints(),
                        rewardPoint.getCreatedAt(),
                        rewardPoint.getCustomer().getCustomerId(),
                        rewardPoint.getCustomerTransaction() != null ?
                                rewardPoint.getCustomerTransaction().getTransactionId() :
                                null
                ))
                .collect(Collectors.groupingBy(RewardPointDTO::getCustomerId));
    }

}

