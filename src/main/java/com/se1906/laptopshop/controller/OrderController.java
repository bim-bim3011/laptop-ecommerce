package com.se1906.laptopshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.OrderRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;



    @GetMapping("/orders")
    public String viewOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        
        model.addAttribute("orders", orders);
        
        return "order-history";
    }
}
