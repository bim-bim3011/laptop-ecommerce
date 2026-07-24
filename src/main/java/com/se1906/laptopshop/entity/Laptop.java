package com.se1906.laptopshop.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "laptops")
@Entity
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laptop_id")
    private Integer laptopId;

    @Column(name = "laptop_name", length = 150, nullable = false)
    private String laptopName;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "image_url", columnDefinition = "NVARCHAR(MAX)")
    private String imageUrl;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfigurationVersion> configurationVersions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @JsonIgnore
    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GiftDetail> giftDetails = new ArrayList<>();

    public Laptop() {
    }

    public Laptop(Integer laptopId, String laptopName, String description, String imageUrl, LocalDateTime updatedAt, LocalDateTime createdAt, List<ConfigurationVersion> configurationVersions, Category category, Brand brand, List<Feedback> feedbacks) {
        this.laptopId = laptopId;
        this.laptopName = laptopName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.configurationVersions = configurationVersions;
        this.category = category;
        this.brand = brand;
        this.feedbacks = feedbacks;
    }


    public Integer getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(Integer laptopId) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
    public List<GiftDetail> getGiftDetails() {
        return giftDetails;
    }

    public void setGiftDetails(List<GiftDetail> giftDetails) {
        this.giftDetails = giftDetails;
    }
}

