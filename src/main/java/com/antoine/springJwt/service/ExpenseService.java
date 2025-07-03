package com.antoine.springJwt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.Expense;
import com.antoine.springJwt.repository.ExpenseRepository;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpensesByUser(Integer userId) {
        return expenseRepository.findByUserId(userId);

    }

    public List<Expense> getAllExpenses () {
        return expenseRepository.findAll();

    }


     public Expense getExpenseById(Integer expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + expenseId));
    }

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Integer expenseId) {
        expenseRepository.deleteById(expenseId);
    }
    
}
