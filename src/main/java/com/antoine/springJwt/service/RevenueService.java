package com.antoine.springJwt.service;

import java.time.LocalDate;
import java.util.List;

import com.antoine.springJwt.model.Revenue;

public interface RevenueService {
    List<Revenue> getAllRevenuesByUser(Integer userId);
    List<Revenue> getAllRevenues();
    Revenue getRevenueById(Integer revenueId);
    Revenue createRevenue(Revenue revenue, Integer userId);
    Double getTotalRevenueAmount();
    void deleteRevenue(Integer revenueId);
    Double calculateTotalRevenue(LocalDate start, LocalDate end, Integer userId);
    Double getTotalRevenueByDepartmentId(Integer departmentId);
}
