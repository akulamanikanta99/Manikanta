package com.example.reward.controllerTest;

import com.example.reward.controller.RewardController;
import com.example.reward.dto.MonthlyPointDTO;
import com.example.reward.dto.RewardSummaryDTO;
import com.example.reward.dto.TransactionDTO;
import com.example.reward.service.reward.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnRewardSummarySuccessfully() throws Exception {
        TransactionDTO transaction = new TransactionDTO(
                1L, 1L, BigDecimal.valueOf(120.0), LocalDateTime.of(2024, 11, 1, 10, 30)
        );

        MonthlyPointDTO monthlyPoint = new MonthlyPointDTO(2024, "NOVEMBER", 90);

        RewardSummaryDTO summary = new RewardSummaryDTO(
                1L, "Alice",
                List.of(transaction),
                List.of(monthlyPoint)
        );

        when(rewardService.getRewardSummary(eq(1L), any(), any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(get("/api/v1/customer-reward-summary/1")
                        .param("startDate", "2024-11-01")
                        .param("endDate", "2024-11-30")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Alice"))
                .andExpect(jsonPath("$[0].transactions[0].amount").value(120.0))
                .andExpect(jsonPath("$[0].monthlyPoints[0].points").value(90));
    }

    @Test
    void shouldReturn404WhenNoRewardSummaryExists() throws Exception {
        when(rewardService.getRewardSummary(eq(1L), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/customer-reward-summary/1")
                        .param("startDate", "2024-11-01")
                        .param("endDate", "2024-11-30"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No rewards found for the provided customer ID"));
    }
}
