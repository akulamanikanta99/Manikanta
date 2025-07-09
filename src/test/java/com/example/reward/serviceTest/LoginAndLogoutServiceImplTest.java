package com.example.reward.serviceTest;

import com.example.reward.entity.Customer;
import com.example.reward.repository.CustomerRepository;
import com.example.reward.service.login.LoginAndLogoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginAndLogoutServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private LoginAndLogoutServiceImpl loginService;

    private Customer mockCustomer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockCustomer = new Customer();
        mockCustomer.setCustomerId(1L);
        mockCustomer.setName("John Doe");
        mockCustomer.setEmail("john@example.com");
    }

    @Test
    public void authenticateCustomerWithCorrectCredentials() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockCustomer));

        boolean result = loginService.authenticateCustomer("john@example.com", "password123");

        assertTrue(result);
        verify(customerRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    public void failAuthenticationWithWrongPassword() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockCustomer));

        boolean result = loginService.authenticateCustomer("john@example.com", "wrongPassword");

        assertFalse(result);
        verify(customerRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    public void failAuthenticationIfEmailNotFound() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        boolean result = loginService.authenticateCustomer("notfound@example.com", "password123");

        assertFalse(result);
        verify(customerRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    public void logoutSuccessfully() {
        loginService.logout();  // just verifying no exception occurs and resets state

        assertDoesNotThrow(() -> loginService.logout());
    }
}
