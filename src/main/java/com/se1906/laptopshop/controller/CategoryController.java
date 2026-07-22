package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Category;
import com.se1906.laptopshop.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryController {

    CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/category-list";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.createCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm Category thành công!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable int id, @ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.updateCategory(id, category);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Category thành công!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa Category thành công!");
        return "redirect:/admin/categories";
    }
}
