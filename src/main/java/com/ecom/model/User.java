package com.ecom.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private boolean enabled;
    private String verificationCode;
    private String resetPasswordToken;
    private Date resetPasswordExpires;
    
    @Enumerated(EnumType.STRING)
    private UserType userType;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }
    public Date getResetPasswordExpires() { return resetPasswordExpires; }
    public void setResetPasswordExpires(Date resetPasswordExpires) { this.resetPasswordExpires = resetPasswordExpires; }
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
}