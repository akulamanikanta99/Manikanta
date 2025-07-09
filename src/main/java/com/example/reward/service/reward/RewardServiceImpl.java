package com.example.reward.service.reward;

import com.example.reward.dto.MonthlyPointDTO;
import com.example.reward.dto.RewardSummaryDTO;
import com.example.reward.dto.TransactionDTO;
import com.example.reward.entity.CustomerTransaction;
import com.example.reward.repository.CustomerRepository;
import com.example.reward.repository.CustomerTransactionRepository;
import com.example.reward.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private CustomerTransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<RewardSummaryDTO> getRewardSummary(Long customerId, LocalDate startDate, LocalDate endDate) {
        List<CustomerTransaction> transactions =
                transactionRepository.findTransactionsForCustomerBetweenDates(customerId, startDate, endDate);

        if (transactions == null || transactions.isEmpty()) return Collections.emptyList();

        Map<YearMonth, Integer> monthlyPointsMap = new HashMap<>();
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        for (CustomerTransaction transaction : transactions) {
            if (transaction == null ||
                    transaction.getCustomer() == null ||
                    transaction.getCustomer().getCustomerId() == null) {
                continue;
            }

            BigDecimal amount = transaction.getAmount() != null ? transaction.getAmount() : BigDecimal.ZERO;
            LocalDate transactionDate = transaction.getDate() != null ? transaction.getDate().toLocalDate() : LocalDate.now();
            Long transactionId = transaction.getTransactionId() != null ? transaction.getTransactionId() : 0L;
            Long txnCustomerId = transaction.getCustomer().getCustomerId();

            int points = transactionService.calculateRewardPoints(amount);
            YearMonth ym = YearMonth.from(transactionDate);
            monthlyPointsMap.merge(ym, points, Integer::sum);

            transactionDTOList.add(new TransactionDTO(
                    transactionId,
                    txnCustomerId,
                    amount,
                    transactionDate.atStartOfDay()
            ));
        }

        List<MonthlyPointDTO> monthlyPoints = monthlyPointsMap.entrySet().stream()
                .map(e -> new MonthlyPointDTO(
                        e.getKey().getYear(),
                        e.getKey().getMonth().toString(),
                        e.getValue()))
                .sorted(Comparator.comparing(MonthlyPointDTO::getYear)
                        .thenComparing(mp -> YearMonth.of(mp.getYear(), java.time.Month.valueOf(mp.getMonth()))))
                .collect(Collectors.toList());

        String customerName = transactions.get(0).getCustomer() != null
                ? Optional.ofNullable(transactions.get(0).getCustomer().getName()).orElse("")
                : "";

        RewardSummaryDTO summary = new RewardSummaryDTO(
                customerId,
                customerName,
                transactionDTOList,
                monthlyPoints
        );

        return List.of(summary);
    }
}