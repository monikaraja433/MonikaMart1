package com.monikamart.controller;

import com.monikamart.model.Order;
import com.monikamart.model.Product;
import com.monikamart.repository.CartItemRepository;
import com.monikamart.repository.OrderRepository;
import com.monikamart.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class OrderController {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/order/place")
    public String placeOrder() {

        var cartItems = cartItemRepository.findAll();

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Check stock before placing order
        for (var item : cartItems) {

            Product product =
                    productRepository.findById(item.getProductId()).orElse(null);

            if (product == null) {
                return "redirect:/cart";
            }

            if (product.getStock() < item.getQuantity()) {
                return "redirect:/cart";
            }
        }

        double total = cartItems.stream()
                .mapToDouble(item ->
                        item.getPrice() * item.getQuantity())
                .sum();

        // Decrease product stock
        for (var item : cartItems) {

            Product product =
                    productRepository.findById(item.getProductId()).orElse(null);

            if (product != null) {
                product.setStock(
                        product.getStock() - item.getQuantity()
                );

                productRepository.save(product);
            }
        }

        Order order = new Order();

        order.setTotalAmount(total);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        orderRepository.save(order);

        cartItemRepository.deleteAll();

        return "redirect:/order/success";
    }

    @GetMapping("/order/success")
    public String success() {
        return "order-success";
    }
}