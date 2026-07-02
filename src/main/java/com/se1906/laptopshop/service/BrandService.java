package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.Brand;
import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    Brand getBrandById(int id);
    Brand createBrand(Brand brand);
    Brand updateBrand(int id, Brand brand);
    void deleteBrand(int id);
}
