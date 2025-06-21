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

import com.antoine.springJwt.model.Account;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.service.AccountService;
import com.antoine.springJwt.service.UserService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

  
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(accountService.getAllAccountsByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {


        if (account.getUser() == null || account.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null); // User is mandatory
        }

        // Fetch the user from the db
        User user = userService.getUserById(account.getUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Invalid user
        }
        account.setUser(user);
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }


}
