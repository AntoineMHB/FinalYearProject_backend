package com.antoine.springJwt.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.antoine.springJwt.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer>{
    List<Expense> findByUserId(Integer userId);

    @Query(value = "SELECT SUM(max_amount) FROM expense", nativeQuery = true)
    Double getTotalExpenseAmount();

    List<Expense> findByUserIdAndCreatedAtBetween(Integer userId, LocalDate start, LocalDate end);

    @Query(value = """
       SELECT COALESCE(SUM(e.max_amount), 0)
       FROM expense e
       JOIN budget b ON e.budget_id = b.id
       WHERE b.department_id = :departmentId
       """, nativeQuery = true)
    Double getTotalExpenseByDepartment(@Param("departmentId") Integer departmentId);



    
}
