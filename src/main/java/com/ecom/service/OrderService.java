package com.ecom.service;

import com.ecom.model.*;
import com.ecom.respository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Creates a new order for a product by a buyer with the given quantity.
     * Sends email notification to the seller.
     */
    public Order createOrder(Product product, Buyer buyer, int quantity) {
        Order order = new Order();
        order.setProduct(product);
        order.setBuyer(buyer);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice() * quantity);
        order.setStatus("PENDING");
        order.setOrderDate(new Date());

        Order savedOrder = orderRepository.save(order);

        // Notify seller via email
        String sellerEmail = product.getSeller().getEmail();
        String message = String.format(
            "You have a new order for product: %s\nQuantity: %d\nTotal: $%.2f\nBuyer: %s %s",
            product.getName(),
            quantity,
            order.getTotalPrice(),
            buyer.getFirstName(),
            buyer.getLastName()
        );

        emailService.sendEmail(sellerEmail, "New Order Notification", message);

        return savedOrder;
    }

    /**
     * Updates the status of an order and sets delivery date if status is DELIVERED.
     */
    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);

            if ("DELIVERED".equalsIgnoreCase(status)) {
                order.setDeliveryDate(new Date());
            }

            return orderRepository.save(order);
        }

        return null;
    }

    /**
     * Returns all orders placed by a specific buyer.
     */
    public List<Order> getOrdersByBuyer(Buyer buyer) {
        return orderRepository.findByBuyer(buyer);
    }

    /**
     * Returns all orders for products sold by a specific seller.
     */
    public List<Order> getOrdersBySeller(Seller seller) {
        return orderRepository.findByProductSeller(seller);
    }

    /**
     * Finds an order by its ID.
     */
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
