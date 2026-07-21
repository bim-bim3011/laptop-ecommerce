package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.service.GiftDetailService;
import com.se1906.laptopshop.service.GiftItemService;
import com.se1906.laptopshop.service.LaptopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/gift-details")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GiftDetailAdminController {

    GiftDetailService giftDetailService;
    GiftItemService giftItemService;
    LaptopService laptopService;

    @GetMapping("/{configId}")
    public String listGiftDetails(@PathVariable int configId, Model model) {
        model.addAttribute("giftDetails", giftDetailService.getGiftsByConfiguration(configId));
        model.addAttribute("giftItems", giftItemService.getAllGiftItems());
        model.addAttribute("configId", configId);
        return "admin/gift-detail-list";
    }

    @PostMapping("/{configId}/add")
    public String addGiftToConfig(@PathVariable int configId,
                                  @RequestParam("giftItemId") int giftItemId,
                                  @RequestParam("quantity") int quantity,
                                  RedirectAttributes redirectAttributes) {
        giftDetailService.addGiftToConfiguration(configId, giftItemId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm quà tặng thành công!");
        return "redirect:/admin/gift-details/" + configId;
    }

    @PostMapping("/remove/{giftDetailId}")
    public String removeGiftFromConfig(@PathVariable int giftDetailId,
                                       @RequestParam("configId") int configId,
                                       RedirectAttributes redirectAttributes) {
        giftDetailService.removeGiftFromConfiguration(giftDetailId);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa quà tặng thành công!");
        return "redirect:/admin/gift-details/" + configId;
    }
}
