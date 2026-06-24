package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home-page")
public class HomeController {

    @GetMapping()
    public String homePage(HttpSession session, Model model) {
        // Lấy thông tin user từ session nếu đã đăng nhập
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        
        // Trả về file home-page.html trong thư mục templates
        return "home-page";
    }
}
