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
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/dashboard")
    public String sellerDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        List<Product> products = productService.findAllBySeller(seller);
        List<Order> orders = orderService.getOrdersBySeller(seller);
        
        model.addAttribute("seller", seller);
        model.addAttribute("products", products);
        model.addAttribute("orders", orders);
        return "seller-dashboard";
    }
    
    @GetMapping("/profile")
    public String showSellerProfile(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("seller", seller);
        return "seller-profile";
    }
    
    @PostMapping("/profile")
    public String updateSellerProfile(@ModelAttribute Seller seller, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller existingSeller = sellerService.findByEmail(email);
        if (existingSeller == null) {
            return "redirect:/login";
        }
        
        seller.setId(existingSeller.getId());
        seller.setEmail(existingSeller.getEmail());
        seller.setPassword(existingSeller.getPassword());
        seller.setEnabled(existingSeller.isEnabled());
        seller.setUserType(existingSeller.getUserType());
        
        sellerService.updateSeller(seller);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        return "redirect:/seller/profile";
    }
    
    @GetMapping("/products/add")
    public String showAddProductForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("product", new Product());
        return "seller-add-product";
    }
    
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        productService.createProduct(product, seller);
        redirectAttributes.addFlashAttribute("message", "Product added successfully!");
        return "redirect:/seller/dashboard";
    }
    
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        Product product = productService.findById(id);
        if (product == null || !product.getSeller().getId().equals(seller.getId())) {
            return "redirect:/seller/dashboard";
        }
        
        model.addAttribute("product", product);
        return "seller-edit-product";
    }
    
    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        Product existingProduct = productService.findById(id);
        if (existingProduct == null || !existingProduct.getSeller().getId().equals(seller.getId())) {
            return "redirect:/seller/dashboard";
        }
        
        product.setId(id);
        product.setSeller(seller);
        product.setCreatedAt(existingProduct.getCreatedAt());
        
        productService.updateProduct(product);
        redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
        return "redirect:/seller/dashboard";
    }
    
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        Product product = productService.findById(id);
        if (product != null && product.getSeller().getId().equals(seller.getId())) {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        }
        
        return "redirect:/seller/dashboard";
    }
    
    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        List<Order> orders = orderService.getOrdersBySeller(seller);
        model.addAttribute("orders", orders);
        return "seller-orders";
    }
    
    @PostMapping("/orders/update-status/{id}")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        
        Seller seller = sellerService.findByEmail(email);
        if (seller == null) {
            return "redirect:/login";
        }
        
        Order order = orderService.findById(id);
        if (order != null && order.getProduct().getSeller().getId().equals(seller.getId())) {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("message", "Order status updated successfully!");
        }
        
        return "redirect:/seller/orders";
    }
}