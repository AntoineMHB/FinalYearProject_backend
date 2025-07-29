package com.antoine.springJwt.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antoine.springJwt.dto.RevenueDto;
import com.antoine.springJwt.mapper.RevenueMapper;
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
    public ResponseEntity<List<RevenueDto>> getRevenues() {
      List<Revenue> revenues = revenueService.getAllRevenues();
      List<RevenueDto> dtos = new ArrayList<>();
      for (Revenue revenue : revenues) {
        dtos.add(RevenueMapper.toDto(revenue));
}

    return ResponseEntity.ok(dtos);
}

    @GetMapping("/total-amount")
    public ResponseEntity<Double> getTotalRevenueAmount() {
       Double total = revenueService.getTotalRevenueAmount();
       return ResponseEntity.ok(total);
    }

    @GetMapping("/total-byPeriod")
    public Double getTotalRevenueForPeriod(
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RequestParam("userId") Integer userId) {
            return revenueService.calculateTotalRevenue(start, end, userId);
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
