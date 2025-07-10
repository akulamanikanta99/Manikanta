package com.example.reward.service.login;

import com.example.reward.entity.Customer;
import com.example.reward.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginAndLogoutServiceImpl implements LoginAndLogoutService {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer loggedInCustomer = null;

    public boolean authenticateCustomer(String email, String password) {
        return customerRepository.findByEmail(email)
                .filter(c -> "password123".equals(password))
                .map(c -> {
                    loggedInCustomer = c;
                    return true;
                })
                .orElse(false);
    }

    public void logout() {
        loggedInCustomer = null;
    }
}
