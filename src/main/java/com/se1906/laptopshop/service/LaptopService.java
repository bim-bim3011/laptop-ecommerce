package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import java.util.List;

public interface LaptopService {
    List<Laptop> getAllLaptops();
    List<Laptop> filterLaptops(Integer categoryId, Integer brandId, List<String> cpus, List<String> rams, List<String> storages, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String keyword);
    Laptop getLaptopById(int id);
    Laptop createLaptop(Laptop laptop);
    Laptop updateLaptop(int id, Laptop laptop);
    void deleteLaptop(int id);

    List<ConfigurationVersion> getAllConfigurations();
    List<ConfigurationVersion> getConfigurationsByLaptopId(int laptopId);
    ConfigurationVersion createConfiguration(int laptopId, ConfigurationVersion configuration);
    ConfigurationVersion updateConfiguration(int id, ConfigurationVersion configuration);
    void deleteConfiguration(int id);
}
