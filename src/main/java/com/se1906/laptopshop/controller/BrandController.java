package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Brand;
import com.se1906.laptopshop.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/brands")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BrandController {

    BrandService brandService;

    @GetMapping
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("brand", new Brand());
        return "admin/brand-list";
    }

    @PostMapping("/create")
    public String createBrand(@ModelAttribute Brand brand, RedirectAttributes redirectAttributes) {
        brandService.createBrand(brand);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm Brand thành công!");
        return "redirect:/admin/brands";
    }

    @PostMapping("/update/{id}")
    public String updateBrand(@PathVariable int id, @ModelAttribute Brand brand, RedirectAttributes redirectAttributes) {
        brandService.updateBrand(id, brand);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Brand thành công!");
        return "redirect:/admin/brands";
    }

    @PostMapping("/delete/{id}")
    public String deleteBrand(@PathVariable int id, RedirectAttributes redirectAttributes) {
        brandService.deleteBrand(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa Brand thành công!");
        return "redirect:/admin/brands";
    }
}
