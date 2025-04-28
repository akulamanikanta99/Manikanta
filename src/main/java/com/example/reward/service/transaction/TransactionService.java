package com.example.reward.service.transaction;

import com.example.reward.entity.CustomerTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TransactionService {

    int calculateRewardPoints(BigDecimal amount);

    void processTransaction(CustomerTransaction transaction);
}
