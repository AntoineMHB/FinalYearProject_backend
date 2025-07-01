package com.antoine.springJwt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.UserRepository;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> tokenStore = new HashMap<>();

    public void sendResetToken(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new RuntimeException("Email not found");

        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);

        // Send the token via email 
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset your password");
        message.setText("Click the link: http://localhost:5173/reset-password?token=" + token);
        mailSender.send(message);
    }

    public void resetPassword(String token, String password) {
        String email = tokenStore.get(token);
        if (email == null) throw new RuntimeException("Invalid token");

        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);

        tokenStore.remove(token);
    }
    

    
}
