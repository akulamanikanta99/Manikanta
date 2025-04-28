package com.example.reward.service.login;

import org.springframework.stereotype.Service;

@Service
public interface LoginAndLogoutService {

    boolean authenticateCustomer(String email, String password);

    void logout();
}
