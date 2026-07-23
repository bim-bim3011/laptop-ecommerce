package com.se1906.laptopshop.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se1906.laptopshop.entity.Category;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.CategoryRepository;
import com.se1906.laptopshop.repository.BrandRepository;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import com.se1906.laptopshop.service.LaptopService;
import com.se1906.laptopshop.entity.Promotion;
import com.se1906.laptopshop.service.PromotionService;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({"/", "/home-page"})
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ConfigurationVersionRepository configRepo;

    @Autowired
    private LaptopService laptopService;
    
    @Autowired
    private PromotionService promotionService;

    /**
     * Xử lý luồng trang chủ, bao gồm hiển thị danh sách sản phẩm, lọc đa tiêu chí và tìm kiếm từ khóa.
     */
    @GetMapping()
    public String homePage(@RequestParam(name = "categoryId", required = false) Integer categoryId, 
                           @RequestParam(name = "brandId", required = false) Integer brandId,
                           @RequestParam(name = "cpu", required = false) List<String> cpus,
                           @RequestParam(name = "ram", required = false) List<String> rams,
                           @RequestParam(name = "storage", required = false) List<String> storages,
                           @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                           @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                           @RequestParam(name = "keyword", required = false) String keyword,
                           HttpSession session, Model model) {
        
        // Lấy thông tin user từ session nếu đã đăng nhập
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        
        // Load các khuyến mãi đang hoạt động
        LocalDateTime now = LocalDateTime.now();
        List<Promotion> activePromotions = promotionService.getAllPromotions().stream()
                .filter(p -> !now.isBefore(p.getStartDate()) && !now.isAfter(p.getEndDate()))
                .collect(Collectors.toList());
        model.addAttribute("activePromotions", activePromotions);

        // Lấy các danh sách để hiển thị lên Form Lọc
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("cpus", configRepo.findDistinctCpus());
        model.addAttribute("rams", configRepo.findDistinctRams());
        model.addAttribute("storages", configRepo.findDistinctStorages());

        // Gọi service lọc Laptop
        List<Laptop> laptops = laptopService.filterLaptops(categoryId, brandId, cpus, rams, storages, minPrice, maxPrice, keyword);
        model.addAttribute("laptops", laptops);

        // Giữ lại các giá trị đã chọn để hiển thị lại trên Form
        boolean isFiltering = (categoryId != null || brandId != null || (cpus != null && !cpus.isEmpty()) || (rams != null && !rams.isEmpty()) || (storages != null && !storages.isEmpty()) || minPrice != null || maxPrice != null || (keyword != null && !keyword.trim().isEmpty()));
        model.addAttribute("isFiltering", isFiltering);

        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("selectedCpus", cpus);
        model.addAttribute("selectedRams", rams);
        model.addAttribute("selectedStorages", storages);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("keyword", keyword);

        // Trả về file home-page.html trong thư mục templates
        return "home-page";
    }
}
