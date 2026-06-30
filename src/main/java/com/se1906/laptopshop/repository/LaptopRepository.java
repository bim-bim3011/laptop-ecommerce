package com.se1906.laptopshop.repository;


import com.se1906.laptopshop.entity.Laptop;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaptopRepository extends JpaRepository<Laptop, Integer> {
    Page<Laptop> findByLaptopNameContainingIgnoreCase(String keyword, Pageable pageable);
}
