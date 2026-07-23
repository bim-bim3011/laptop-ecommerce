package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Cart;
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
import com.se1906.laptopshop.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;

import com.se1906.laptopshop.entity.Promotion;
import com.se1906.laptopshop.repository.PromotionRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    OrderDetailRepository orderDetailRepository;

    CartItemRepository cartItemRepository;
    PromotionRepository promotionRepository;
    static final List<String> CANCELLABLE_STATUSES = Arrays.asList("PENDING", "PENDING_PAYMENT", "PROCESSING");

    PaymentService paymentService;

    @Override
    public List<Order> getOrderHistory(User user) {
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
    public void prepareCheckoutData(List<Integer> selectedItemIds, Model model) {
        List<CartItem> selectedItems = cartItemRepository.findAllById(selectedItemIds);

        List<com.se1906.laptopshop.entity.GiftDetail> giftDetails = new ArrayList<>();
        Set<Integer> addedGiftItemIds = new HashSet<>();

        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            totalAmount += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();

            // GIỮ NGUYÊN GỐC: Add gifts from ALL configurations of the selected laptop
            if (item.getConfigurationVersion() != null && item.getConfigurationVersion().getLaptop() != null) {
                com.se1906.laptopshop.entity.Laptop laptop = item.getConfigurationVersion().getLaptop();
                if (laptop.getConfigurationVersions() != null) {
                    for (com.se1906.laptopshop.entity.ConfigurationVersion cv : laptop.getConfigurationVersions()) {
                        if (cv.getGiftDetails() != null) {
                            for (com.se1906.laptopshop.entity.GiftDetail gd : cv.getGiftDetails()) {
                                if (gd.getGiftItem() != null) {
                                    gd.getGiftItem().getItemName();
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

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("selectedItemIds", selectedItemIds);
        model.addAttribute("giftDetails", giftDetails);
    }

    @Override
    @Transactional
    public String placeOrder(List<Integer> selectedItemIds, String paymentMethod, String couponCode, User user,
            HttpServletRequest request, String receiverName, String receiverPhone, String shippingAddress, String province, String district, String ward, String shippingMethod) {
        List<CartItem> selectedItems = cartItemRepository.findAllById(selectedItemIds);

        // Tạo đơn hàng tổng
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderDate(LocalDateTime.now());

        if ("vnpay".equalsIgnoreCase(paymentMethod)) {
            newOrder.setPaymentMethod("VNPAY");
            newOrder.setPaymentStatus("PENDING");
            newOrder.setStatus("PENDING_PAYMENT");
        } else {
            newOrder.setPaymentMethod("COD");
            newOrder.setPaymentStatus("PENDING");
            newOrder.setStatus("PENDING"); // Khách đặt xong ở trạng thái CHỜ XÁC NHẬN
        }

        // Tính tổng tiền
        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            totalAmount += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();
        }

        // Tính toán discount
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            Promotion promotion = promotionRepository.findByCouponCode(couponCode).orElse(null);
            if (promotion != null) {
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                if (!now.isBefore(promotion.getStartDate()) && !now.isAfter(promotion.getEndDate())) {
                    if (promotion.getMinOrderValue() == null
                            || java.math.BigDecimal.valueOf(totalAmount).compareTo(promotion.getMinOrderValue()) >= 0) {
                        double discountAmount = 0;
                        if ("PERCENTAGE".equalsIgnoreCase(promotion.getDiscountType())) {
                            discountAmount = totalAmount * promotion.getDiscountValue().doubleValue() / 100;
                            if (promotion.getMaxDiscountAmount() != null
                                    && discountAmount > promotion.getMaxDiscountAmount().doubleValue()) {
                                discountAmount = promotion.getMaxDiscountAmount().doubleValue();
                            }
                        } else if ("FIXED_AMOUNT".equalsIgnoreCase(promotion.getDiscountType())) {
                            discountAmount = promotion.getDiscountValue().doubleValue();
                        }

                        if (discountAmount > totalAmount)
                            discountAmount = totalAmount;

                        totalAmount -= discountAmount;
                        newOrder.setDiscountAmount(java.math.BigDecimal.valueOf(discountAmount));
                        newOrder.setPromotion(promotion);
                    }
                }
            }
        }

        if ("express".equalsIgnoreCase(shippingMethod)) {
            totalAmount += 150000;
        }

        newOrder.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));

        // Lưu đơn hàng tổng để lấy ID
        Order savedOrder = orderRepository.save(newOrder);

        // Nối chuỗi địa chỉ
        String fullAddress = (shippingAddress != null ? shippingAddress : "");
        if (ward != null && !ward.trim().isEmpty()) fullAddress += ", " + ward;
        if (district != null && !district.trim().isEmpty()) fullAddress += ", " + district;
        if (province != null && !province.trim().isEmpty()) fullAddress += ", " + province;

        // Tạo chi tiết đơn hàng (OrderDetail)
        for (CartItem item : selectedItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setConfigurationVersion(item.getConfigurationVersion());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getConfigurationVersion().getPrice());
            
            // Ghi nhận thông tin giao hàng vào chi tiết đơn
            detail.setReceiverName(receiverName);
            detail.setReceiverPhone(receiverPhone);
            detail.setShippingAddress(fullAddress);
            
            orderDetailRepository.save(detail);
        }

        // Xóa sản phẩm đã thanh toán khỏi giỏ hàng
        if (!selectedItems.isEmpty()) {
            Cart cart = selectedItems.get(0).getCart();
            if (cart != null && cart.getCartItems() != null) {
                cart.getCartItems().removeAll(selectedItems);
            }
        }
        cartItemRepository.deleteAllInBatch(selectedItems);

        // Trả kết quả chuyển hướng hoặc báo thành công
        if ("vnpay".equalsIgnoreCase(paymentMethod)) {
            return paymentService.createVnPayPaymentUrl(request, (long) totalAmount,
                    "Thanh toan don hang " + savedOrder.getOrderId(), String.valueOf(savedOrder.getOrderId()));
        }

        return "SUCCESS";
    }

    @Override
    @Transactional
    public boolean cancelOrder(int id, User user) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            return false;
        }

        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            return false;
        }

        // GIỮ NGUYÊN GỐC + HOÀN TIỀN: Nếu đã thanh toán qua VNPay thành công thì hoàn
        // tiền
        if ("VNPAY".equalsIgnoreCase(order.getPaymentMethod())
                && "SUCCESS".equalsIgnoreCase(order.getPaymentStatus())) {
            // Kích hoạt dòng này sau khi bạn tích hợp phương thức refund vào
            // paymentService:
            // paymentService.refundVnPay(order, user);
            order.setPaymentStatus("REFUNDED");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        return true;
    }

    @Override
    public Order getOrderDetail(int id, User user) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            return null;
        }
        return order;
    }

    @Override
    @Transactional
    public boolean shipOrder(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || !"PENDING".equalsIgnoreCase(order.getStatus())) {
            return false;
        }
        order.setStatus("SHIPPING"); // Duyệt đơn chuyển thành Đang vận chuyển
        orderRepository.save(order);
        return true;
    }

    @Override
    @Transactional
    public boolean completeOrder(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || !"SHIPPING".equalsIgnoreCase(order.getStatus())) {
            return false;
        }
        order.setStatus("DELIVERED"); // Đã giao hàng
        if ("COD".equalsIgnoreCase(order.getPaymentMethod())) {
            order.setPaymentStatus("SUCCESS");
        }
        orderRepository.save(order);
        return true;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}
