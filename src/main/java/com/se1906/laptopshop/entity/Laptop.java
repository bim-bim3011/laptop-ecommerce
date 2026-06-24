package com.se1906.laptopshop.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "laptops")
@Entity
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laptop_id")
    private int laptopId;

    @Column(name = "laptop_name", length = 150, nullable = false)
    private String laptopName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfigurationVersion> configurationVersions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public Laptop() {
    }

    public Laptop(int laptopId, String laptopName, String description, LocalDateTime updatedAt, LocalDateTime createdAt, List<ConfigurationVersion> configurationVersions, Category category, Brand brand, List<Feedback> feedbacks) {
        this.laptopId = laptopId;
        this.laptopName = laptopName;
        this.description = description;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.configurationVersions = configurationVersions;
        this.category = category;
        this.brand = brand;
        this.feedbacks = feedbacks;
    }

    public int getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(int laptopId) {
        this.laptopId = laptopId;
    }

    public String getLaptopName() {
        return laptopName;
    }

    public void setLaptopName(String laptopName) {
        this.laptopName = laptopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ConfigurationVersion> getConfigurationVersions() {
        return configurationVersions;
    }

    public void setConfigurationVersions(List<ConfigurationVersion> configurationVersions) {
        this.configurationVersions = configurationVersions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}

