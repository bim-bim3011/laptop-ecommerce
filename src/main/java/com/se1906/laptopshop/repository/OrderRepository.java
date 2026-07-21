package com.se1906.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    @org.springframework.data.jpa.repository.Query("SELECT SUM(o.totalAmount) FROM Order o")
    java.math.BigDecimal sumTotalRevenue();
    
    long countByStatus(String status);
}
