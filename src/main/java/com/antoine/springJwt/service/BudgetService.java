package com.antoine.springJwt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.Budget;
import com.antoine.springJwt.repository.BudgetRepository;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public List<Budget> getBudgetsByUser(Integer userId) {
        return budgetRepository.findByUserId(userId);
    }

    public List<Budget> getAllBudgets () {
        return budgetRepository.findAll();

    }

    public Budget creaBudget(Budget budget) {
        // we ensure all mandatory fields area set
        if (budget.getAmount() == null || budget.getUser() == null) {
            throw new IllegalArgumentException("Amount and User are required");
        }
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Integer budgetId) {
        budgetRepository.deleteById(budgetId);
    }
    
}
