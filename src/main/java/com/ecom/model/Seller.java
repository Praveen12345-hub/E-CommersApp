package com.ecom.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User {
    private String companyName;
    private String taxNumber;
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getTaxNumber() { return taxNumber; }
    public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }
}