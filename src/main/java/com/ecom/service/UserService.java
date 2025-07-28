package com.ecom.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.ecom.model.User;
import com.ecom.model.UserType;
import com.ecom.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user by email: " + email);  // Debug log
        User user = userRepository.findByEmail(email);

        if (user == null) {
            System.out.println("User not found with email: " + email);  // Debug log
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        System.out.println("Found user: " + user.getEmail() + ", enabled: " + user.isEnabled());  // Debug log
        System.out.println("Stored password: " + user.getPassword());  // Debug log

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserType().name())
                .disabled(!user.isEnabled())
                .build();
    }
    
    public User registerUser(User user, UserType userType) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(userType);
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        
        User savedUser = userRepository.save(user);
        
        // Send verification email
        String verificationUrl = "http://localhost:8080/verify?code=" + user.getVerificationCode();
        String emailText = "Please verify your email by clicking the link: " + verificationUrl;
        emailService.sendEmail(user.getEmail(), "Email Verification", emailText);
        
        return savedUser;
    }
    
    public boolean verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        
        if (user == null || user.isEnabled()) {
            return false;
        }
        
        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);
        return true;
    }
    
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(UUID.randomUUID().toString());
            user.setResetPasswordExpires(new Date(System.currentTimeMillis() + 3600000)); // 1 hour
            userRepository.save(user);
            
            String resetUrl = "http://localhost:8080/reset-password?token=" + user.getResetPasswordToken();
            String emailText = "To reset your password, click the link: " + resetUrl;
            emailService.sendEmail(user.getEmail(), "Password Reset", emailText);
        }
    }
    
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        
        if (user == null || user.getResetPasswordExpires().before(new Date())) {
            return false;
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpires(null);
        userRepository.save(user);
        return true;
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

}