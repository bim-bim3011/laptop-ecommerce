package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.OrderDetail;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.CartItemRepository;
import com.se1906.laptopshop.repository.OrderDetailRepository;
import com.se1906.laptopshop.repository.OrderRepository;
import com.se1906.laptopshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private static final List<String> CANCELLABLE_STATUSES = Arrays.asList("PENDING", "PROCESSING");

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Override
    public Order getOrderByIdAndUser(int orderId, User user) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getUser() != null && order.getUser().getUserId().equals(user.getUserId())) {
            return order;
        }
        return null;
    }

    @Override
    public List<CartItem> getSelectedCartItems(List<Integer> selectedItemIds) {
        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            return new ArrayList<>();
        }
        return cartItemRepository.findAllById(selectedItemIds);
    }

    @Override
    public List<GiftDetail> getGiftDetailsFromCartItems(List<CartItem> selectedItems) {
        List<GiftDetail> giftDetails = new ArrayList<>();
        Set<Integer> addedGiftItemIds = new HashSet<>();

        for (CartItem item : selectedItems) {
            if (item.getConfigurationVersion() != null && item.getConfigurationVersion().getLaptop() != null) {
                Laptop laptop = item.getConfigurationVersion().getLaptop();
                if (laptop.getConfigurationVersions() != null) {
                    for (ConfigurationVersion cv : laptop.getConfigurationVersions()) {
                        if (cv.getGiftDetails() != null) {
                            for (GiftDetail gd : cv.getGiftDetails()) {
                                if (gd.getGiftItem() != null) {
                                    Integer itemId = gd.getGiftItem().getGiftItemId();
                                    if (!addedGiftItemIds.contains(itemId)) {
                                        giftDetails.add(gd);
                                        addedGiftItemIds.add(itemId);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return giftDetails;
    }

    @Override
    public double calculateTotalAmount(List<CartItem> selectedItems) {
        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            if (item.getConfigurationVersion() != null && item.getConfigurationVersion().getPrice() != null) {
                totalAmount += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();
            }
        }
        return totalAmount;
    }

    @Override
    @Transactional
    public Order placeOrder(User user, List<Integer> selectedItemIds, String receiverName, String receiverPhone, String shippingAddress, String province, String district, String ward, String shippingMethod) {
        List<CartItem> selectedItems = getSelectedCartItems(selectedItemIds);
        if (selectedItems.isEmpty()) {
            return null;
        }

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus("PENDING");
        newOrder.setOrderDate(LocalDateTime.now());

        double totalAmount = calculateTotalAmount(selectedItems);
        if ("express".equals(shippingMethod)) {
            totalAmount += 150000;
        }
        newOrder.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));

        Order savedOrder = orderRepository.save(newOrder);

        String fullAddress = shippingAddress + ", " + ward + ", " + district + ", " + province;

        for (CartItem item : selectedItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setConfigurationVersion(item.getConfigurationVersion());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getConfigurationVersion().getPrice());
            
            detail.setReceiverName(receiverName);
            detail.setReceiverPhone(receiverPhone);
            detail.setShippingAddress(fullAddress);

            orderDetailRepository.save(detail);
        }

        cartItemRepository.deleteAll(selectedItems);
        return savedOrder;
    }

    @Override
    @Transactional
    public boolean cancelOrder(int orderId, User user) {
        Order order = getOrderByIdAndUser(orderId, user);
        if (order == null) {
            return false;
        }

        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            return false;
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        return true;
    }
}
