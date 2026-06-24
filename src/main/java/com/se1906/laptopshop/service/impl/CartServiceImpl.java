package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Cart;
import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.CartItemRepository;
import com.se1906.laptopshop.repository.CartRepository;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import com.se1906.laptopshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ConfigurationVersionRepository cvRepository;

    @Override
    public Cart getCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            return cart;
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }
    }

    @Override
    @Transactional
    public void addToCart(User user, int configurationId, int quantity) {
        ConfigurationVersion cv = cvRepository.findByConfigurationId(configurationId);
        if (cv == null) {
            throw new IllegalArgumentException("Không tìm thấy cấu hình sản phẩm với ID: " + configurationId);
        }

        Cart cart = getCartByUser(user);

        CartItem existingItemOpt = cartItemRepository.findByCartAndConfigurationVersion(cart, cv);

        int targetQuantity = quantity;
        if (existingItemOpt != null) {
            targetQuantity += existingItemOpt.getQuantity();
        }

        if (targetQuantity > cv.getStockQuantity()) {
            throw new IllegalArgumentException("Số lượng yêu cầu vượt quá tồn kho hiện tại (" + cv.getStockQuantity() + ")");
        }

        if (existingItemOpt != null) {
            existingItemOpt.setQuantity(targetQuantity);
            cartItemRepository.save(existingItemOpt);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setConfigurationVersion(cv);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional
    public void removeCartItem(int itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(int itemId, String action) {
        CartItem cartItem = cartItemRepository.findByCartItemId(itemId);
        if (cartItem == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng với ID: " + itemId);
        }

        int newQty = cartItem.getQuantity();
        if ("increase".equalsIgnoreCase(action)) {
            newQty++;
        } else if ("decrease".equalsIgnoreCase(action)) {
            newQty--;
        }

        if (newQty <= 0) {
            cartItemRepository.delete(cartItem);
            return;
        }

        if (newQty > cartItem.getConfigurationVersion().getStockQuantity()) {
            throw new IllegalArgumentException("Số lượng yêu cầu vượt quá tồn kho hiện tại (" + cartItem.getConfigurationVersion().getStockQuantity() + ")");
        }

        cartItem.setQuantity(newQty);
        cartItemRepository.save(cartItem);
    }
}
