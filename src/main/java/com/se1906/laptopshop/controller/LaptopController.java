package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LaptopController {

    @Autowired
    private ConfigurationVersionRepository cvRepository;

    @Autowired
    private com.se1906.laptopshop.repository.LaptopRepository laptopRepository;

    @GetMapping("/laptop/{id}")
    public String laptopDetailPage(@org.springframework.web.bind.annotation.PathVariable("id") int id, Model model, jakarta.servlet.http.HttpSession session) {
        com.se1906.laptopshop.entity.User user = (com.se1906.laptopshop.entity.User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        com.se1906.laptopshop.entity.Laptop laptop = laptopRepository.findById(id).orElse(null);
        if (laptop == null) {
            return "redirect:/home-page";
        }
        
        model.addAttribute("laptop", laptop);

        // Fetch similar laptops (same category, exclude current)
        List<com.se1906.laptopshop.entity.Laptop> similarLaptops = new java.util.ArrayList<>();
        if (laptop.getCategory() != null) {
            similarLaptops = laptopRepository.findByCategory_CategoryId(laptop.getCategory().getCategoryId())
                    .stream()
                    .filter(l -> l.getLaptopId() != id)
                    .limit(4)
                    .toList();
            
            // Initialize lazy collections for similar laptops to avoid lazy loading issues in view
            for (com.se1906.laptopshop.entity.Laptop sim : similarLaptops) {
                if (sim.getConfigurationVersions() != null) {
                    sim.getConfigurationVersions().size();
                }
            }
        }
        model.addAttribute("similarLaptops", similarLaptops);

        List<com.se1906.laptopshop.entity.GiftDetail> giftDetails = new java.util.ArrayList<>();
        java.util.Set<Integer> addedGiftItemIds = new java.util.HashSet<>();
        try {
            if (laptop.getConfigurationVersions() != null && !laptop.getConfigurationVersions().isEmpty()) {
                for (com.se1906.laptopshop.entity.ConfigurationVersion config : laptop.getConfigurationVersions()) {
                    if (config.getGiftDetails() != null) {
                        for (com.se1906.laptopshop.entity.GiftDetail gd : config.getGiftDetails()) {
                            if (gd.getGiftItem() != null) {
                                gd.getGiftItem().getItemName(); // trigger fetch
                                Integer itemId = gd.getGiftItem().getGiftItemId();
                                if (!addedGiftItemIds.contains(itemId)) {
                                    giftDetails.add(gd);
                                    addedGiftItemIds.add(itemId);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching gift details: " + e.getMessage());
            e.printStackTrace();
            giftDetails = new java.util.ArrayList<>(); // Reset on error
        }
        model.addAttribute("giftDetails", giftDetails);

        return "laptop-detail";
    }

    @GetMapping("/test-laptops")
    public String testLaptopsPage(Model model) {
        List<ConfigurationVersion> configurations = cvRepository.findAll();
        model.addAttribute("configurations", configurations);
        return "test-laptops";
    }
}

