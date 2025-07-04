package com.antoine.springJwt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antoine.springJwt.dto.BudgetDto;
import com.antoine.springJwt.mapper.BudgetMapper;
import com.antoine.springJwt.model.Budget;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.service.BudgetService;
import com.antoine.springJwt.service.UserService;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final UserService userService;

    public BudgetController(BudgetService budgetService, UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;
    }

    // @GetMapping("/user/{userId}")
    // public ResponseEntity<List<Budget>> getBudgetsByUser(@PathVariable Integer userId) {
    //     return ResponseEntity.ok(budgetService.getBudgetsByUser(userId));
    // }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        if (budget.getUser() == null || budget.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null); // User is mandatory
        }

        // Fetch the user from the db
        User user = userService.getUserById(budget.getUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Invalid user
        }
        budget.setUser(user);
        return ResponseEntity.ok(budgetService.creaBudget(budget));
    }

@GetMapping
public ResponseEntity<List<BudgetDto>> getBudgets() {
    List<Budget> budgets = budgetService.getAllBudgets();
    List<BudgetDto> dtos = new ArrayList<>();
    for (Budget budget : budgets) {
        dtos.add(BudgetMapper.toDto(budget));
}

    return ResponseEntity.ok(dtos);
}

    // public ResponseEntity<List<Budget>> getBudgets() {
    //     return ResponseEntity.ok(budgetService.getAllBudgets());
    // }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Integer budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();
    }
    
}
