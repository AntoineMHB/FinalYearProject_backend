package com.antoine.springJwt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.Budget;
import com.antoine.springJwt.model.BudgetStatus;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.BudgetRepository;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditLogService auditLogService;

    public long getBudgetCountForDepartment(Integer departmentId) {
       return budgetRepository.countByDepartmentId(departmentId);
    }


    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Map<String, Double> getTotalBudgetByDepartment() {
        List<Object[]> results = budgetRepository.getTotalBudgetAmountGroupedByDepartment();
        Map<String, Double> budgetMap = new HashMap<>();

        for (Object[] row : results) {
            String departmentName = (String) row[0];
            Double totalAmount = (Double) row[1];
            budgetMap.put(departmentName, totalAmount);
        }

        return budgetMap;
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

    public boolean existsByBudgetNameAndUserId(String budgetName, Integer userId) {
       return budgetRepository.existsByBudgetNameAndUserId(budgetName, userId);
    }

    public List<Budget> getBudgetsByDepartmentId(Integer departmentId) {
        return budgetRepository.findByDepartmentId(departmentId);
    }

   
    public List<Budget> getBudgetsByStatus(BudgetStatus status) {
      return budgetRepository.findByStatus(status);
    }

    public Budget getBudgetById(Integer budgetId) {
    return budgetRepository.findById(budgetId).orElse(null);
  }

    public Budget save(Budget budget) {
    return budgetRepository.save(budget);
   }




    public void deleteBudget(Integer budgetId) {
        budgetRepository.deleteById(budgetId);
    }
    
}
