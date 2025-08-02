package com.antoine.springJwt.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antoine.springJwt.dto.ExpenseDto;
import com.antoine.springJwt.dto.ExpenseReportDto;
import com.antoine.springJwt.mapper.ExpenseMapper;
import com.antoine.springJwt.model.Expense;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.service.ExpenseService;
import com.antoine.springJwt.service.JwtService;
import com.antoine.springJwt.service.UserService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final UserService userService;
    private final JwtService jwtService;

    public ExpenseController(ExpenseService expenseService, UserService userService, JwtService jwtService) {
        this.expenseService = expenseService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

  
    
   @GetMapping
    public ResponseEntity<List<ExpenseDto>> getExpenses() {
      List<Expense> expenses = expenseService.getAllExpenses();
      List<ExpenseDto> dtos = new ArrayList<>();
      for (Expense expense : expenses) {
        dtos.add(ExpenseMapper.toDto(expense));
}

    return ResponseEntity.ok(dtos);
}

    @GetMapping("/total-expense")
    public ResponseEntity<Double> getTotalExpenseAmount() {
       Double total = expenseService.getTotalExpenseAmount();
       return ResponseEntity.ok(total);
    }

    @GetMapping("/report")
    public ExpenseReportDto getExpenseReport(
        @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
        @RequestParam("userId") Integer userId) {
            return expenseService.generateReport(start, end, userId);
    }

    @GetMapping("/total-expense-by-dpt/{departmentId}")
    public ResponseEntity<Double> getTotalExpenseByDepartment(@PathVariable Integer departmentId) {
        Double total = expenseService.getTotalExpenseByDepartment(departmentId);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    

    @PostMapping
    public ResponseEntity<Expense> createRevenue(@RequestBody Expense expense, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        Integer userId = jwtService.extractUserId(token);

        if (userId == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (expense.getUser() == null || expense.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null); // User is mandatory
        }

        // Fetch the user from the db
        User user = userService.getUserById(expense.getUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Invalid user
        }
        expense.setUser(user);
        return ResponseEntity.ok(expenseService.createExpense(expense, userId));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }


}
