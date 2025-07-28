package com.ecom.service;

import com.ecom.model.*;
import com.ecom.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    public Product createProduct(Product product, Seller seller) {
        product.setSeller(seller);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        return productRepository.save(product);
    }
    
    public Product updateProduct(Product product) {
        product.setUpdatedAt(new Date());
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public List<Product> findAllBySeller(Seller seller) {
        return productRepository.findBySeller(seller);
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}