package com.antoine.springJwt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antoine.springJwt.dto.BudgetDto;
import com.antoine.springJwt.mapper.BudgetMapper;
import com.antoine.springJwt.model.Budget;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.BudgetRepository;
import com.antoine.springJwt.service.BudgetService;
import com.antoine.springJwt.service.JwtService;
import com.antoine.springJwt.service.UserService;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final UserService userService;
    private final JwtService jwtService;
    private final BudgetRepository budgetRepository;

    public BudgetController(BudgetService budgetService, UserService userService, JwtService jwtService, BudgetRepository budgetRepository) {
        this.budgetService = budgetService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.budgetRepository = budgetRepository;
    }

    // @GetMapping("/user/{userId}")
    // public ResponseEntity<List<Budget>> getBudgetsByUser(@PathVariable Integer userId) {
    //     return ResponseEntity.ok(budgetService.getBudgetsByUser(userId));
    // }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        Integer userId = jwtService.extractUserId(token);

        if (budget.getUser() == null || budget.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null); // User is mandatory
        }

        // Fetch the user from the db
        User user = userService.getUserById(budget.getUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Invalid user
        }
        budget.setUser(user);
        return ResponseEntity.ok(budgetService.createBudget(budget, userId));
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
    @GetMapping("/count")
    public ResponseEntity<Long> countBudgets() {
        long count = budgetService.countBudgets();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/total-budget")
    public ResponseEntity<Double> getTotalBudgetAmount() {
       Double total = budgetService.getTotalBudgetAmount();
       return ResponseEntity.ok(total);
    }

    @GetMapping("/departments/{id}/budgets/count")
    public ResponseEntity<Long> countBudgetsByDepartment(@PathVariable("id") Integer departmentId) {
       long count = budgetRepository.countByDepartmentId(departmentId);
       return ResponseEntity.ok(count);
    }

    @GetMapping("/total-by-department")
    public Map<String, Double> getTotalBudgetByDepartment() {
        return budgetService.getTotalBudgetByDepartment();
    }


    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Integer budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();
    }
    
}
