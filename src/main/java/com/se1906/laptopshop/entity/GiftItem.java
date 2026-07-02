package com.se1906.laptopshop.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "gift_items")
@Entity
public class GiftItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_item_id")
    private Integer giftItemId;

    @Column(name = "item_name", length = 100, nullable = false)
    private String itemName;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "giftItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GiftDetail> giftDetails = new ArrayList<>();

    public GiftItem() {
    }

    public GiftItem(Integer giftItemId, String itemName, String description, BigDecimal price, String imageUrl, LocalDateTime updatedAt, LocalDateTime createdAt, List<GiftDetail> giftDetails) {
        this.giftItemId = giftItemId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.giftDetails = giftDetails;
    }

    public Integer getGiftItemId() {
        return giftItemId;
    }

    public void setGiftItemId(Integer giftItemId) {
        this.giftItemId = giftItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public List<GiftDetail> getGiftDetails() {
        return giftDetails;
    }

    public void setGiftDetails(List<GiftDetail> giftDetails) {
        this.giftDetails = giftDetails;
    }
}
