package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import com.se1906.laptopshop.repository.GiftItemRepository;
import com.se1906.laptopshop.repository.LaptopRepository;
import com.se1906.laptopshop.service.BrandService;
import com.se1906.laptopshop.service.CategoryService;
import com.se1906.laptopshop.service.CloudinaryService;
import com.se1906.laptopshop.service.LaptopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/laptops")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LaptopAdminController {

    LaptopService laptopService;
    BrandService brandService;
    CategoryService categoryService;
    CloudinaryService cloudinaryService;
    GiftItemRepository giftItemRepository;
    ConfigurationVersionRepository configurationVersionRepository;

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
    public String listConfigs(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String cpu,
            @RequestParam(required = false, defaultValue = "") String ram,
            @RequestParam(required = false, defaultValue = "") String storage,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "configurationId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        org.springframework.data.domain.Page<ConfigurationVersion> page = laptopService.getAdminPaginatedConfigs(keyword, cpu, ram, storage, pageNo, pageSize, sortField, sortDir);
        
        model.addAttribute("configs", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("cpu", cpu);
        model.addAttribute("ram", ram);
        model.addAttribute("storage", storage);

        model.addAttribute("distinctCpus", laptopService.getDistinctCpus());
        model.addAttribute("distinctRams", laptopService.getDistinctRams());
        model.addAttribute("distinctStorages", laptopService.getDistinctStorages());

        model.addAttribute("laptops", laptopService.getAllLaptops());
        model.addAttribute("allGifts", giftItemRepository.findAll());
        return "admin/config-list";
    }

    @PostMapping("/configs/create")
    public String createConfig(@jakarta.validation.Valid @ModelAttribute com.se1906.laptopshop.dto.ConfigDTO dto,
                               org.springframework.validation.BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().stream()
                    .map(org.springframework.validation.ObjectError::getDefaultMessage)
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("Dữ liệu không hợp lệ");
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi thêm mới: " + errorMsg);
            return "redirect:/admin/laptops/configs";
        }
        
        ConfigurationVersion config = new ConfigurationVersion();
        config.setCpu(dto.getCpu());
        config.setRam(dto.getRam());
        config.setStorage(dto.getStorage());
        config.setGpu(dto.getGpu());
        config.setPrice(dto.getPrice());
        config.setStockQuantity(dto.getStockQuantity());
        List<Integer> selectedGiftIds = dto.getSelectedGifts() != null ? dto.getSelectedGifts() : new java.util.ArrayList<>();
        laptopService.createConfiguration(dto.getLaptopId(), config, selectedGiftIds);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm Configuration thành công!");
        return "redirect:/admin/laptops/configs";
    }

    @PostMapping("/configs/update/{configId}")
    @Transactional
    public String updateConfig(@PathVariable int configId,
                               @jakarta.validation.Valid @ModelAttribute com.se1906.laptopshop.dto.ConfigDTO dto,
                               org.springframework.validation.BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // laptopId is not required for update, so filter out laptopId error if present
            boolean hasRealErrors = result.getFieldErrors().stream().anyMatch(e -> !e.getField().equals("laptopId"));
            if (hasRealErrors) {
                String errorMsg = result.getFieldErrors().stream()
                        .filter(e -> !e.getField().equals("laptopId"))
                        .map(org.springframework.validation.FieldError::getDefaultMessage)
                        .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                        .orElse("Dữ liệu không hợp lệ");
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cập nhật: " + errorMsg);
                return "redirect:/admin/laptops/configs";
            }
        }

        ConfigurationVersion config = configurationVersionRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cấu hình có ID: " + configId));
        config.setCpu(dto.getCpu());
        config.setRam(dto.getRam());
        config.setStorage(dto.getStorage());
        config.setGpu(dto.getGpu());
        config.setPrice(dto.getPrice());
        config.setStockQuantity(dto.getStockQuantity());
        if (dto.getLaptopId() != null) {
            config.setLaptop(laptopService.getLaptopById(dto.getLaptopId()));
        }
        if (config.getGiftDetails() != null) {
            config.getGiftDetails().clear();
        } else {
            config.setGiftDetails(new java.util.ArrayList<>());
        }
        if (dto.getSelectedGifts() != null && !dto.getSelectedGifts().isEmpty()) {
            for (Integer giftId : dto.getSelectedGifts()) {
                com.se1906.laptopshop.entity.GiftDetail detail = new com.se1906.laptopshop.entity.GiftDetail();
                detail.setConfigurationVersion(config);
                detail.setGiftItem(giftItemRepository.findById(giftId).orElse(null));
                config.getGiftDetails().add(detail);
            }
        }
        configurationVersionRepository.save(config);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Configuration thành công!");
        return "redirect:/admin/laptops/configs";
    }

    @PostMapping("/configs/delete/{configId}")
    public String deleteConfig(@PathVariable int configId, RedirectAttributes redirectAttributes) {
        laptopService.deleteConfiguration(configId);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa Configuration thành công!");
        return "redirect:/admin/laptops/configs";
    }
    @ExceptionHandler(Exception.class)
    @org.springframework.web.bind.annotation.ResponseBody
    public String handleAllExceptions(Exception ex) {
        java.io.StringWriter sw = new java.io.StringWriter();
        ex.printStackTrace(new java.io.PrintWriter(sw));
        return "<pre>" + sw.toString() + "</pre>";
    }
    // ========================================================
// 1. HIỂN THỊ TRANG SỬA VỚI DỮ LIỆU CŨ (MỞ FILE config-edit.html)
// ========================================================
    @GetMapping("/configs/edit/{id}")
    @Transactional(readOnly = true)
    public String showEditConfigForm(@PathVariable("id") int id, Model model) {
        // Tìm cấu hình cũ theo ID, nếu không thấy thì báo lỗi
        ConfigurationVersion config = configurationVersionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cấu hình có ID: " + id));

        // Đẩy thông tin cấu hình hiện tại sang form
        model.addAttribute("config", config);

        // Load danh sách tất cả các Laptop để hiển thị ở ô Select Option chọn dòng máy
        model.addAttribute("laptops", laptopService.getAllLaptops());
        model.addAttribute("allGifts", giftItemRepository.findAll());


        // Trả về file config-edit.html bác vừa tạo ở bước trước
        return "admin/config-edit";
    }


}
