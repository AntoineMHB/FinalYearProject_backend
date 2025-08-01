package com.antoine.springJwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.antoine.springJwt.model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer>{
    List<Budget> findByUserId(Integer userId);

    @Query(value = "SELECT SUM(max_amount) FROM budget", nativeQuery = true)
    Double getTotalBudgetAmount();

    long countByDepartmentId(Integer departmentId);

    @Query("SELECT b.department.name, SUM(b.amount) " +
           "FROM Budget b " +
           "GROUP BY b.department.name")
    List<Object[]> getTotalBudgetAmountGroupedByDepartment();
    
}


