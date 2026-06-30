package com.se1906.laptopshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LaptopRequest {
    private Integer laptopId; // null = thêm mới, có giá trị = cập nhật

    @NotBlank(message = "Laptop name must not be empty")
    private String laptopName;

    private String description;

    @NotNull(message = "Please select a category")
    private Integer categoryId;

    @NotNull(message = "Please select a brand")
    private Integer brandId;
    private MultipartFile imageFile;
    private String currentImageUrl;
}
