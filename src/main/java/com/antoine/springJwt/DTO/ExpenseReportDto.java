package com.antoine.springJwt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseReportDto {
    private BigDecimal totalExpenses;
    private BigDecimal averageDailyExpense;
    private LocalDate highestExpenseDate;
    private Double highestExpenseAmount;
    private LocalDate lowestExpenseDate;
    private Double lowestExpenseAmount;
    private int activeExpenseDays;
    private int zeroSpendDays;
    private Map<LocalDate, BigDecimal> dailyExpenses;
    private Map<String, BigDecimal> expensesByBudget;

    
    // Custom constructor (placed below all fields)
    public ExpenseReportDto(
        BigDecimal totalExpenses,
        BigDecimal averageDailyExpense,
        com.antoine.springJwt.model.Expense highestExpense,
        com.antoine.springJwt.model.Expense lowestExpense,
        long activeExpenseDays,
        long zeroSpendDays
    ) {
        this.totalExpenses = totalExpenses;
        this.averageDailyExpense = averageDailyExpense;
        this.highestExpenseDate = highestExpense != null ? highestExpense.getCreatedAt().toLocalDate() : null;
        this.highestExpenseAmount = highestExpense != null ? highestExpense.getAmount(): null;
        this.lowestExpenseDate = lowestExpense != null ? lowestExpense.getCreatedAt().toLocalDate() : null;
        this.lowestExpenseAmount = lowestExpense != null ? lowestExpense.getAmount() : null;
        this.activeExpenseDays = (int) activeExpenseDays;
        this.zeroSpendDays = (int) zeroSpendDays;
        this.dailyExpenses = null;
        this.expensesByBudget = null;
    }
}
