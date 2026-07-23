package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GiftDetailRepository extends JpaRepository<GiftDetail, Integer> {
    List<GiftDetail> findByConfigurationVersion_ConfigurationId(Integer configurationId);
    @Transactional
    void deleteByLaptop(Laptop laptop);
}
