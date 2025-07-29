package com.antoine.springJwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.Budget;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.BudgetRepository;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditLogService auditLogService;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public List<Budget> getBudgetsByUser(Integer userId) {
        return budgetRepository.findByUserId(userId);
    }

    public List<Budget> getAllBudgets () {
        return budgetRepository.findAll();

    }

    public Double getTotalBudgetAmount() {
       Double total = budgetRepository.getTotalBudgetAmount();
       System.out.println("DEBUG TOTAL BUDGET AMOUNT: " + total);
       return total != null ? total : 0.0;
    }

    public long countBudgets() {
        return budgetRepository.count();
    }

    public Budget createBudget(Budget budget, Integer userId) {
        // we ensure all mandatory fields area set
        if (budget.getAmount() == null || budget.getUser() == null) {
            throw new IllegalArgumentException("Amount and User are required");
        }
        User user = userService.getUserById(userId);
        auditLogService.log("CREATE", "BUDGET", "Created budget of " + budget.getAmount(), user);
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Integer budgetId) {
        budgetRepository.deleteById(budgetId);
    }
    
}
