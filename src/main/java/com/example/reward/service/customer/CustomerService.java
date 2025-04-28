package com.example.reward.service.customer;

import com.example.reward.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    void registerCustomer(Customer customer);

    void deleteCustomerWithTransactionsAndRewards(Long customerId);
}
