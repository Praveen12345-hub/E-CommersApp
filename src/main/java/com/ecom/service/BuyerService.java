package com.ecom.service;

import com.ecom.model.*;
import com.ecom.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerService {
    @Autowired
    private BuyerRepository buyerRepository;
    
    @Autowired
    private UserService userService;
    
    public Buyer registerBuyer(Buyer buyer) {
        userService.registerUser(buyer, UserType.BUYER);
        return buyerRepository.save(buyer);
    }
    
    public Buyer updateBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }
    
    public Buyer findByEmail(String email) {
        User user = userService.findByEmail(email);
        if (user != null && user.getUserType().equals(UserType.BUYER)) {
            return buyerRepository.findById(user.getId()).orElse(null);
        }
        return null;
    }
}