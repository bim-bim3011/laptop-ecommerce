package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.GiftItem;
import com.se1906.laptopshop.service.GiftItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/gift-items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GiftItemAdminController {

    GiftItemService giftItemService;

    @GetMapping
    public List<GiftItem> getAllGiftItems() {
        return giftItemService.getAllGiftItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftItem> getGiftItemById(@PathVariable int id) {
        return ResponseEntity.ok(giftItemService.getGiftItemById(id));
    }

    @PostMapping
    public ResponseEntity<GiftItem> createGiftItem(@RequestBody GiftItem giftItem) {
        return ResponseEntity.ok(giftItemService.createGiftItem(giftItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiftItem> updateGiftItem(@PathVariable int id, @RequestBody GiftItem giftItem) {
        return ResponseEntity.ok(giftItemService.updateGiftItem(id, giftItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftItem(@PathVariable int id) {
        giftItemService.deleteGiftItem(id);
        return ResponseEntity.noContent().build();
    }
}
