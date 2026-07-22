package com.se1906.laptopshop.dto;

import lombok.Data;

@Data
public class AddGiftToConfigRequest {
    private int giftItemId;
    private int quantity;
}
