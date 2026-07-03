package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.GiftItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftItemRepository extends JpaRepository<GiftItem, Integer> {
}
