package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.entity.GiftItem;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.repository.ConfigurationVersionRepository;
import com.se1906.laptopshop.repository.GiftDetailRepository;
import com.se1906.laptopshop.repository.GiftItemRepository;
import com.se1906.laptopshop.service.GiftDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GiftDetailServiceImpl implements GiftDetailService {

    GiftDetailRepository giftDetailRepository;
    ConfigurationVersionRepository configurationVersionRepository;
    GiftItemRepository giftItemRepository;

    @Override
    public List<GiftDetail> getGiftsByConfiguration(int configId) {
        return giftDetailRepository.findByConfigurationVersion_ConfigurationId(configId);
    }

    @Override
    public GiftDetail addGiftToConfiguration(int configId, int giftItemId, int quantity) {
        ConfigurationVersion config = configurationVersionRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
        GiftItem giftItem = giftItemRepository.findById(giftItemId)
                .orElseThrow(() -> new RuntimeException("Gift Item not found"));

        GiftDetail giftDetail = new GiftDetail();
        giftDetail.setConfigurationVersion(config);
        giftDetail.setGiftItem(giftItem);
        giftDetail.setQuantity(quantity);

        return giftDetailRepository.save(giftDetail);
    }

    @Override
    public void removeGiftFromConfiguration(int giftDetailId) {
        if (giftDetailRepository.existsById(giftDetailId)) {
            giftDetailRepository.deleteById(giftDetailId);
        } else {
            throw new RuntimeException("Gift Detail not found");
        }
    }

    @Override
    public void saveGiftsForConfig(Laptop laptop, ConfigurationVersion config, List<Integer> selectedGiftIds) {
        if (selectedGiftIds == null || selectedGiftIds.isEmpty()) {
            return;
        }

        for (Integer giftItemId : selectedGiftIds) {
            GiftItem giftItem = giftItemRepository.findById(giftItemId).orElse(null);
            if (giftItem != null) {
                GiftDetail giftDetail = new GiftDetail();
                giftDetail.setLaptop(laptop); // Gán thông tin Laptop
                giftDetail.setConfigurationVersion(config); // Gán thông tin Config cụ thể
                giftDetail.setGiftItem(giftItem); // Món quà tương ứng
                giftDetail.setQuantity(1); // Mặc định số lượng tặng kèm khi mua là 1

                giftDetailRepository.save(giftDetail);
            }
        }
    }
}
