package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Promotion;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.service.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/promotions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PromotionAdminController {

    PromotionService promotionService;

    @GetMapping
    public List<Promotion> getAllPromotions() {
        return promotionService.getAllPromotions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable int id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        return ResponseEntity.ok(promotionService.createPromotion(promotion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable int id, @RequestBody Promotion promotion) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, promotion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable int id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/gifts")
    public List<GiftDetail> getGiftDetailsByPromotionId(@PathVariable int id) {
        return promotionService.getGiftDetailsByPromotionId(id);
    }

    @PostMapping("/{id}/gifts")
    public ResponseEntity<GiftDetail> createGiftDetail(@PathVariable int id, @RequestBody GiftDetail giftDetail) {
        return ResponseEntity.ok(promotionService.createGiftDetail(id, giftDetail));
    }

    @PutMapping("/gifts/{giftId}")
    public ResponseEntity<GiftDetail> updateGiftDetail(@PathVariable int giftId, @RequestBody GiftDetail giftDetail) {
        return ResponseEntity.ok(promotionService.updateGiftDetail(giftId, giftDetail));
    }

    @DeleteMapping("/gifts/{giftId}")
    public ResponseEntity<Void> deleteGiftDetail(@PathVariable int giftId) {
        promotionService.deleteGiftDetail(giftId);
        return ResponseEntity.noContent().build();
    }
}
