package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.Cart;
import com.se1906.laptopshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUser(User user);
}
