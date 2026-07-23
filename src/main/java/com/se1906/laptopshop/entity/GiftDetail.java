package com.se1906.laptopshop.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Table(name = "gift_details")
@Entity
@Getter
@Setter
public class GiftDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Integer giftId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "configuration_version_id")
    private ConfigurationVersion configurationVersion;

    @ManyToOne
    @JoinColumn(name = "gift_item_id")
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private GiftItem giftItem;
    @ManyToOne
    @JoinColumn(name = "laptop_id") // Nối chính xác sang khóa chính laptop_id của bảng laptops
    private Laptop laptop;
}
