package com.antoine.springJwt.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.AuthenticationResponse;
import com.antoine.springJwt.model.Role;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getEmail().endsWith("@admincomp.com")) {
            user.setRole(Role.ADMIN);
        } else {
             user.setRole(Role.USER);
        }
        

        user = repository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(
            token, 
            user.getId(),
            user.getFirstname(), 
            user.getLastname(), 
            user.getEmail());
    }

   // Method to authenticate a user during login
   public AuthenticationResponse authenticate(User request) {
    try {
        // Authenticate using the AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Find the user by email after authentication
        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> 
            new RuntimeException("User not found"));

        // Generate JWT token for the authenticated user
        String token = jwtService.generateToken(user);

        // Return the response containing the JWT token and user details
        return new AuthenticationResponse(
            token, 
            user.getId(),
            user.getFirstname(), 
            user.getLastname(), 
            user.getEmail()
           );
    } catch (AuthenticationException e) {
        // Handle invalid credentials or other authentication errors
        throw new RuntimeException("Invalid email or password", e);
    }
}

}
