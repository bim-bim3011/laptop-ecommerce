package com.se1906.laptopshop.service;

import com.se1906.laptopshop.dto.LaptopRequest;
import com.se1906.laptopshop.entity.Laptop;
import org.springframework.data.domain.Page;

public interface LaptopService {
    Page<Laptop> getAllLaptops(String keyword, int page, int size);
    Laptop getLaptopById(Integer id);
    LaptopRequest convertToRequest(Laptop laptop);
    void saveLaptop(LaptopRequest request);
    void deleteLaptop(Integer id);
}
