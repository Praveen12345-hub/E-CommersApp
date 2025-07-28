package com.ecom.respository;


import com.ecom.model.Buyer;
import com.ecom.model.Order;
import com.ecom.model.Product;
import com.ecom.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(Buyer buyer);
    List<Order> findByProduct(Product product);
    List<Order> findByProductSeller(Seller seller);
}