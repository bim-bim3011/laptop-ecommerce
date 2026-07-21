package com.se1906.laptopshop.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.OrderRepository;

import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.OrderDetail;
import com.se1906.laptopshop.repository.CartItemRepository;
import com.se1906.laptopshop.repository.OrderDetailRepository;


import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Các trạng thái được coi là "chưa giao cho đơn vị vận chuyển" -> vẫn có thể hủy
    private static final List<String> CANCELLABLE_STATUSES = Arrays.asList("PENDING", "PROCESSING");

    // ==========================================
    // 1. HIỂN THỊ LỊCH SỬ ĐƠN HÀNG
    // ==========================================
    @GetMapping("/orders")
    public String viewOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        model.addAttribute("orders", orders);
        return "order-history";
    }

    // ==========================================
    // 2. HIỂN THỊ TRANG THANH TOÁN
    // ==========================================
    @PostMapping("/orders/checkout")
    public String checkoutPage(
            @RequestParam(value = "selectedItemIds", required = false) List<Integer> selectedItemIds,
            HttpSession session, 
            Model model,
            RedirectAttributes redirectAttributes) {
        
        // 1. Kiểm tra đăng nhập
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có sản phẩm nào được chọn để thanh toán!");
            return "redirect:/cart";
        }

        List<CartItem> selectedItems = cartItemRepository.findAllById(selectedItemIds);
        
        List<com.se1906.laptopshop.entity.GiftDetail> giftDetails = new java.util.ArrayList<>();
        java.util.Set<Integer> addedGiftItemIds = new java.util.HashSet<>();

        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            totalAmount += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();
            
            // Add gifts from ALL configurations of the selected laptop
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

        // 3. Trả về tên file HTML của trang thanh toán
        return "checkout";
    }

    // ==========================================
    // 3. XỬ LÝ ĐẶT HÀNG & CHUYỂN HƯỚNG
    // ==========================================
    @PostMapping("/orders/place-order")
    public String placeOrder(
            @RequestParam(value = "selectedItemIds", required = false) List<Integer> selectedItemIds,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverPhone") String receiverPhone,
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam("province") String province,
            @RequestParam("district") String district,
            @RequestParam("ward") String ward,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Kiểm tra nếu giỏ hàng trống
        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có sản phẩm nào được chọn!");
            return "redirect:/cart";
        }

        // 1. Lấy danh sách sản phẩm trong giỏ hàng
        List<CartItem> selectedItems = cartItemRepository.findAllById(selectedItemIds);

        // 2. Tạo đơn hàng tổng
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus("PENDING");
        newOrder.setOrderDate(LocalDateTime.now());

        // (Tùy chọn) Lưu thông tin người nhận nếu Entity Order có các thuộc tính này
        String fullAddress = shippingAddress + ", " + ward + ", " + district + ", " + province;


        // Tính tổng tiền
        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            totalAmount += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();
        }
        newOrder.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));

        // Lưu đơn hàng tổng để lấy ID
        Order savedOrder = orderRepository.save(newOrder);

        // 3. Tạo chi tiết đơn hàng (OrderDetail)
        for (CartItem item : selectedItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setConfigurationVersion(item.getConfigurationVersion());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getConfigurationVersion().getPrice());
            
            // Lưu thông tin địa chỉ vào OrderDetail theo yêu cầu
            detail.setReceiverName(receiverName);
            detail.setReceiverPhone(receiverPhone);
            detail.setShippingAddress(fullAddress);

            orderDetailRepository.save(detail);
        }

        // 4. Xóa sản phẩm đã thanh toán khỏi giỏ hàng
        cartItemRepository.deleteAll(selectedItems);

        // 5. Thông báo và chuyển trang
        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng của bạn đã được đặt thành công!");
        return "redirect:/orders";
    }

    // ==========================================
    // 4. HỦY ĐƠN HÀNG
    // ==========================================
    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng.");
            return "redirect:/orders";
        }

        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Đơn hàng đã được giao cho đơn vị vận chuyển hoặc đã hoàn tất, không thể hủy.");
            return "redirect:/orders";
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng #" + order.getOrderId() + " đã được hủy thành công.");

        return "redirect:/orders";
    }

    // ==========================================
    // 5. HIỂN THỊ CHI TIẾT ĐƠN HÀNG
    // ==========================================
    @GetMapping("/orderdetail/{id}")
    public String viewOrderDetail(@PathVariable("id") int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng.");
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "order-detail";
    }
}