package com.example.reward.service.customer;

import com.example.reward.dto.CustomerDTO;
import com.example.reward.entity.Customer;
import com.example.reward.exception.ResourceNotFoundException;
import com.example.reward.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public void registerCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomerWithTransactionsAndRewards(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID " + customerId));

        customerRepository.delete(customer);
    }

    public CustomerDTO getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> new CustomerDTO(customer.getCustomerId(), customer.getName(), customer.getEmail()))
                .orElse(null);
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> new CustomerDTO(customer.getCustomerId(), customer.getName(), customer.getEmail()))
                .collect(Collectors.toList());
    }

}