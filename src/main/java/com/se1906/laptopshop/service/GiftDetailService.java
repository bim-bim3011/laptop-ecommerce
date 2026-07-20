package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.GiftDetail;
import java.util.List;

public interface GiftDetailService {
    List<GiftDetail> getGiftsByConfiguration(int configId);
    GiftDetail addGiftToConfiguration(int configId, int giftItemId, int quantity);
    void removeGiftFromConfiguration(int giftDetailId);
}
