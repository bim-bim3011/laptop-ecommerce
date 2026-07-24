package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Promotion;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.repository.PromotionRepository;
import com.se1906.laptopshop.repository.GiftDetailRepository;
import com.se1906.laptopshop.service.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    PromotionRepository promotionRepository;
    GiftDetailRepository giftDetailRepository;

    @Override
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    public Promotion getPromotionById(int id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));
    }

    @Override
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Override
    public Promotion updatePromotion(int id, Promotion promotion) {
        Promotion existing = getPromotionById(id);
        existing.setCouponCode(promotion.getCouponCode());
        existing.setTitle(promotion.getTitle());
        existing.setDiscountType(promotion.getDiscountType());
        existing.setDiscountValue(promotion.getDiscountValue());
        existing.setMinOrderValue(promotion.getMinOrderValue());
        existing.setMaxDiscountAmount(promotion.getMaxDiscountAmount());
        existing.setStartDate(promotion.getStartDate());
        existing.setEndDate(promotion.getEndDate());
        return promotionRepository.save(existing);
    }

    @Override
    public void deletePromotion(int id) {
        Promotion existing = getPromotionById(id);
        promotionRepository.delete(existing);
    }
    
    @Override
    public java.util.Map<String, Object> validateCoupon(String code, double orderTotal) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        if (code == null || code.trim().isEmpty()) {
            result.put("valid", false);
            result.put("message", "Vui lòng nhập mã giảm giá!");
            return result;
        }
        
        String trimmedCode = code.trim();
        Promotion promotion = promotionRepository.findByCouponCode(trimmedCode).orElse(null);
        
        if (promotion != null && promotion.getCouponCode() != null && !promotion.getCouponCode().equals(trimmedCode)) {
            promotion = null;
        }
        
        if (promotion == null) {
            result.put("valid", false);
            result.put("message", "Mã giảm giá không tồn tại!");
            return result;
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (now.isBefore(promotion.getStartDate()) || now.isAfter(promotion.getEndDate())) {
            result.put("valid", false);
            result.put("message", "Mã giảm giá đã hết hạn hoặc chưa bắt đầu!");
            return result;
        }
        
        if (promotion.getMinOrderValue() != null && java.math.BigDecimal.valueOf(orderTotal).compareTo(promotion.getMinOrderValue()) < 0) {
            result.put("valid", false);
            result.put("message", "Đơn hàng chưa đạt giá trị tối thiểu để áp dụng mã này!");
            return result;
        }
        
        double discountAmount = 0;
        if ("PERCENTAGE".equalsIgnoreCase(promotion.getDiscountType())) {
            discountAmount = orderTotal * promotion.getDiscountValue().doubleValue() / 100;
            if (promotion.getMaxDiscountAmount() != null && discountAmount > promotion.getMaxDiscountAmount().doubleValue()) {
                discountAmount = promotion.getMaxDiscountAmount().doubleValue();
            }
        } else if ("FIXED_AMOUNT".equalsIgnoreCase(promotion.getDiscountType())) {
            discountAmount = promotion.getDiscountValue().doubleValue();
        }
        
        if (discountAmount > orderTotal) {
            discountAmount = orderTotal;
        }
        
        result.put("valid", true);
        result.put("discountAmount", discountAmount);
        result.put("message", "Áp dụng mã giảm giá thành công!");
        result.put("promotionId", promotion.getPromotionId());
        
        return result;
    }

    @Override
    public List<GiftDetail> getGiftDetailsByPromotionId(int promotionId) {
        Promotion promotion = getPromotionById(promotionId);
        return promotion.getGiftDetails();
    }

    @Override
    public GiftDetail createGiftDetail(int promotionId, GiftDetail giftDetail) {
        Promotion promotion = getPromotionById(promotionId);
        giftDetail.setPromotion(promotion);
        return giftDetailRepository.save(giftDetail);
    }

    @Override
    public GiftDetail updateGiftDetail(int id, GiftDetail giftDetail) {
        GiftDetail existing = giftDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GiftDetail not found"));
        existing.setQuantity(giftDetail.getQuantity());
        if (giftDetail.getConfigurationVersion() != null) {
            existing.setConfigurationVersion(giftDetail.getConfigurationVersion());
        }
        return giftDetailRepository.save(existing);
    }

    @Override
    public void deleteGiftDetail(int id) {
        GiftDetail existing = giftDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GiftDetail not found"));
        giftDetailRepository.delete(existing);
    }
}
