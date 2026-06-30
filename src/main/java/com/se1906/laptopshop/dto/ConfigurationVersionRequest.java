package com.se1906.laptopshop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ConfigurationVersionRequest {
    private Integer configurationId; // null = add new, has value = update

    @NotNull(message = "Laptop is required")
    private Integer laptopId;

    @NotBlank(message = "CPU must not be empty")
    private String cpu;

    @NotBlank(message = "RAM must not be empty")
    private String ram;

    @NotBlank(message = "Storage must not be empty")
    private String storage;

    private String gpu; // optional nullable=false

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be >= 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be >= 0")
    private Integer stockQuantity;
}
