package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Brand;
import com.se1906.laptopshop.repository.BrandRepository;
import com.se1906.laptopshop.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(int id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
    }

    @Override
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(int id, Brand brand) {
        Brand existing = getBrandById(id);
        existing.setBrandName(brand.getBrandName());
        return brandRepository.save(existing);
    }

    @Override
    public void deleteBrand(int id) {
        Brand existing = getBrandById(id);
        brandRepository.delete(existing);
    }

    @Override
    public List<Brand> searchBrands(String keyword) {
        return brandRepository.searchBrands(keyword);
    }
}
