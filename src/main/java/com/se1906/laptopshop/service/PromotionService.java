package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.Promotion;
import com.se1906.laptopshop.entity.GiftDetail;
import java.util.List;

public interface PromotionService {
    List<Promotion> getAllPromotions();
    Promotion getPromotionById(int id);
    Promotion createPromotion(Promotion promotion);
    Promotion updatePromotion(int id, Promotion promotion);
    void deletePromotion(int id);

    List<GiftDetail> getGiftDetailsByPromotionId(int promotionId);
    GiftDetail createGiftDetail(int promotionId, GiftDetail giftDetail);
    GiftDetail updateGiftDetail(int id, GiftDetail giftDetail);
    void deleteGiftDetail(int id);
}
