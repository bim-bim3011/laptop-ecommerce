package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.Cart;
import com.se1906.laptopshop.entity.User;

public interface CartService {
    Cart getCartByUser(User user);
    void addToCart(User user, int configurationId, int quantity);
    void removeCartItem(int itemId);
    void updateCartItemQuantity(int itemId, String action);
    void clearCart(User user);
}
