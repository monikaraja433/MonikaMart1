package com.monikamart.controller;

import com.monikamart.model.User;
import com.monikamart.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerDashboardController {

    private final UserRepository userRepository;

    public SellerDashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(HttpSession session, Model model) {

        Object userIdObject = session.getAttribute("userId");

        if (userIdObject == null) {
            return "redirect:/login";
        }

        Long userId = (Long) userIdObject;

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        String role = user.getRole();

        if (role == null || !role.trim().equalsIgnoreCase("SELLER")) {
            return "redirect:/";
        }

        session.setAttribute("userRole", role.trim());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userEmail", user.getEmail());

        model.addAttribute("userName", user.getFullName());

        return "seller-dashboard";
    }
}