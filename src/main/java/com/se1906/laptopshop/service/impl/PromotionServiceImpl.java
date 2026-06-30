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
