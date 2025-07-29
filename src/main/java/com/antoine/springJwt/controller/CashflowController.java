package com.antoine.springJwt.controller;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import com.antoine.springJwt.dto.CashflowDto;
import com.antoine.springJwt.service.ExpenseService;
import com.antoine.springJwt.service.RevenueService;

@RestController
@RequestMapping("/api") // Optional base path
public class CashflowController {

    private final RevenueService revenueService;
    private final ExpenseService expenseService;

    public CashflowController(RevenueService revenueService, ExpenseService expenseService) {
        this.revenueService = revenueService;
        this.expenseService = expenseService;
    }

    @GetMapping("/cashflow/summary")
    public CashflowDto getCashflowSummary(
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RequestParam("userId") Integer userId) {

        Double totalRevenue = revenueService.calculateTotalRevenue(start, end, userId);
        Double totalExpenses = expenseService.calculateTotalExpense(start, end, userId);
        Double netProfit = totalRevenue - totalExpenses;

        return new CashflowDto(totalRevenue, totalExpenses, netProfit);
    }
}
