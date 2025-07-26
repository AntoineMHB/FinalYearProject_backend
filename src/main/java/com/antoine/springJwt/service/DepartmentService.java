package com.antoine.springJwt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.antoine.springJwt.dto.DepartmentBudgetDto;
import com.antoine.springJwt.model.Budget;
import com.antoine.springJwt.model.Department;
import com.antoine.springJwt.model.Expense;
import com.antoine.springJwt.repository.DepartmentRepository;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartmentsByUser(Integer userId) {
        return departmentRepository.findByUserId(userId);

    }

    public List<Department> getAllDepartments () {
        return departmentRepository.findAll();

    }


     public Department getDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + departmentId));
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Integer departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    public List<DepartmentBudgetDto> getDepartmentBudgets() {
        List<DepartmentBudgetDto> result = new ArrayList<>();
        List<Department> departments = departmentRepository.findAll();

        for (Department dept : departments) {
            double allocated = 0.0;
            double spent = 0.0;

            for (Budget budget: dept.getBudgets()) {
                allocated += budget.getAmount();
                spent += budget.getExpenses().stream().mapToDouble(Expense::getAmount).sum();
            }
            double remaining = allocated - spent;

               result.add(new DepartmentBudgetDto(
            dept.getName(), 
            allocated, 
            spent, 
            remaining
        ));
        }

    return result;
    
  }

}
