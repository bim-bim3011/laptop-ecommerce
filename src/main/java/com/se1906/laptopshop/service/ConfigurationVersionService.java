package com.se1906.laptopshop.service;

import com.se1906.laptopshop.dto.ConfigurationVersionRequest;
import com.se1906.laptopshop.entity.ConfigurationVersion;

import java.util.List;

public interface ConfigurationVersionService {
    List<ConfigurationVersion> getByLaptopId(Integer laptopId);
    ConfigurationVersion getById(Integer id);
    ConfigurationVersionRequest convertToRequest(ConfigurationVersion configurationVersion);
    void save(ConfigurationVersionRequest request);
    void delete(Integer id);
}
