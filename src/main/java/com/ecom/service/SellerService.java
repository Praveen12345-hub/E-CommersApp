package com.ecom.service;

import com.ecom.model.Seller;
import com.ecom.model.User;
import com.ecom.model.UserType;
import com.ecom.respository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

    /**
     * Registers a new seller. Also registers the seller as a user with type SELLER.
     */
    public Seller registerSeller(Seller seller) {
        userService.registerUser(seller, UserType.SELLER);
        return sellerRepository.save(seller);
    }

    /**
     * Updates an existing seller's details.
     */
    public Seller updateSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    /**
     * Finds a seller by their email.
     * Returns null if no user with the given email is found or if the user is not a seller.
     */
    public Seller findByEmail(String email) {
        User user = userService.findByEmail(email);
        if (user != null && user.getUserType() == UserType.SELLER) {
            return sellerRepository.findById(user.getId()).orElse(null);
        }
        return null;
    }
}
