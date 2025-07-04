package com.antoine.springJwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.antoine.springJwt.model.Revenue;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Integer>{
    List<Revenue> findByUserId(Integer userId);

    @Query(value = "SELECT SUM(max_amount) FROM revenue", nativeQuery = true)
    Double getTotalRevenueAmount();
    
}
