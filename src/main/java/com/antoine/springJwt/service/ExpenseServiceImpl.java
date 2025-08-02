package com.antoine.springJwt.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antoine.springJwt.dto.ExpenseReportDto;
import com.antoine.springJwt.model.Expense;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private final ExpenseRepository expenseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditLogService auditLogService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Double getTotalExpenseByDepartment(Integer departmentId) {
       return expenseRepository.getTotalExpenseByDepartment(departmentId);
    }


@Override
public ExpenseReportDto generateReport(LocalDate start, LocalDate end, Integer userId) {
    List<Expense> expenses = expenseRepository.findByUserIdAndCreatedAtBetween(userId, start, end);

    double totalExpenses = expenses.stream()
         .mapToDouble(Expense::getAmount)
         .sum();
    
    double average = expenses.isEmpty() ? 0.0 : totalExpenses / expenses.size();
    
    Optional<Expense> maxExpense = expenses.stream().max(Comparator.comparing(Expense::getAmount));
    Optional<Expense> minExpense = expenses.stream().min(Comparator.comparing(Expense::getAmount));

    long activeDays = expenses.stream().map(Expense::getCreatedAt).distinct().count();
    long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
    long zeroDays = totalDays - activeDays;

    return new ExpenseReportDto(
        BigDecimal.valueOf(totalExpenses),
        BigDecimal.valueOf(average),
        maxExpense.orElse(null),
        minExpense.orElse(null),
        activeDays,
        zeroDays
    );
}


    
    @Override
    public Double calculateTotalExpense(LocalDate start, LocalDate end, Integer userId) {
        return expenseRepository
            .findByUserIdAndCreatedAtBetween(userId, start, end)
            .stream()
            .mapToDouble(Expense::getAmount)
            .sum();
    }


    public List<Expense> getAllExpensesByUser(Integer userId) {
        return expenseRepository.findByUserId(userId);

    }

    public List<Expense> getAllExpenses () {
        return expenseRepository.findAll();

    }

    public Double getTotalExpenseAmount() {
       Double total = expenseRepository.getTotalExpenseAmount();
       System.out.println("DEBUG TOTAL EXPENSE AMOUNT: " + total);
       return total != null ? total : 0.0;
    }


     public Expense getExpenseById(Integer expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + expenseId));
    }

    public Expense createExpense(Expense expense, Integer userId) {
        User user = userService.getUserById(userId);
        auditLogService.log("CREATE", "EXPENSE", "Created expense of " + expense.getAmount(), user);
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Integer expenseId) {
        expenseRepository.deleteById(expenseId);
    }
    
}
