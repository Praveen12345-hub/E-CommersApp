package com.ecom.controller;


import com.ecom.model.*;
import com.ecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/buyer")
public class BuyerController {
    @Autowired
    private BuyerService buyerService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/dashboard")
    public String buyerDashboard(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }
        
        List<Product> products = productService.findAllProducts();
        model.addAttribute("buyer", buyer);
        model.addAttribute("products", products);
        return "buyer-dashboard";
    }
    
    @GetMapping("/profile")
    public String showBuyerProfile(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("buyer", buyer);
        return "buyer-profile";
    }
    
    @PostMapping("/profile")
    public String updateBuyerProfile(@ModelAttribute Buyer buyer, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer existingBuyer = buyerService.findByEmail(email);
        if (existingBuyer == null) {
            return "redirect:/login";
        }
        
        buyer.setId(existingBuyer.getId());
        buyer.setEmail(existingBuyer.getEmail());
        buyer.setPassword(existingBuyer.getPassword());
        buyer.setEnabled(existingBuyer.isEnabled());
        buyer.setUserType(existingBuyer.getUserType());
        
        buyerService.updateBuyer(buyer);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        return "redirect:/buyer/profile";
    }
    
    @GetMapping("/products")
    public String viewProducts(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }
        
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "buyer-products";
    }
    
    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String keyword, Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }
        
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("searchKeyword", keyword);
        return "buyer-products";
    }
    
    @GetMapping("/products/category/{category}")
    public String viewProductsByCategory(@PathVariable String category, Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }
        
        List<Product> products = productService.findByCategory(category);
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        return "buyer-products";
    }
    
    @GetMapping("/products/view/{id}")
    public String viewProductDetails(@PathVariable Long id, Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }
        
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/buyer/products";
        }
        
        model.addAttribute("product", product);
        return "buyer-product-details";
    }
    
    @PostMapping("/products/order/{id}")
    public String placeOrder(@PathVariable Long id, @RequestParam int quantity, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }
        
        Product product = productService.findById(id);
        if (product == null || product.getQuantity() < quantity) {
            redirectAttributes.addFlashAttribute("error", "Product not available or insufficient quantity.");
            return "redirect:/buyer/products/view/" + id;
        }
        
        orderService.createOrder(product, buyer, quantity);
        redirectAttributes.addFlashAttribute("message", "Order placed successfully! The seller has been notified.");
        return "redirect:/buyer/orders";
    }
    
    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        Buyer buyer = buyerService.findByEmail(email);
        if (buyer == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrdersByBuyer(buyer);
        model.addAttribute("orders", orders);
        return "buyer-orders";
    }

}
/* It handles all web requests from the BUYER role, such as:
 Viewing the dashboard
 Managing profile
 Browsing and ordering products
 Viewing orders
 * */
