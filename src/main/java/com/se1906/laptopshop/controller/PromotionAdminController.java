package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Promotion;
import com.se1906.laptopshop.service.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/promotions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PromotionAdminController {

    PromotionService promotionService;

    @GetMapping
    public String listPromotions(Model model) {
        model.addAttribute("promotions", promotionService.getAllPromotions());
        return "admin/promotion-list";
    }

    @PostMapping("/create")
    public String createPromotion(@ModelAttribute Promotion promotion, RedirectAttributes redirectAttributes) {
        promotionService.createPromotion(promotion);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm Promotion thành công!");
        return "redirect:/admin/promotions";
    }

    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable int id, @ModelAttribute Promotion promotion, RedirectAttributes redirectAttributes) {
        promotionService.updatePromotion(id, promotion);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Promotion thành công!");
        return "redirect:/admin/promotions";
    }

    @PostMapping("/delete/{id}")
    public String deletePromotion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        promotionService.deletePromotion(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa Promotion thành công!");
        return "redirect:/admin/promotions";
    }
}
