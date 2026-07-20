package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.dto.AddGiftToConfigRequest;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.service.GiftDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GiftDetailAdminController {

    GiftDetailService giftDetailService;

    @GetMapping("/configurations/{configId}/gifts")
    public List<GiftDetail> getGiftsByConfiguration(@PathVariable int configId) {
        return giftDetailService.getGiftsByConfiguration(configId);
    }

    @PostMapping("/configurations/{configId}/gifts")
    public ResponseEntity<GiftDetail> addGiftToConfiguration(
            @PathVariable int configId,
            @RequestBody AddGiftToConfigRequest request) {
        GiftDetail savedGiftDetail = giftDetailService.addGiftToConfiguration(configId, request.getGiftItemId(), request.getQuantity());
        return ResponseEntity.ok(savedGiftDetail);
    }

    @DeleteMapping("/gifts-details/{giftDetailId}")
    public ResponseEntity<Void> removeGiftFromConfiguration(@PathVariable int giftDetailId) {
        giftDetailService.removeGiftFromConfiguration(giftDetailId);
        return ResponseEntity.noContent().build();
    }
}
