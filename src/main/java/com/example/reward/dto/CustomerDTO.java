package com.example.reward.dto;

public class CustomerDTO {
    private Long customerId;
    private String name;
    private String email;

    public CustomerDTO(Long customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
    }

}
