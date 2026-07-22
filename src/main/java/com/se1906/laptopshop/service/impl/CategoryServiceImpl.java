package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Category;
import com.se1906.laptopshop.repository.CategoryRepository;
import com.se1906.laptopshop.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(int id, Category category) {
        Category existing = getCategoryById(id);
        existing.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(int id) {
        Category existing = getCategoryById(id);
        categoryRepository.delete(existing);
    }
}
