package com.se1906.laptopshop.controller;


import com.se1906.laptopshop.dto.ConfigurationVersionRequest;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.service.ConfigurationVersionService;
import com.se1906.laptopshop.service.LaptopService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/laptops/{laptopId}/configurations")
@AllArgsConstructor
public class AdminConfigurationVersionController {
    private ConfigurationVersionService configurationVersionService;
    private LaptopService laptopService;


    @GetMapping("/new")
    public String createForm(@PathVariable Integer laptopId, Model model) {
        Laptop laptop = laptopService.getLaptopById(laptopId);

        ConfigurationVersionRequest request = new ConfigurationVersionRequest();
        request.setLaptopId(laptopId);

        model.addAttribute("configurationRequest", request);
        model.addAttribute("laptop", laptop);
        model.addAttribute("pageTitle", "Add Configuration Version");
        return "admin/configuration/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer laptopId, @PathVariable Integer id, Model model , RedirectAttributes redirectAttributes) {
        try {
            Laptop laptop = laptopService.getLaptopById(laptopId);
            var cv = configurationVersionService.getById(id);

            model.addAttribute("configurationRequest", configurationVersionService.convertToRequest(cv));
            model.addAttribute("laptop", laptop);
            model.addAttribute("pageTitle", "Edit Configuration Version");
            return "admin/configuration/form";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/laptops/view/" + laptopId;
        }
    }

    @PostMapping("/save")
    public String save(@PathVariable Integer laptopId,
                       @Valid @ModelAttribute("configurationRequest") ConfigurationVersionRequest request,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        request.setLaptopId(laptopId); // luôn lấy laptopId từ URL, không tin giá trị client gửi lên

        if (bindingResult.hasErrors()) {
            model.addAttribute("laptop", laptopService.getLaptopById(laptopId));
            model.addAttribute("pageTitle", request.getConfigurationId() == null ? "Add Configuration Version" : "Edit Configuration Version");
            return "admin/configuration/form";
        }

        configurationVersionService.save(request);
        redirectAttributes.addFlashAttribute("message",
                request.getConfigurationId() == null ? "Configuration added successfully!" : "Configuration updated successfully!");
        return "redirect:/admin/laptops/view/" + laptopId;
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer laptopId, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            configurationVersionService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Configuration deleted successfully!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/admin/laptops/view/" + laptopId;
    }

}
