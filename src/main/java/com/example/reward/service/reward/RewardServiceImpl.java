package com.example.reward.service.reward;

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

    @Override
    @Transactional
    public Map<Long, List<Map<String, Integer>>> getCustomerMonthlyRewardSummary(Long customerId, LocalDate endDate) {
        String[] monthLabels = {"", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        LocalDate startDate = endDate.minusMonths(3);
        List<CustomerTransaction> customerTransactions = transactionRepository
                .findTransactionsForCustomerBetweenDates(customerId, startDate, endDate);

        Map<Long, List<Map<String, Integer>>> rewardSummaryByCustomer = new HashMap<>();
        Map<Integer, Integer> rewardPointsByMonth = new HashMap<>();

        for (CustomerTransaction txn : customerTransactions) {
            int rewardPoints = transactionService.calculateRewardPoints(txn.getAmount());
            int monthNumber = txn.getDate().getMonthValue();
            rewardPointsByMonth.merge(monthNumber, rewardPoints, Integer::sum);
        }

        int accumulatedTotalPoints = rewardPointsByMonth.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        List<Map<String, Integer>> monthlyRewardsList = new ArrayList<>();
        int startMonth = startDate.getMonthValue();
        int endMonth = endDate.getMonthValue();

        for (int i = 0; i <= 2; i++) {
            int targetMonth = (startMonth + i - 1) % 12 + 1; // Adjust for rollover and 1-based indexing
            String monthLabel = monthLabels[targetMonth];

            Map<String, Integer> monthReward = new HashMap<>();
            monthReward.put(monthLabel, rewardPointsByMonth.getOrDefault(targetMonth, 0));
            monthlyRewardsList.add(monthReward);
        }

        Map<String, Integer> totalRewardMap = new HashMap<>();
        totalRewardMap.put("Total", accumulatedTotalPoints);
        monthlyRewardsList.add(totalRewardMap);

        rewardSummaryByCustomer.put(customerId, monthlyRewardsList);
        return rewardSummaryByCustomer;
    }


    @Override
    public Map<Long, List<RewardPoint>> getAllRewardPointsGroupedByCustomer() {
        List<RewardPoint> allRewards = rewardPointRepository.findAll();
        return allRewards.stream()
                .collect(Collectors.groupingBy(rewardPoint -> rewardPoint.getCustomer().getCustomerId()));
    }

}
