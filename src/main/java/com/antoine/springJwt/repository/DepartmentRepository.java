package com.antoine.springJwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antoine.springJwt.model.Department;


public interface DepartmentRepository extends JpaRepository<Department, Integer>{
    List<Department> findByUserId(Integer userId);
}
