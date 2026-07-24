package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import java.util.List;

public interface OrderService {

    Order getOrderByIdAndUser(int orderId, User user);
    
    List<CartItem> getSelectedCartItems(List<Integer> selectedItemIds);
    
    List<GiftDetail> getGiftDetailsFromCartItems(List<CartItem> selectedItems);
    
    List<GiftDetail> getGiftDetailsFromOrder(Order order);
    
    // Luồng Khách hàng (Giữ nguyên toàn bộ logic cũ)
    List<Order> getOrderHistory(User user);
    void prepareCheckoutData(List<Integer> selectedItemIds, Model model);
    String placeOrder(List<Integer> selectedItemIds, String paymentMethod, String couponCode, User user, HttpServletRequest request, String receiverName, String receiverPhone, String shippingAddress, String province, String district, String ward, String shippingMethod);
    boolean cancelOrder(int id, User user);
    Order getOrderDetail(int id, User user);

    // Luồng Admin (Xử lý cập nhật trạng thái mới)
    boolean shipOrder(int id);
    boolean completeOrder(int id);
    List<Order> getAllOrders();

}
