package com.example.reward.service.transaction;

import com.example.reward.entity.CustomerTransaction;
import com.example.reward.entity.RewardPoint;
import com.example.reward.repository.CustomerTransactionRepository;
import com.example.reward.repository.RewardPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private CustomerTransactionRepository transactionRepository;

    @Autowired
    private RewardPointRepository rewardPointRepository;

    @Override
    public int calculateRewardPoints(BigDecimal amount) {
        int points = 0;

        // Points for amount greater than $100
        if (amount.compareTo(new BigDecimal(100)) > 0) {
            points += amount.subtract(new BigDecimal(100)).intValue() * 2; // 2 points per dollar above $100
        }

        // Points for amount between $50 and $100
        if (amount.compareTo(new BigDecimal(50)) > 0) {
            points += Math.min(amount.intValue(), 100) - 50; // 1 point per dollar between $50 and $100
        }

        return points;
    }

    @Override
    @Transactional
    public void processTransaction(CustomerTransaction transaction) {
        int points = calculateRewardPoints(transaction.getAmount());

        RewardPoint rewardPoint = new RewardPoint.Builder()
                .setCustomer(transaction.getCustomer())
                .setPoints(points)
                .setCreatedAt(transaction.getDate())
                .setCustomerTransaction(transaction)
                .build();

        rewardPointRepository.save(rewardPoint);
        transactionRepository.save(transaction);
    }
}