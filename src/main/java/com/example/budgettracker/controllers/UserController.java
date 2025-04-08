package com.example.budgettracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.budgettracker.model.User;
import com.example.budgettracker.singleton.UserManager;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserManager userManager = UserManager.getInstance();

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role) {
        if (userManager.registerUser(username, password, role)) {
            return "User registered successfully!";
        } else {
            return "Username already taken!";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        User user = userManager.authenticateUser(username, password);
        if (user != null) {
            // ✅ Redirect to transaction page with username
            return "redirect:/transactions?username=" + username;
        } else {
            // ❌ Return to login form with error (assuming you have a login.html)
            return "redirect:/login?error=true";
        }
    }
}
