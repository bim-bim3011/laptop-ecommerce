package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.User;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByUser(User user);
    
    Order getOrderByIdAndUser(int orderId, User user);
    
    List<CartItem> getSelectedCartItems(List<Integer> selectedItemIds);
    
    List<GiftDetail> getGiftDetailsFromCartItems(List<CartItem> selectedItems);
    
    double calculateTotalAmount(List<CartItem> selectedItems);
    
    Order placeOrder(User user, List<Integer> selectedItemIds, String receiverName, String receiverPhone, String shippingAddress, String province, String district, String ward, String shippingMethod);
    
    boolean cancelOrder(int orderId, User user);
}
