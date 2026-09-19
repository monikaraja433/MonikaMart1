package com.monikamart.controller;

import com.monikamart.model.User;
import com.monikamart.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {

        System.out.println("===== LOGIN START =====");
        System.out.println("EMAIL RECEIVED = [" + email + "]");

        Optional<User> userOptional =
                userRepository.findByEmail(email.trim());

        if (userOptional.isEmpty()) {
            System.out.println("❌ USER NOT FOUND");
            return "redirect:/login?error=true";
        }

        User user = userOptional.get();

        System.out.println("✅ USER FOUND = [" + user.getEmail() + "]");
        System.out.println("ROLE = [" + user.getRole() + "]");

        if (!user.getPassword().trim().equals(password.trim())) {
            System.out.println("❌ PASSWORD DOES NOT MATCH");
            return "redirect:/login?error=true";
        }

        System.out.println("✅ PASSWORD MATCHED");

        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRole());

        if (user.getRole() != null &&
                user.getRole().trim().equalsIgnoreCase("SELLER")) {

            System.out.println("➡️ SELLER → /seller/dashboard");
            return "redirect:/seller/dashboard";
        }

        System.out.println("➡️ USER → /");
        return "redirect:/";
    }
}