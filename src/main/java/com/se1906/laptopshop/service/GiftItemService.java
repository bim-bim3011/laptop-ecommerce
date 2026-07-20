package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.GiftItem;
import java.util.List;

public interface GiftItemService {
    List<GiftItem> getAllGiftItems();
    GiftItem getGiftItemById(int id);
    GiftItem createGiftItem(GiftItem giftItem);
    GiftItem updateGiftItem(int id, GiftItem giftItem);
    void deleteGiftItem(int id);
}
