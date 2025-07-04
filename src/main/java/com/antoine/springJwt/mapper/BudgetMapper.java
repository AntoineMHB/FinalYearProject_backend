package com.antoine.springJwt.mapper;

import com.antoine.springJwt.dto.BudgetDto;
import com.antoine.springJwt.model.Budget;

public class BudgetMapper {
    public static BudgetDto toDto(Budget budget) {
        BudgetDto dto = new BudgetDto();
        dto.setId(budget.getId());
        dto.setBudgetName(budget.getBudgetName());
        dto.setAmount(budget.getAmount());
        dto.setDescription(budget.getDescription());
        dto.setUserId(budget.getUser().getId());
        dto.setDepartmentId(budget.getDepartment().getId());
        return dto;
    }
}
