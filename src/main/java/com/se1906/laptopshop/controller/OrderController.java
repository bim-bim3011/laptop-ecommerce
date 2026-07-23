package com.se1906.laptopshop.controller;

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
import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ==========================================
    // 1. HIỂN THỊ LỊCH SỬ ĐƠN HÀNG
    // ==========================================
    @GetMapping("/orders")
    public String viewOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderService.getOrdersByUser(user);
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

        List<CartItem> selectedItems = orderService.getSelectedCartItems(selectedItemIds);
        List<GiftDetail> giftDetails = orderService.getGiftDetailsFromCartItems(selectedItems);
        double totalAmount = orderService.calculateTotalAmount(selectedItems);

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
            @RequestParam(value = "shippingMethod", defaultValue = "standard") String shippingMethod,
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

        Order savedOrder = orderService.placeOrder(user, selectedItemIds, receiverName, receiverPhone, shippingAddress, province, district, ward, shippingMethod);
        
        if (savedOrder == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi đặt hàng.");
            return "redirect:/cart";
        }

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

        boolean success = orderService.cancelOrder(id, user);

        if (!success) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy đơn hàng hoặc đơn hàng không tồn tại.");
            return "redirect:/orders";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng #" + id + " đã được hủy thành công.");

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
        
        Order order = orderService.getOrderByIdAndUser(id, user);
        
        if (order == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng.");
            return "redirect:/orders";
        }
        
        model.addAttribute("order", order);
        return "order-detail";
    }
}