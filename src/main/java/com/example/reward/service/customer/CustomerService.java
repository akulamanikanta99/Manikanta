package com.example.reward.service.customer;

import com.example.reward.dto.CustomerDTO;
import com.example.reward.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    void registerCustomer(Customer customer);

    void deleteCustomerWithTransactionsAndRewards(Long customerId);

    CustomerDTO getCustomerById(Long customerId);

    List<CustomerDTO> getAllCustomers();
}
