package com.se1906.laptopshop.dto;

public class UpdateCartItemRequest {
    private int itemId;
    private String action;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
