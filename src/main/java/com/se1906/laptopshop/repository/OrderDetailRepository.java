package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    boolean existsByConfigurationVersion_ConfigurationId(Integer configurationId);
    boolean existsByConfigurationVersion_Laptop_LaptopId(Integer laptopId);
}
