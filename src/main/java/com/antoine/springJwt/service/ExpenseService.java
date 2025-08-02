package com.antoine.springJwt.service;

import java.time.LocalDate;
import java.util.List;

import com.antoine.springJwt.dto.ExpenseReportDto;
import com.antoine.springJwt.model.Expense;

public interface ExpenseService {
    ExpenseReportDto generateReport(LocalDate start, LocalDate end, Integer userId);
    Double getTotalExpenseAmount();
    Expense createExpense(Expense expense, Integer userId);
    void deleteExpense(Integer expenseId);
    List<Expense> getAllExpenses();
    public Double calculateTotalExpense(LocalDate start, LocalDate end, Integer userId);
    Double getTotalExpenseByDepartment(Integer departmentId);

}
