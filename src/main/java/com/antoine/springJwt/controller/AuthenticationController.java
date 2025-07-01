package com.antoine.springJwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.antoine.springJwt.DTO.ForgotPasswordRequest;
import com.antoine.springJwt.DTO.ResetPasswordRequest;
import com.antoine.springJwt.model.AuthenticationResponse;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.service.AuthenticationService;
import com.antoine.springJwt.service.PasswordResetService;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class AuthenticationController {

    @Autowired
    private PasswordResetService passwordResetService;

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendResetToken(request.getEmail());
        return ResponseEntity.ok("Reset link sent");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password updated");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
