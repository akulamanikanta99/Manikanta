package com.example.reward.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardPointId;

    private int points;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private CustomerTransaction customerTransaction;

    public static class Builder {
        private int points;
        private LocalDateTime createdAt;
        private Customer customer;
        private CustomerTransaction customerTransaction;

        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setCustomerTransaction(CustomerTransaction customerTransaction) {
            this.customerTransaction = customerTransaction;
            return this;
        }

        public RewardPoint build() {
            RewardPoint rewardPoint = new RewardPoint();
            rewardPoint.points = this.points;
            rewardPoint.createdAt = this.createdAt;
            rewardPoint.customer = this.customer;
            rewardPoint.customerTransaction = this.customerTransaction;
            return rewardPoint;
        }
    }
}
