package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.dto.ConfigurationVersionRequest;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import com.se1906.laptopshop.repository.LaptopRepository;
import com.se1906.laptopshop.repository.OrderDetailRepository;
import com.se1906.laptopshop.service.ConfigurationVersionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ConfigurationVersionServiceImpl implements ConfigurationVersionService {
    private ConfigurationVersionRepository configurationVersionRepository;
    private LaptopRepository laptopRepository;
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<ConfigurationVersion> getByLaptopId(Integer laptopId) {
        return configurationVersionRepository.findByLaptop_LaptopId(laptopId);
    }

    @Override
    public ConfigurationVersion getById(Integer id) {
        return configurationVersionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuration version not found with id = " + id));
    }

    @Override
    public ConfigurationVersionRequest convertToRequest(ConfigurationVersion cv) {
        ConfigurationVersionRequest request = new ConfigurationVersionRequest();
        request.setConfigurationId(cv.getConfigurationId());
        request.setLaptopId(cv.getLaptop() != null ? cv.getLaptop().getLaptopId() : null);
        request.setCpu(cv.getCpu());
        request.setRam(cv.getRam());
        request.setStorage(cv.getStorage());
        request.setGpu(cv.getGpu());
        request.setPrice(cv.getPrice());
        request.setStockQuantity(cv.getStockQuantity());
        return request;
    }

    @Override
    public void save(ConfigurationVersionRequest request) {
        ConfigurationVersion cv = (request.getConfigurationId() != null)
                ? getById(request.getConfigurationId())
                : new ConfigurationVersion();

        Laptop laptop = laptopRepository.findById(request.getLaptopId())
                .orElseThrow(() -> new EntityNotFoundException("Laptop not found with id = " + request.getLaptopId()));

        cv.setLaptop(laptop);
        cv.setCpu(request.getCpu());
        cv.setRam(request.getRam());
        cv.setStorage(request.getStorage());
        cv.setGpu(request.getGpu());
        cv.setPrice(request.getPrice());
        cv.setStockQuantity(request.getStockQuantity());

        configurationVersionRepository.save(cv);
    }

    @Override
    public void delete(Integer id) {
        if (!configurationVersionRepository.existsById(id)) {
            throw new EntityNotFoundException("Configuration version not found with id = " + id);
        }
        if (orderDetailRepository.existsByConfigurationVersion_ConfigurationId(id)) {
            throw new IllegalStateException("Cannot delete this configuration because it already has order history");
        }
        configurationVersionRepository.deleteById(id);
    }
}
