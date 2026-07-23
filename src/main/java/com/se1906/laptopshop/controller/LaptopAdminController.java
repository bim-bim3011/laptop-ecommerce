package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.service.BrandService;
import com.se1906.laptopshop.service.CategoryService;
import com.se1906.laptopshop.service.CloudinaryService;
import com.se1906.laptopshop.service.LaptopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/laptops")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LaptopAdminController {

    LaptopService laptopService;
    BrandService brandService;
    CategoryService categoryService;
    CloudinaryService cloudinaryService;

    @GetMapping
    public String listLaptops(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "laptopId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        org.springframework.data.domain.Page<Laptop> page = laptopService.getAdminPaginatedLaptops(keyword, brandId, categoryId, pageNo, pageSize, sortField, sortDir);
        
        model.addAttribute("laptops", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("brandId", brandId);
        model.addAttribute("categoryId", categoryId);

        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/laptop-list";
    }

    @PostMapping("/create")
    public String createLaptop(@RequestParam("laptopName") String laptopName,
                               @RequestParam("description") String description,
                               @RequestParam("brandId") int brandId,
                               @RequestParam("categoryId") int categoryId,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            Laptop laptop = new Laptop();
            laptop.setLaptopName(laptopName);
            laptop.setDescription(description);
            laptop.setBrand(brandService.getBrandById(brandId));
            laptop.setCategory(categoryService.getCategoryById(categoryId));

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.upload(imageFile);
                laptop.setImageUrl(imageUrl);
            }

            laptopService.createLaptop(laptop);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm Laptop thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/laptops";
    }

    @PostMapping("/update/{id}")
    public String updateLaptop(@PathVariable int id,
                               @RequestParam("laptopName") String laptopName,
                               @RequestParam("description") String description,
                               @RequestParam("brandId") int brandId,
                               @RequestParam("categoryId") int categoryId,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            Laptop laptop = laptopService.getLaptopById(id);
            laptop.setLaptopName(laptopName);
            laptop.setDescription(description);
            laptop.setBrand(brandService.getBrandById(brandId));
            laptop.setCategory(categoryService.getCategoryById(categoryId));

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.upload(imageFile);
                laptop.setImageUrl(imageUrl);
            }

            laptopService.updateLaptop(id, laptop);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Laptop thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/laptops";
    }

    @PostMapping("/delete/{id}")
    public String deleteLaptop(@PathVariable int id, RedirectAttributes redirectAttributes) {
        laptopService.deleteLaptop(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa Laptop thành công!");
        return "redirect:/admin/laptops";
    }

    // ==================== CONFIGURATIONS ====================

    @GetMapping("/configs")
    public String listConfigs(Model model) {
        model.addAttribute("configs", laptopService.getAllConfigurations());
        model.addAttribute("laptops", laptopService.getAllLaptops());
        return "admin/config-list";
    }

    @PostMapping("/configs/create")
    public String createConfig(@RequestParam("laptopId") int laptopId,
                               @RequestParam("cpu") String cpu,
                               @RequestParam("ram") String ram,
                               @RequestParam("storage") String storage,
                               @RequestParam(value = "gpu", required = false) String gpu,
                               @RequestParam("price") BigDecimal price,
                               @RequestParam("stockQuantity") int stockQuantity,
                               RedirectAttributes redirectAttributes) {
        ConfigurationVersion config = new ConfigurationVersion();
        config.setCpu(cpu);
        config.setRam(ram);
        config.setStorage(storage);
        config.setGpu(gpu);
        config.setPrice(price);
        config.setStockQuantity(stockQuantity);
        laptopService.createConfiguration(laptopId, config);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm Configuration thành công!");
        return "redirect:/admin/laptops/configs";
    }

    @PostMapping("/configs/update/{configId}")
    public String updateConfig(@PathVariable int configId,
                               @RequestParam("cpu") String cpu,
                               @RequestParam("ram") String ram,
                               @RequestParam("storage") String storage,
                               @RequestParam(value = "gpu", required = false) String gpu,
                               @RequestParam("price") BigDecimal price,
                               @RequestParam("stockQuantity") int stockQuantity,
                               RedirectAttributes redirectAttributes) {
        ConfigurationVersion config = new ConfigurationVersion();
        config.setCpu(cpu);
        config.setRam(ram);
        config.setStorage(storage);
        config.setGpu(gpu);
        config.setPrice(price);
        config.setStockQuantity(stockQuantity);
        laptopService.updateConfiguration(configId, config);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Configuration thành công!");
        return "redirect:/admin/laptops/configs";
    }

    @PostMapping("/configs/delete/{configId}")
    public String deleteConfig(@PathVariable int configId, RedirectAttributes redirectAttributes) {
        laptopService.deleteConfiguration(configId);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa Configuration thành công!");
        return "redirect:/admin/laptops/configs";
    }
}
