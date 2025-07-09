package com.example.reward.serviceTest;

import com.example.reward.dto.CustomerDTO;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
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
    public void shouldDeleteCustomerWithRelationsWhenCustomerExists() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        customerService.deleteCustomerWithTransactionsAndRewards(customerId);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenDeletingNonExistentCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        customerService.deleteCustomerWithTransactionsAndRewards(customerId);
    }

    @Test
    public void shouldReturnCustomerDTOWhenCustomerExists() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        CustomerDTO dto = customerService.getCustomerById(customerId);
        assertNotNull(dto);
        assertEquals(customer.getCustomerId(), dto.getCustomerId());
        assertEquals(customer.getEmail(), dto.getEmail());
    }

    @Test
    public void shouldReturnNullWhenCustomerNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        CustomerDTO dto = customerService.getCustomerById(customerId);
        assertNull(dto);
    }

    @Test
    public void shouldReturnAllCustomerDTOs() {
        Customer customer2 = new Customer(2L, "Alice", "alice@example.com");
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer, customer2));
        assertEquals(2, customerService.getAllCustomers().size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoCustomersExist() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(customerService.getAllCustomers().isEmpty());
    }
}
