package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.Cart;
import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByCartAndConfigurationVersion(Cart cart, ConfigurationVersion configurationVersion);

    CartItem findByCartItemId(int cartItemId);
}
