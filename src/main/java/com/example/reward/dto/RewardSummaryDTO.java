package com.example.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardSummaryDTO {
    private Long id;
    private String customerName;
    private List<TransactionDTO> transactions;
    private List<MonthlyPointDTO> monthlyPoints;
}