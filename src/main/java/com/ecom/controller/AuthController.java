package com.ecom.controller;
import com.ecom.model.*;
import com.ecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    private SellerService sellerService;
    
    @Autowired
    private BuyerService buyerService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/register/seller")
    public String showSellerRegistrationForm(Model model) {
        model.addAttribute("seller", new Seller());
        
        return "seller-register";
    }
    
    @PostMapping("/register/seller")
    public String registerSeller(@ModelAttribute Seller seller, RedirectAttributes redirectAttributes) {
        sellerService.registerSeller(seller);
        redirectAttributes.addFlashAttribute("message", "Registration successful! Please check your email to verify your account.");
        return "redirect:/login";
    }
    
    @GetMapping("/register/buyer")
    public String showBuyerRegistrationForm(Model model) {
        model.addAttribute("buyer", new Buyer());
        return "buyer-register";
    }
    
    @PostMapping("/register/buyer")
    public String registerBuyer(@ModelAttribute Buyer buyer, RedirectAttributes redirectAttributes) {
        buyerService.registerBuyer(buyer);
        redirectAttributes.addFlashAttribute("message", "Registration successful! Please check your email to verify your account.");
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @GetMapping("/verify")
    public String verifyUser(@RequestParam String code, Model model) {
        boolean verified = userService.verifyUser(code);
        if (verified) {
            model.addAttribute("message", "Email verified successfully! You can now login.");
        } else {
            model.addAttribute("error", "Verification failed. The link may be invalid or expired.");
        }
        return "verification-result";
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        userService.initiatePasswordReset(email);
        redirectAttributes.addFlashAttribute("message", "If an account exists with that email, a password reset link has been sent.");
        return "redirect:/login";
    }
    
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        User user = userService.findByResetPasswordToken(token);
        if (user == null || user.getResetPasswordExpires().before(new java.util.Date())) {
            model.addAttribute("error", "Invalid or expired password reset token.");
            return "reset-password-error";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }
    
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token, @RequestParam String password, RedirectAttributes redirectAttributes) {
        boolean success = userService.resetPassword(token, password);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Password reset successfully! You can now login with your new password.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired password reset token.");
            return "redirect:/forgot-password";
        }
    }
}