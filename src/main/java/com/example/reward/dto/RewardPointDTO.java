package com.example.reward.dto;

import java.time.LocalDateTime;

public class RewardPointDTO {
    private Long rewardPointId;
    private int points;
    private LocalDateTime createdAt;
    private Long customerId;
    private Long transactionId;
    private String month;

    public RewardPointDTO(Long rewardPointId, int points, LocalDateTime createdAt, Long customerId,
                          Long transactionId, String month) {
        this.rewardPointId = rewardPointId;
        this.points = points;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.month = month;
    }

    public RewardPointDTO() {
    }

    public RewardPointDTO(Long rewardPointId, int points, LocalDateTime createdAt, Long customerId,
                          Long transactionId) {
        this.rewardPointId = rewardPointId;
        this.points = points;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.transactionId = transactionId;
    }

    public Long getRewardPointId() {
        return rewardPointId;
    }

    public void setRewardPointId(Long rewardPointId) {
        this.rewardPointId = rewardPointId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    // Getter and setter for the month
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "RewardPointDTO{" +
                "rewardPointId=" + rewardPointId +
                ", points=" + points +
                ", createdAt=" + createdAt +
                ", customerId=" + customerId +
                ", transactionId=" + transactionId +
                ", month='" + month + '\'' +
                '}';
    }
}
