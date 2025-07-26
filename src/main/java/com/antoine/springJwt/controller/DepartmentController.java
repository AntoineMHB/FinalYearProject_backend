package com.antoine.springJwt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antoine.springJwt.dto.DepartmentBudgetDto;
import com.antoine.springJwt.dto.DepartmentDto;
import com.antoine.springJwt.mapper.DepartmentMapper;
import com.antoine.springJwt.model.Department;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.service.DepartmentService;
import com.antoine.springJwt.service.UserService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final UserService userService;

    public DepartmentController(DepartmentService departmentService, UserService userService) {
        this.departmentService = departmentService;
        this.userService = userService;
    }

  
    
@GetMapping
public ResponseEntity<List<DepartmentDto>> getDepartments() {
    List<Department> departments = departmentService.getAllDepartments();
    List<DepartmentDto> dtos = new ArrayList<>();
    for (Department department : departments) {
        dtos.add(DepartmentMapper.toDto(department));
}

    return ResponseEntity.ok(dtos);
}
    @GetMapping("/budget-summary")
    public ResponseEntity<List<DepartmentBudgetDto>> getDepartmentBudgetsSummary() {
        List<DepartmentBudgetDto> summaries = departmentService.getDepartmentBudgets();
        return ResponseEntity.ok(summaries);
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {


        if (department.getUser() == null || department.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null); // User is mandatory
        }

        // Fetch the user from the db
        User user = userService.getUserById(department.getUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Invalid user
        }
        department.setUser(user);
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }


}
