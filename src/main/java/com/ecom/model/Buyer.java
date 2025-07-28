package com.ecom.model;

import jakarta.persistence.*;
@Entity
@Table(name = "buyers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Buyer extends User{
private String shippingAddress;
    
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { 
    	this.shippingAddress = shippingAddress;
    	}

}


