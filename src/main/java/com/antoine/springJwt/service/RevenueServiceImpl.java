package com.antoine.springJwt.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antoine.springJwt.dto.RevenueDto;
import com.antoine.springJwt.model.Revenue;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.RevenueRepository;

@Service
public class RevenueServiceImpl implements RevenueService {

    @Autowired
    private final RevenueRepository revenueRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserService userService;


    public RevenueServiceImpl(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    @Override
    public Double getTotalRevenueByDepartmentId(Integer departmentId) {
        return revenueRepository.getTotalRevenueByDepartmentId(departmentId);
    }



    @Override
    public Double calculateTotalRevenue(LocalDate start, LocalDate end, Integer userId) {
        List<Revenue> revenues = revenueRepository.findByUserIdAndCreatedAtBetween(userId, start, end);

        return revenues.stream()
             .mapToDouble(Revenue::getAmount)
             .sum();
    }

    @Override
    public List<Revenue> getAllRevenuesByUser(Integer userId) {
        return revenueRepository.findByUserId(userId);
    }

    @Override
    public List<Revenue> getAllRevenues() {
        return revenueRepository.findAll();
    }

    @Override
    public Revenue getRevenueById(Integer revenueId) {
        return revenueRepository.findById(revenueId)
                .orElseThrow(() -> new IllegalArgumentException("Revenue not found with ID: " + revenueId));
    }

    @Override
    public Revenue createRevenue(Revenue revenue, Integer userId) {
        User user = userService.getUserById(userId);
        auditLogService.log("CREATE", "REVENUE", "Created revenue of " + revenue.getAmount(), user);
        return revenueRepository.save(revenue);
    }

    @Override
    public Double getTotalRevenueAmount() {
        Double total = revenueRepository.getTotalRevenueAmount();
        return total != null ? total : 0.0;
    }

    @Override
    public List<Revenue> getRevenuesByDepartment(Integer departmentId) {
        return revenueRepository.findByDepartmentId(departmentId);
    }

  

    @Override
    public void deleteRevenue(Integer revenueId) {
        revenueRepository.deleteById(revenueId);
    }
}
