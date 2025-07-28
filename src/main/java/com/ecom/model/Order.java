package com.ecom.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int quantity;
    private double totalPrice;
    private String status;
    private Date orderDate;
    private Date deliveryDate;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Buyer getBuyer() { return buyer; }
    public void setBuyer(Buyer buyer) { this.buyer = buyer; }
}