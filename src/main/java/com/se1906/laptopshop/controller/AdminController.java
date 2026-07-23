package com.se1906.laptopshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminController {

    com.se1906.laptopshop.repository.OrderRepository orderRepository;
    com.se1906.laptopshop.repository.UserRepository userRepository;
    com.se1906.laptopshop.repository.ConfigurationVersionRepository configurationVersionRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalRevenue", orderRepository.calculateTotalRevenue());
        model.addAttribute("activeUsers", userRepository.countActiveUsers());
        model.addAttribute("totalInventory", configurationVersionRepository.countTotalInventory());
        model.addAttribute("totalOrders", orderRepository.countTotalOrders());

        return "admin-dashboard";
    }

    @GetMapping
    public String adminPage(Model model) {
        return dashboard(model);
    }
}
