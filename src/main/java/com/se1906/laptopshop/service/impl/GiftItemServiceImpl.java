package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.GiftItem;
import com.se1906.laptopshop.repository.GiftItemRepository;
import com.se1906.laptopshop.service.GiftItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GiftItemServiceImpl implements GiftItemService {

    GiftItemRepository giftItemRepository;

    @Override
    public List<GiftItem> getAllGiftItems() {
        return giftItemRepository.findAll();
    }

    @Override
    public GiftItem getGiftItemById(int id) {
        return giftItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Gift Item not found"));
    }

    @Override
    public GiftItem createGiftItem(GiftItem giftItem) {
        return giftItemRepository.save(giftItem);
    }

    @Override
    public GiftItem updateGiftItem(int id, GiftItem giftItem) {
        GiftItem existingGiftItem = getGiftItemById(id);
        existingGiftItem.setItemName(giftItem.getItemName());
        existingGiftItem.setDescription(giftItem.getDescription());
        existingGiftItem.setPrice(giftItem.getPrice());
        existingGiftItem.setImageUrl(giftItem.getImageUrl());
        return giftItemRepository.save(existingGiftItem);
    }

    @Override
    public void deleteGiftItem(int id) {
        GiftItem existingGiftItem = getGiftItemById(id);
        giftItemRepository.delete(existingGiftItem);
    }
}
