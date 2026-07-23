package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationVersionRepository extends JpaRepository<ConfigurationVersion, Integer> {
    ConfigurationVersion findByConfigurationId(int id);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(c.stockQuantity), 0) FROM ConfigurationVersion c")
    long countTotalInventory();

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT c.cpu FROM ConfigurationVersion c WHERE c.cpu IS NOT NULL ORDER BY c.cpu ASC")
    java.util.List<String> findDistinctCpus();

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT c.ram FROM ConfigurationVersion c WHERE c.ram IS NOT NULL ORDER BY c.ram ASC")
    java.util.List<String> findDistinctRams();

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT c.storage FROM ConfigurationVersion c WHERE c.storage IS NOT NULL ORDER BY c.storage ASC")
    java.util.List<String> findDistinctStorages();

    @org.springframework.data.jpa.repository.Query("SELECT c FROM ConfigurationVersion c WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(c.laptop.laptopName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:cpu IS NULL OR :cpu = '' OR c.cpu = :cpu) " +
           "AND (:ram IS NULL OR :ram = '' OR c.ram = :ram) " +
           "AND (:storage IS NULL OR :storage = '' OR c.storage = :storage)")
    org.springframework.data.domain.Page<ConfigurationVersion> searchAndFilterConfigs(
            @org.springframework.data.repository.query.Param("keyword") String keyword, 
            @org.springframework.data.repository.query.Param("cpu") String cpu, 
            @org.springframework.data.repository.query.Param("ram") String ram, 
            @org.springframework.data.repository.query.Param("storage") String storage, 
            org.springframework.data.domain.Pageable pageable);
}
