package com.monikamart.controller;

import com.monikamart.model.Order;
import com.monikamart.repository.CartItemRepository;
import com.monikamart.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class OrderController {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderController(
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository) {

        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/order/place")
    public String placeOrder() {

        var cartItems = cartItemRepository.findAll();

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        double total = cartItems.stream()
                .mapToDouble(item ->
                        item.getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();

        order.setTotalAmount(total);
        order.setOrderDate(LocalDateTime.now());

        orderRepository.save(order);

        // Clear cart after order
        cartItemRepository.deleteAll();

        return "redirect:/order/success";
    }

    @GetMapping("/order/success")
    public String success() {
        return "order-success";
    }
}