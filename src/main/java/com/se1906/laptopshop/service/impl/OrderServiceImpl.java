package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.CartItem;
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


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {



   OrderRepository orderRepository;


    OrderDetailRepository orderDetailRepository;


     CartItemRepository cartItemRepository;
     static final List<String> CANCELLABLE_STATUSES = Arrays.asList("PENDING", "PENDING_PAYMENT");


    PaymentService paymentService;
    @Override
    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Override
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
    public String placeOrder(List<Integer> selectedItemIds, String paymentMethod, User user, HttpServletRequest request) {
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
        newOrder.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));

        // Lưu đơn hàng tổng để lấy ID
        Order savedOrder = orderRepository.save(newOrder);

        // Tạo chi tiết đơn hàng (OrderDetail)
        for (CartItem item : selectedItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setConfigurationVersion(item.getConfigurationVersion());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getConfigurationVersion().getPrice());
            orderDetailRepository.save(detail);
        }

        // Xóa sản phẩm đã thanh toán khỏi giỏ hàng
        cartItemRepository.deleteAll(selectedItems);

        // Trả kết quả chuyển hướng hoặc báo thành công
        if ("vnpay".equalsIgnoreCase(paymentMethod)) {
            return paymentService.createVnPayPaymentUrl(request, (long) totalAmount, "Thanh toan don hang " + savedOrder.getOrderId(), String.valueOf(savedOrder.getOrderId()));
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

        // GIỮ NGUYÊN GỐC + HOÀN TIỀN: Nếu đã thanh toán qua VNPay thành công thì hoàn tiền
        if ("VNPAY".equalsIgnoreCase(order.getPaymentMethod()) && "SUCCESS".equalsIgnoreCase(order.getPaymentStatus())) {
            // Kích hoạt dòng này sau khi bạn tích hợp phương thức refund vào paymentService:
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
