package com.ecom.respository;


import com.ecom.model.Product;
import com.ecom.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(Seller seller);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(String category);// fixed
}