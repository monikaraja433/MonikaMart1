package com.monikamart.controller;

import com.monikamart.model.Order;
import com.monikamart.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderHistoryController {

    private final OrderRepository orderRepository;

    public OrderHistoryController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/my-orders")
    public String myOrders(HttpSession session, Model model) {

        Object userId = session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderRepository.findAll();

        model.addAttribute("orders", orders);

        return "my-orders";
    }
}