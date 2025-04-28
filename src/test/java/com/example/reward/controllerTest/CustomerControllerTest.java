package com.example.reward.controllerTest;

import com.example.reward.controller.CustomerController;
import com.example.reward.entity.Customer;
import com.example.reward.exception.ResourceNotFoundException;
import com.example.reward.service.customer.CustomerService;
import com.example.reward.service.login.LoginAndLogoutService;
import com.example.reward.service.reward.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private LoginAndLogoutService loginAndLogoutService;

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterCustomerSuccessfully() {
        Customer customer = new Customer();
        customer.setName("Manikanta");
        customer.setEmail("manikanta@example.com");

        ResponseEntity<String> response = customerController.registerCustomer(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Customer registered successfully", response.getBody());
    }

    @Test
    void shouldLoginCustomerSuccessfully_whenCredentialsAreValid() {
        String email = "manikanta@example.com";
        String password = "test123";

        when(loginAndLogoutService.authenticateCustomer(email, password)).thenReturn(true);

        ResponseEntity<String> response = customerController.loginCustomer(email, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    void shouldFailLogin_whenCredentialsAreInvalid() {
        String email = "manikanta@example.com";
        String password = "wrongPassword";

        when(loginAndLogoutService.authenticateCustomer(email, password)).thenReturn(false);

        ResponseEntity<String> response = customerController.loginCustomer(email, password);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void shouldLogoutCustomerSuccessfully() {
        ResponseEntity<String> response = customerController.logoutCustomer();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", response.getBody());

        verify(loginAndLogoutService, times(1)).logout();
    }

    @Test
    void shouldThrowException_whenDeletingCustomerWithNegativeId() {
        Long customerId = -1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerController.deleteCustomer(customerId);
        });

        assertEquals("Customer ID must be positive", exception.getMessage());
    }

    @Test
    void shouldReturnError_whenCustomerToDeleteIsNotFound() {
        Long customerId = 1L;

        doThrow(new ResourceNotFoundException("Customer not found"))
                .when(customerService).deleteCustomerWithTransactionsAndRewards(customerId);

        ResponseEntity<String> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting customer with ID " + customerId, response.getBody());
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        Long customerId = 1L;

        doNothing().when(customerService).deleteCustomerWithTransactionsAndRewards(customerId);

        ResponseEntity<String> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer with ID " + customerId + " and associated transactions and reward points deleted successfully", response.getBody());
    }
}

