package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.entity.Laptop;

import java.util.List;

public interface GiftDetailService {
    List<GiftDetail> getGiftsByConfiguration(int configId);
    GiftDetail addGiftToConfiguration(int configId, int giftItemId, int quantity);
    void removeGiftFromConfiguration(int giftDetailId);
    void saveGiftsForConfig(Laptop laptop, ConfigurationVersion config, List<Integer> selectedGiftIds);
}
