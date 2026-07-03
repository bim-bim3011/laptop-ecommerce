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

// LƯU Ý 1: BẠN HÃY IMPORT THÊM CÁC CLASS NÀY (Nhấn Alt+Enter trong IntelliJ để import tự động nếu đường dẫn package của bạn khác)
// import com.se1906.laptopshop.entity.CartItem;
// import com.se1906.laptopshop.entity.OrderDetail;
// import com.se1906.laptopshop.repository.CartItemRepository;
// import com.se1906.laptopshop.repository.OrderDetailRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // LƯU Ý 2: BỎ COMMENT 2 REPOSITORY NÀY ĐỂ KẾT NỐI VỚI GIỎ HÀNG VÀ CHI TIẾT ĐƠN HÀNG
    // @Autowired
    // private OrderDetailRepository orderDetailRepository;
    //
    // @Autowired
    // private CartItemRepository cartItemRepository;

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
    public String checkoutPage(HttpSession session, Model model) {
        // 1. Kiểm tra đăng nhập
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        // 2. [Tùy chọn] Tại đây bạn lấy dữ liệu các sản phẩm được chọn từ form
        // dựa trên parameter "selectedItemIds" để gửi sang trang thanh toán.

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
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        // BỎ COMMENT TOÀN BỘ ĐOẠN DƯỚI ĐÂY KHI ĐÃ IMPORT ĐỦ CART ITEM VÀ ORDER DETAIL
        /*
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
        // newOrder.setReceiverName(receiverName);
        // newOrder.setReceiverPhone(receiverPhone);
        // newOrder.setShippingAddress(shippingAddress);

        // Tính tổng tiền
        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            totalAmount += item.getConfigurationVersion().getPrice() * item.getQuantity();
        }
        newOrder.setTotalAmount(totalAmount);

        // Lưu đơn hàng tổng để lấy ID
        Order savedOrder = orderRepository.save(newOrder);

        // 3. Tạo chi tiết đơn hàng (OrderDetail)
        for (CartItem item : selectedItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setConfigurationVersion(item.getConfigurationVersion());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getConfigurationVersion().getPrice());

            orderDetailRepository.save(detail);
        }

        // 4. Xóa sản phẩm đã thanh toán khỏi giỏ hàng
        cartItemRepository.deleteAll(selectedItems);
        */

        // LƯU Ý 3: Nếu bạn chưa fix xong các import, hãy giữ nguyên 2 dòng dưới đây để code cũ vẫn chạy tạm được (nhưng sẽ bị null tiền).
        // Nếu đã bỏ comment đoạn code trên, hãy XÓA 2 dòng tạo order cũ này đi:
        Order tempOrder = new Order();
        tempOrder.setUser(user);
        tempOrder.setStatus("PENDING");
        tempOrder.setOrderDate(LocalDateTime.now());
        orderRepository.save(tempOrder);

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
}