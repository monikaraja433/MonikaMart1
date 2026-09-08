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
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {

            User user = userOptional.get();

            if (user.getPassword().equals(password)) {

                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getFullName());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());

                return "redirect:/";
            }
        }

        return "redirect:/login?error=true";
    }
}


