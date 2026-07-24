package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.service.UserService;
import org.springframework.data.domain.Page;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserAdminController {

    UserService userService;

    @GetMapping
    public String listUsers(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "") String roleName,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "userId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Page<User> page = userService.getPaginatedUsers(keyword, status, roleName, pageNo, pageSize, sortField, sortDir);
        
        model.addAttribute("users", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("roleName", roleName);
        
        return "admin/user-list";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Khóa User thành công!");
        return "redirect:/admin/users";
    }
}
