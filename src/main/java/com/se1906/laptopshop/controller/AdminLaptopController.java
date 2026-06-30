package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.dto.LaptopRequest;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.repository.BrandRepository;
import com.se1906.laptopshop.repository.CategoryRepository;
import com.se1906.laptopshop.service.LaptopService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/laptops")
@AllArgsConstructor
public class AdminLaptopController {
    private LaptopService laptopService;
    private CategoryRepository categoryRepository;
    private BrandRepository brandRepository;


    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Page<Laptop> laptopPage = laptopService.getAllLaptops(keyword, page, 8);
        model.addAttribute("laptopPage", laptopPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "admin/laptop/list";
    }
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("laptopRequest", new LaptopRequest());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("pageTitle", "Add New Laptop");
        return "admin/laptop/form";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Laptop laptop = laptopService.getLaptopById(id);
        model.addAttribute("laptopRequest", laptopService.convertToRequest(laptop));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("pageTitle", "Edit Laptop");
        return "admin/laptop/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("laptopRequest") LaptopRequest request,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("pageTitle", request.getLaptopId() == null ? "Add New Laptop" : "Edit Laptop");
            return "admin/laptop/form";
        }
        laptopService.saveLaptop(request);
        redirectAttributes.addFlashAttribute("message",
                request.getLaptopId() == null ? "Laptop added successfully!" : "Laptop updated successfully!");
        return "redirect:/admin/laptops";
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("laptop", laptopService.getLaptopById(id));
        return "admin/laptop/view";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            laptopService.deleteLaptop(id);
            redirectAttributes.addFlashAttribute("message", "Laptop deleted successfully!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/admin/laptops";
    }
}
