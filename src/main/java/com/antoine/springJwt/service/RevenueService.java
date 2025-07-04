package com.antoine.springJwt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.Revenue;
import com.antoine.springJwt.repository.RevenueRepository;

@Service
public class RevenueService {
    private final RevenueRepository revenueRepository;

    public RevenueService(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    public List<Revenue> getAllRevenuesByUser(Integer userId) {
        return revenueRepository.findByUserId(userId);

    }

    public List<Revenue> getAllRevenues () {
        return revenueRepository.findAll();

    }


     public Revenue getRevenueById(Integer revenueId) {
        return revenueRepository.findById(revenueId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + revenueId));
    }

    public Revenue createRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }

    // RevenueService.java
    public Double getTotalRevenueAmount() {
       Double total = revenueRepository.getTotalRevenueAmount();
       System.out.println("DEBUG TOTAL REVENUE AMOUNT: " + total);
       return total != null ? total : 0.0;
    }


    public void deleteRevenue(Integer revenueId) {
        revenueRepository.deleteById(revenueId);
    }
    
}
