package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
    Category createCategory(Category category);
    Category updateCategory(int id, Category category);
    void deleteCategory(int id);
}
