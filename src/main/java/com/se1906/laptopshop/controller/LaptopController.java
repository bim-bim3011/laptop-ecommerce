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

    @GetMapping("/test-laptops")
    public String testLaptopsPage(Model model) {
        List<ConfigurationVersion> configurations = cvRepository.findAll();
        model.addAttribute("configurations", configurations);
        return "test-laptops";
    }
}

