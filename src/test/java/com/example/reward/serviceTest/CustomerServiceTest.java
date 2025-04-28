package com.example.reward.serviceTest;

import com.example.reward.entity.Customer;
import com.example.reward.exception.ResourceNotFoundException;
import com.example.reward.repository.CustomerRepository;
import com.example.reward.service.customer.CustomerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private Long customerId;

    @Before
    public void initTestData() {
        customerId = 1L;
        customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
    }

    @Test
    public void shouldRegisterCustomerSuccessfully() {
        customerService.registerCustomer(customer);

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void shouldDeleteCustomerWithRelations_whenCustomerExists() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.deleteCustomerWithTransactionsAndRewards(customerId);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowException_whenDeletingNonExistentCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        customerService.deleteCustomerWithTransactionsAndRewards(customerId);

    }
}

