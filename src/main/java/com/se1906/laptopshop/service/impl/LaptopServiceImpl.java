package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.repository.LaptopRepository;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import com.se1906.laptopshop.service.LaptopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LaptopServiceImpl implements LaptopService {

    LaptopRepository laptopRepository;
    ConfigurationVersionRepository configurationVersionRepository;

    @Override
    public List<Laptop> getAllLaptops() {
        return laptopRepository.findAll();
    }

    @Override
    public List<Laptop> filterLaptops(Integer categoryId, Integer brandId, List<String> cpus, List<String> rams, List<String> storages, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String keyword) {
        return laptopRepository.findAll(com.se1906.laptopshop.repository.LaptopSpecification.filterLaptops(categoryId, brandId, cpus, rams, storages, minPrice, maxPrice, keyword));
    }

    @Override
    public Laptop getLaptopById(int id) {
        return laptopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Laptop not found"));
    }

    @Override
    public Laptop createLaptop(Laptop laptop) {
        return laptopRepository.save(laptop);
    }

    @Override
    public Laptop updateLaptop(int id, Laptop laptop) {
        Laptop existing = getLaptopById(id);
        existing.setLaptopName(laptop.getLaptopName());
        existing.setDescription(laptop.getDescription());
        existing.setImageUrl(laptop.getImageUrl());
        existing.setBrand(laptop.getBrand());
        existing.setCategory(laptop.getCategory());
        return laptopRepository.save(existing);
    }

    @Override
    public void deleteLaptop(int id) {
        Laptop existing = getLaptopById(id);
        laptopRepository.delete(existing);
    }

    @Override
    public List<ConfigurationVersion> getAllConfigurations() {
        return configurationVersionRepository.findAll();
    }

    @Override
    public List<ConfigurationVersion> getConfigurationsByLaptopId(int laptopId) {
        Laptop laptop = getLaptopById(laptopId);
        return laptop.getConfigurationVersions();
    }

    @Override
    public ConfigurationVersion createConfiguration(int laptopId, ConfigurationVersion configuration) {
        Laptop laptop = getLaptopById(laptopId);
        configuration.setLaptop(laptop);
        return configurationVersionRepository.save(configuration);
    }

    @Override
    public ConfigurationVersion updateConfiguration(int id, ConfigurationVersion configuration) {
        ConfigurationVersion existing = configurationVersionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found"));

        existing.setRam(configuration.getRam());
        existing.setStorage(configuration.getStorage());
        existing.setCpu(configuration.getCpu());
        existing.setGpu(configuration.getGpu());
        existing.setPrice(configuration.getPrice());
        existing.setStockQuantity(configuration.getStockQuantity());
        return configurationVersionRepository.save(existing);
    }

    @Override
    public void deleteConfiguration(int id) {
        ConfigurationVersion existing = configurationVersionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found"));
        configurationVersionRepository.delete(existing);
    }
}
