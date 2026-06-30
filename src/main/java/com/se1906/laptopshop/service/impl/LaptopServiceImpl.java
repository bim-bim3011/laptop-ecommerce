package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.dto.LaptopRequest;
import com.se1906.laptopshop.entity.Brand;
import com.se1906.laptopshop.entity.Category;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.repository.BrandRepository;
import com.se1906.laptopshop.repository.CategoryRepository;
import com.se1906.laptopshop.repository.LaptopRepository;
import com.se1906.laptopshop.repository.OrderDetailRepository;
import com.se1906.laptopshop.service.CloudinaryService;
import com.se1906.laptopshop.service.LaptopService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LaptopServiceImpl implements LaptopService {
    private LaptopRepository laptopRepository;
    private CategoryRepository categoryRepository;
    private BrandRepository brandRepository;
    private OrderDetailRepository orderDetailRepository;
    private CloudinaryService cloudinaryService;
    @Override
    public Page<Laptop> getAllLaptops(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("laptopId").descending());
        if (keyword != null && !keyword.isBlank()) {
            return laptopRepository.findByLaptopNameContainingIgnoreCase(keyword, pageable);
        }
        return laptopRepository.findAll(pageable);
    }

    @Override
    public Laptop getLaptopById(Integer id) {
        return laptopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laptop not found with id = " + id));
    }

    @Override
    public LaptopRequest convertToRequest(Laptop laptop) {
        LaptopRequest request = new LaptopRequest();
        request.setLaptopId(laptop.getLaptopId());
        request.setLaptopName(laptop.getLaptopName());
        request.setDescription(laptop.getDescription());
        request.setCurrentImageUrl(laptop.getImageUrl());
        if (laptop.getCategory() != null) request.setCategoryId(laptop.getCategory().getCategoryId());
        if (laptop.getBrand() != null) request.setBrandId(laptop.getBrand().getBrandId());
        return request;
    }

    @Override
    public void saveLaptop(LaptopRequest request) {
        Laptop laptop = (request.getLaptopId() != null)
                ? getLaptopById(request.getLaptopId())
                : new Laptop();

        laptop.setLaptopName(request.getLaptopName());
        laptop.setDescription(request.getDescription());

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(request.getImageFile(), "Laptop");
            laptop.setImageUrl(imageUrl);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));

        laptop.setCategory(category);
        laptop.setBrand(brand);

        laptopRepository.save(laptop);
    }

    @Override
    public void deleteLaptop(Integer id) {
        if (!laptopRepository.existsById(id)) {
            throw new EntityNotFoundException("Laptop not found with id = " + id);
        }
        if (orderDetailRepository.existsByConfigurationVersion_Laptop_LaptopId(id)) {
            throw new IllegalStateException("Cannot delete this laptop because one of its configurations already has order history");
        }
        laptopRepository.deleteById(id);
    }
}
