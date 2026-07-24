package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Integer>, JpaSpecificationExecutor<Laptop> {
    List<Laptop> findByCategory_CategoryId(int categoryId);
    List<Laptop> findByLaptopNameContainingIgnoreCase(String keyword);

    @org.springframework.data.jpa.repository.Query("SELECT l FROM Laptop l WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(l.laptopName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:brandId IS NULL OR l.brand.brandId = :brandId) " +
           "AND (:categoryId IS NULL OR l.category.categoryId = :categoryId)")
    org.springframework.data.domain.Page<Laptop> searchAndFilterLaptops(
            @org.springframework.data.repository.query.Param("keyword") String keyword, 
            @org.springframework.data.repository.query.Param("brandId") Integer brandId, 
            @org.springframework.data.repository.query.Param("categoryId") Integer categoryId, 
            org.springframework.data.domain.Pageable pageable);
}
