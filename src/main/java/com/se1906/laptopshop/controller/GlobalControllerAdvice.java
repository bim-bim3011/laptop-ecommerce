package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Cart;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartService cartService;

    @ModelAttribute("cartItemCount")
    public int getCartItemCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart != null && cart.getCartItems() != null) {
                return cart.getCartItems().stream()
                           .mapToInt(item -> item.getQuantity())
                           .sum();
            }
        }
        return 0;
    }
}
