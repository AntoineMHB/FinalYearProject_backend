package com.antoine.springJwt.controller;

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

import com.antoine.springJwt.model.Revenue;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.service.RevenueService;
import com.antoine.springJwt.service.UserService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/revenues")
public class RevenueController {
    private final RevenueService revenueService;
    private final UserService userService;

    public RevenueController(RevenueService revenueService, UserService userService) {
        this.revenueService = revenueService;
        this.userService = userService;
    }

  
    
    @GetMapping
    public ResponseEntity<List<Revenue>> getRevenues() {
        return ResponseEntity.ok(revenueService.getAllRevenues());
    }

    @PostMapping
    public ResponseEntity<Revenue> createRevenue(@RequestBody Revenue revenue) {


        if (revenue.getUser() == null || revenue.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null); // User is mandatory
        }

        // Fetch the user from the db
        User user = userService.getUserById(revenue.getUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Invalid user
        }
        revenue.setUser(user);
        return ResponseEntity.ok(revenueService.createRevenue(revenue));
    }

    @DeleteMapping("/{revenueId}")
    public ResponseEntity<Void> deleteRevenue(@PathVariable Integer revenueId) {
        revenueService.deleteRevenue(revenueId);
        return ResponseEntity.noContent().build();
    }


}
