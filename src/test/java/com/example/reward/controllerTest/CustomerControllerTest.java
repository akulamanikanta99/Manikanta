package com.example.reward.controllerTest;

import com.example.reward.controller.CustomerController;
import com.example.reward.dto.CustomerDTO;
import com.example.reward.entity.Customer;
import com.example.reward.service.customer.CustomerService;
import com.example.reward.service.login.LoginAndLogoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Mock
    private LoginAndLogoutService loginAndLogoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterCustomerSuccessfully() throws Exception {
        Customer customer = new Customer(null, "Manikanta", "manikanta@example.com");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Customer registered successfully"));

        verify(customerService, times(1)).registerCustomer(any(Customer.class));
    }

    @Test
    void shouldLoginCustomerSuccessfully_whenCredentialsAreValid() throws Exception {
        when(loginAndLogoutService.authenticateCustomer("manikanta@example.com", "test123")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("email", "manikanta@example.com")
                        .param("password", "test123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void shouldFailLogin_whenCredentialsAreInvalid() throws Exception {
        when(loginAndLogoutService.authenticateCustomer("manikanta@example.com", "wrong")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("email", "manikanta@example.com")
                        .param("password", "wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void shouldLogoutCustomerSuccessfully() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));

        verify(loginAndLogoutService, times(1)).logout();
    }

    @Test
    void shouldDeleteCustomerSuccessfully() throws Exception {
        Long customerId = 1L;

        mockMvc.perform(delete("/" + customerId))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer with ID 1 and associated transactions and reward points deleted successfully"));

        verify(customerService, times(1)).deleteCustomerWithTransactionsAndRewards(customerId);
    }

    @Test
    void shouldReturnCustomerById() throws Exception {
        CustomerDTO dto = new CustomerDTO(1L, "Manikanta", "manikanta@example.com");

        when(customerService.getCustomerById(1L)).thenReturn(dto);

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.name").value("Manikanta"))
                .andExpect(jsonPath("$.email").value("manikanta@example.com"));
    }

    @Test
    void shouldReturn404_whenCustomerNotFoundById() throws Exception {
        when(customerService.getCustomerById(999L)).thenReturn(null);

        mockMvc.perform(get("/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllCustomers() throws Exception {
        List<CustomerDTO> customers = Arrays.asList(
                new CustomerDTO(1L, "John", "john@example.com"),
                new CustomerDTO(2L, "Alice", "alice@example.com")
        );

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(2));
    }

    @Test
    void shouldReturnEmptyList_whenNoCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
