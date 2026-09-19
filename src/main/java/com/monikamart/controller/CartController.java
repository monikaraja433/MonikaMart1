package com.monikamart.controller;

import com.monikamart.model.CartItem;
import com.monikamart.model.Product;
import com.monikamart.repository.CartItemRepository;
import com.monikamart.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartController(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository) {

        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String viewCart(Model model) {

        var cartItems = cartItemRepository.findAll();

        double total = cartItems.stream()
                .mapToDouble(item ->
                        item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }

    @GetMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam String productName,
            @RequestParam double price) {

        Product product =
                productRepository.findById(productId).orElse(null);

        if (product == null || product.getStock() <= 0) {
            return "redirect:/products";
        }

        CartItem item = new CartItem(
                productId,
                productName,
                price,
                1
        );

        cartItemRepository.save(item);

        return "redirect:/cart";
    }

    @GetMapping("/increase/{id}")
    public String increase(@PathVariable Long id) {

        CartItem item =
                cartItemRepository.findById(id).orElse(null);

        if (item != null) {

            Product product =
                    productRepository.findById(item.getProductId()).orElse(null);

            if (product != null &&
                    item.getQuantity() < product.getStock()) {

                item.setQuantity(item.getQuantity() + 1);
                cartItemRepository.save(item);
            }
        }

        return "redirect:/cart";
    }

    @GetMapping("/decrease/{id}")
    public String decrease(@PathVariable Long id) {

        CartItem item =
                cartItemRepository.findById(id).orElse(null);

        if (item != null) {

            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                cartItemRepository.save(item);
            } else {
                cartItemRepository.delete(item);
            }
        }

        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {

        cartItemRepository.deleteById(id);

        return "redirect:/cart";
    }
}