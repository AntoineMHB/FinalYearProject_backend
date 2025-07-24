package com.antoine.springJwt.mapper;

import com.antoine.springJwt.dto.RevenueDto;
import com.antoine.springJwt.model.Revenue;

public class RevenueMapper {
    public static RevenueDto toDto(Revenue revenue) {
        RevenueDto dto = new RevenueDto();
        dto.setId(revenue.getId());
        dto.setRevenueName(revenue.getRevenueName());
        dto.setAmount(revenue.getAmount());
        dto.setDescription(revenue.getDescription());
        dto.setUserId(revenue.getUser().getId());
        dto.setBudgetId(revenue.getBudget().getId());
        dto.setCreatedAt(revenue.getCreatedAt());
        dto.setUpdatedAt(revenue.getUpdatedAt());
        return dto;
    }
}
