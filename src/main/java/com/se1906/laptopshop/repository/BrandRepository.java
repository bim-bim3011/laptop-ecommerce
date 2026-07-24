package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    
    @org.springframework.data.jpa.repository.Query("SELECT b FROM Brand b WHERE :keyword IS NULL OR LOWER(b.brandName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    java.util.List<Brand> searchBrands(@org.springframework.data.repository.query.Param("keyword") String keyword);
}
