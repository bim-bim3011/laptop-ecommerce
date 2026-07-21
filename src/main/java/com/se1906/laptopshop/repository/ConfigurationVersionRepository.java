package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationVersionRepository extends JpaRepository<ConfigurationVersion, Integer> {
    ConfigurationVersion findByConfigurationId(int id);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(c.stockQuantity), 0) FROM ConfigurationVersion c")
    long countTotalInventory();
}
