package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.repository.LaptopRepository;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import com.se1906.laptopshop.service.GiftDetailService;
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
    GiftDetailService giftDetailService;

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
    public org.springframework.data.domain.Page<Laptop> getAdminPaginatedLaptops(String keyword, Integer brandId, Integer categoryId, int pageNo, int pageSize, String sortField, String sortDir) {
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase(org.springframework.data.domain.Sort.Direction.ASC.name()) ? 
                org.springframework.data.domain.Sort.by(sortField).ascending() : 
                org.springframework.data.domain.Sort.by(sortField).descending();
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNo - 1, pageSize, sort);
        return laptopRepository.searchAndFilterLaptops(keyword, brandId, categoryId, pageable);
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
    public ConfigurationVersion createConfiguration(int laptopId, ConfigurationVersion configuration , List<Integer> selectedGifts) {
        Laptop laptop = getLaptopById(laptopId);
        configuration.setLaptop(laptop);
        ConfigurationVersion savedConfig = configurationVersionRepository.save(configuration);
        giftDetailService.saveGiftsForConfig(laptop, savedConfig, selectedGifts);
        return savedConfig;
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

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public org.springframework.data.domain.Page<ConfigurationVersion> getAdminPaginatedConfigs(String keyword, String cpu, String ram, String storage, int pageNo, int pageSize, String sortField, String sortDir) {
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase(org.springframework.data.domain.Sort.Direction.ASC.name()) ? 
                org.springframework.data.domain.Sort.by(sortField).ascending() : 
                org.springframework.data.domain.Sort.by(sortField).descending();
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNo - 1, pageSize, sort);
        org.springframework.data.domain.Page<ConfigurationVersion> page = configurationVersionRepository.searchAndFilterConfigs(keyword, cpu, ram, storage, pageable);
        // Khởi tạo LAZY collections để tránh LazyInitializationException ở view
        for (ConfigurationVersion cv : page.getContent()) {
            cv.getGiftDetails().size();
            if (cv.getLaptop() != null) {
                cv.getLaptop().getLaptopName();
            }
        }
        return page;
    }

    @Override
    public List<String> getDistinctCpus() {
        return configurationVersionRepository.findDistinctCpus();
    }

    @Override
    public List<String> getDistinctRams() {
        return configurationVersionRepository.findDistinctRams();
    }

    @Override
    public List<String> getDistinctStorages() {
        return configurationVersionRepository.findDistinctStorages();
    }
}
