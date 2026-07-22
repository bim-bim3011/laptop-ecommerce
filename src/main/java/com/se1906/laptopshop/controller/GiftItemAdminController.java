package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.GiftItem;
import com.se1906.laptopshop.service.GiftItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/gift-items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GiftItemAdminController {

    GiftItemService giftItemService;

    @GetMapping
    public String listGiftItems(Model model) {
        model.addAttribute("giftItems", giftItemService.getAllGiftItems());
        return "admin/gift-item-list";
    }

    @PostMapping("/create")
    public String createGiftItem(@ModelAttribute GiftItem giftItem, RedirectAttributes redirectAttributes) {
        giftItemService.createGiftItem(giftItem);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm Gift Item thành công!");
        return "redirect:/admin/gift-items";
    }

    @PostMapping("/update/{id}")
    public String updateGiftItem(@PathVariable int id, @ModelAttribute GiftItem giftItem, RedirectAttributes redirectAttributes) {
        giftItemService.updateGiftItem(id, giftItem);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Gift Item thành công!");
        return "redirect:/admin/gift-items";
    }

    @PostMapping("/delete/{id}")
    public String deleteGiftItem(@PathVariable int id, RedirectAttributes redirectAttributes) {
        giftItemService.deleteGiftItem(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa Gift Item thành công!");
        return "redirect:/admin/gift-items";
    }
}
