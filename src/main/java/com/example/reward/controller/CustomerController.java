package com.example.reward.controller;


import com.example.reward.dto.CustomerDTO;
import com.example.reward.entity.Customer;
import com.example.reward.service.customer.CustomerService;
import com.example.reward.service.login.LoginAndLogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/v1")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoginAndLogoutService loginService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody @Validated Customer customer) {
        customerService.registerCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = loginService.authenticateCustomer(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutCustomer() {
        loginService.logout();
        return ResponseEntity.ok("Logged out successfully");
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive");
        }
        try {
            customerService.deleteCustomerWithTransactionsAndRewards(customerId);
            return ResponseEntity.ok("Customer with ID " + customerId
                    + " and associated transactions and reward points deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting customer with ID " + customerId);
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        CustomerDTO customerDTO = customerService.getCustomerById(
                customerId);
        if (customerDTO != null) {
            return ResponseEntity.ok(customerDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTO = customerService.getAllCustomers();
        return ResponseEntity.ok(customerDTO);
    }
}

