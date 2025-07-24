package com.antoine.springJwt.mapper;

import com.antoine.springJwt.dto.ExpenseDto;
import com.antoine.springJwt.model.Expense;

public class ExpenseMapper {
    public static ExpenseDto toDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setExpenseName(expense.getExpenseName());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setUserId(expense.getUser().getId());
        dto.setBudgetId(expense.getBudget().getId());
        dto.setCreatedAt(expense.getCreatedAt());
        dto.setUpdatedAt(expense.getUpdatedAt());
        return dto;
    }
}
