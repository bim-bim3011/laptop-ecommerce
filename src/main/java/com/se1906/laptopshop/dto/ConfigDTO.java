package com.se1906.laptopshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class ConfigDTO {
    
    private Integer laptopId;

    @NotBlank(message = "CPU không được để trống")
    @Length(min = 2, message = "CPU phải có ít nhất 2 ký tự")
    private String cpu;

    @NotBlank(message = "RAM không được để trống")
    @Pattern(regexp = ".*(?i)(GB|TB).*", message = "RAM phải chứa đơn vị GB hoặc TB")
    private String ram;

    @NotBlank(message = "Storage không được để trống")
    @Pattern(regexp = ".*(?i)(GB|TB).*", message = "Storage phải chứa đơn vị GB hoặc TB")
    private String storage;

    private String gpu;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 1000, message = "Giá phải lớn hơn 1000 VNĐ")
    private BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không được là số âm")
    private Integer stockQuantity;
}
