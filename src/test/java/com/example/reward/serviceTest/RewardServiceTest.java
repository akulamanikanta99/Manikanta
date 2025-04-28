package com.example.reward.serviceTest;

import com.example.reward.entity.Customer;
import com.example.reward.entity.CustomerTransaction;
import com.example.reward.entity.RewardPoint;
import com.example.reward.repository.CustomerRepository;
import com.example.reward.repository.CustomerTransactionRepository;
import com.example.reward.repository.RewardPointRepository;
import com.example.reward.service.reward.RewardServiceImpl;
import com.example.reward.service.transaction.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RewardServiceTest {

    @Mock
    private CustomerTransactionRepository transactionRepository;

    @Mock
    private RewardPointRepository rewardPointRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private Long customerId;
    private LocalDate endDate;
    private CustomerTransaction transaction1, transaction2;

    @Before
    public void setupTestData() {
        customerId = 1L;
        endDate = LocalDate.now();

        Customer dummyCustomer = new Customer();
        dummyCustomer.setCustomerId(customerId);

        transaction1 = new CustomerTransaction();
        transaction1.setAmount(new BigDecimal("150"));
        transaction1.setDate(endDate.minusMonths(2).atStartOfDay());
        transaction1.setCustomer(dummyCustomer);

        transaction2 = new CustomerTransaction();
        transaction2.setAmount(new BigDecimal("75"));
        transaction2.setDate(endDate.minusMonths(1).atStartOfDay());
        transaction2.setCustomer(dummyCustomer);
    }

    @Test
    public void shouldReturnMonthlyAndTotalRewardSummaryForCustomer() {
        when(transactionService.calculateRewardPoints(new BigDecimal("150"))).thenReturn(100);
        when(transactionService.calculateRewardPoints(new BigDecimal("75"))).thenReturn(25);
        when(transactionRepository.findTransactionsForCustomerBetweenDates(customerId, endDate.minusMonths(3), endDate))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        Map<Long, List<Map<String, Integer>>> result = rewardService.getCustomerMonthlyRewardSummary(customerId, endDate);

        assertNotNull(result);
        assertTrue(result.containsKey(customerId));

        List<Map<String, Integer>> rewardList = result.get(customerId);
        assertEquals(4, rewardList.size()); // 3 months + total

        int calculatedTotal = rewardList.stream()
                .filter(map -> !map.containsKey("Total"))
                .flatMap(m -> m.values().stream())
                .mapToInt(Integer::intValue).sum();

        Map<String, Integer> totalMap = rewardList.get(rewardList.size() - 1);
        assertEquals((Integer) calculatedTotal, totalMap.get("Total"));
    }

    @Test
    public void shouldGroupAllRewardPointsByCustomerId() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        RewardPoint rp1 = new RewardPoint();
        rp1.setPoints(100);
        rp1.setCustomer(customer);

        RewardPoint rp2 = new RewardPoint();
        rp2.setPoints(25);
        rp2.setCustomer(customer);

        when(rewardPointRepository.findAll()).thenReturn(Arrays.asList(rp1, rp2));

        Map<Long, List<RewardPoint>> result = rewardService.getAllRewardPointsGroupedByCustomer();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(1L));
        assertEquals(2, result.get(1L).size());
    }
}
