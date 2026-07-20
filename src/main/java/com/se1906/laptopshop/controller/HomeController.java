package com.se1906.laptopshop.controller;

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
import com.se1906.laptopshop.repository.LaptopRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({"/", "/home-page"})
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    @GetMapping()
    public String homePage(@RequestParam(name = "categoryId", required = false) Integer categoryId, 
                           @RequestParam(name = "keyword", required = false) String keyword,
                           HttpSession session, Model model) {
        // Lấy thông tin user từ session nếu đã đăng nhập
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        List<Laptop> laptops;
        if (categoryId != null) {
            laptops = laptopRepository.findByCategory_CategoryId(categoryId);
            model.addAttribute("selectedCategoryId", categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            laptops = laptopRepository.findByLaptopNameContainingIgnoreCase(keyword.trim());
            model.addAttribute("keyword", keyword.trim());
        } else {
            laptops = laptopRepository.findAll();
        }
        model.addAttribute("laptops", laptops);

        // Trả về file home-page.html trong thư mục templates
        return "home-page";
    }
}
