package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.service.LaptopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.se1906.laptopshop.service.CloudinaryService;

import java.util.List;

@RestController
@RequestMapping("/admin/api/laptops")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LaptopAdminController {

    LaptopService laptopService;
    CloudinaryService cloudinaryService;

    @GetMapping
    public List<Laptop> getAllLaptops() {
        return laptopService.getAllLaptops();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laptop> getLaptopById(@PathVariable int id) {
        return ResponseEntity.ok(laptopService.getLaptopById(id));
    }

    @PostMapping
    public ResponseEntity<Laptop> createLaptop(@RequestBody Laptop laptop) {
        return ResponseEntity.ok(laptopService.createLaptop(laptop));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Laptop> updateLaptop(@PathVariable int id, @RequestBody Laptop laptop) {
        return ResponseEntity.ok(laptopService.updateLaptop(id, laptop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaptop(@PathVariable int id) {
        laptopService.deleteLaptop(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Laptop> uploadImage(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.upload(file);
            Laptop laptop = laptopService.getLaptopById(id);
            laptop.setImageUrl(imageUrl);
            laptopService.updateLaptop(id, laptop);
            return ResponseEntity.ok(laptop);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/configurations")
    public List<ConfigurationVersion> getAllConfigurations() {
        return laptopService.getAllConfigurations();
    }

    @GetMapping("/{id}/configurations")
    public List<ConfigurationVersion> getConfigurationsByLaptopId(@PathVariable int id) {
        return laptopService.getConfigurationsByLaptopId(id);
    }

    @PostMapping("/{id}/configurations")
    public ResponseEntity<ConfigurationVersion> createConfiguration(@PathVariable int id, @RequestBody ConfigurationVersion configuration) {
        return ResponseEntity.ok(laptopService.createConfiguration(id, configuration));
    }

    @PutMapping("/configurations/{configId}")
    public ResponseEntity<ConfigurationVersion> updateConfiguration(@PathVariable int configId, @RequestBody ConfigurationVersion configuration) {
        return ResponseEntity.ok(laptopService.updateConfiguration(configId, configuration));
    }

    @DeleteMapping("/configurations/{configId}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable int configId) {
        laptopService.deleteConfiguration(configId);
        return ResponseEntity.noContent().build();
    }
}
