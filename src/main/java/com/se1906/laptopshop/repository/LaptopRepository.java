package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Integer> {
    List<Laptop> findByCategory_CategoryId(int categoryId);
    List<Laptop> findByLaptopNameContainingIgnoreCase(String keyword);
}
