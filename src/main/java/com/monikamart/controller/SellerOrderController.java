package com.monikamart.controller;

import com.monikamart.model.Order;
import com.monikamart.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SellerOrderController {

    private final OrderRepository orderRepository;

    public SellerOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/seller/orders")
    public String sellerOrders(HttpSession session, Model model) {

        String role = (String) session.getAttribute("userRole");

        if (role == null || !role.equalsIgnoreCase("SELLER")) {
            return "redirect:/login";
        }

        List<Order> orders = orderRepository.findAll();

        model.addAttribute("orders", orders);

        return "seller-orders";
    }

    @PostMapping("/seller/orders/update-status")
    public String updateStatus(
            @RequestParam Long orderId,
            @RequestParam String status,
            HttpSession session) {

        String role = (String) session.getAttribute("userRole");

        if (role == null || !role.equalsIgnoreCase("SELLER")) {
            return "redirect:/login";
        }

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }

        return "redirect:/seller/orders";
    }
}

