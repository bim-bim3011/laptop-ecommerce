package com.se1906.laptopshop.controller;

import java.util.List;

import com.se1906.laptopshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import com.se1906.laptopshop.service.PromotionService;

import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.User;


import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PromotionService promotionService;

    // ==========================================
    // 1. HIỂN THỊ LỊCH SỬ ĐƠN HÀNG
    // ==========================================
    @GetMapping("/orders")
    public String viewOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("orders", orderService.getOrderHistory(user));
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

        orderService.prepareCheckoutData(selectedItemIds, model);
        return "checkout";
    }

    // ==========================================
    // 2.5 ÁP DỤNG MÃ GIẢM GIÁ (THYMELEAF)
    // ==========================================
    @PostMapping("/orders/checkout-coupon")
    public String checkoutCoupon(
            @RequestParam(value = "selectedItemIds", required = false) List<Integer> selectedItemIds,
            @RequestParam(value = "receiverName", required = false) String receiverName,
            @RequestParam(value = "receiverPhone", required = false) String receiverPhone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "ward", required = false) String ward,
            @RequestParam(value = "shippingAddress", required = false) String shippingAddress,
            @RequestParam(value = "shippingMethod", required = false) String shippingMethod,
            @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
            @RequestParam(value = "inputCouponCode", required = false) String couponCode,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có sản phẩm nào được chọn!");
            return "redirect:/cart";
        }

        // Lấy lại dữ liệu đơn hàng
        orderService.prepareCheckoutData(selectedItemIds, model);

        double totalAmount = (Double) model.getAttribute("totalAmount");

        if (couponCode != null && !couponCode.trim().isEmpty()) {
            Map<String, Object> couponResult = promotionService.validateCoupon(couponCode.trim(), totalAmount);
            boolean isValid = (Boolean) couponResult.get("valid");
            model.addAttribute("couponMessage", couponResult.get("message"));
            model.addAttribute("isValidCoupon", isValid);
            if (isValid) {
                double discountAmount = (Double) couponResult.get("discountAmount");
                model.addAttribute("discountAmount", discountAmount);
                model.addAttribute("newTotal", totalAmount - discountAmount);
                model.addAttribute("appliedCouponCode", couponCode.trim());
            } else {
                model.addAttribute("discountAmount", 0.0);
                model.addAttribute("newTotal", totalAmount);
            }
        }

        // Giữ lại dữ liệu đã nhập
        model.addAttribute("receiverName", receiverName);
        model.addAttribute("receiverPhone", receiverPhone);
        model.addAttribute("email", email);
        model.addAttribute("selectedProvince", province);
        model.addAttribute("selectedDistrict", district);
        model.addAttribute("selectedWard", ward);
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("shippingMethod", shippingMethod);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("inputCouponCode", couponCode);

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
            @RequestParam(value = "paymentMethod", required = false, defaultValue = "cod") String paymentMethod,
            @RequestParam(value = "couponCode", required = false) String couponCode,
            HttpSession session,
            jakarta.servlet.http.HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có sản phẩm nào được chọn!");
            return "redirect:/cart";
        }

        // ĐÃ SỬA: Bổ sung biến người nhận vào đúng thứ tự tham số truyền đi
        String result = orderService.placeOrder(selectedItemIds, paymentMethod, couponCode, user, request);

        if (result.startsWith("http") || result.contains("vnpayment.vn")) {
            return "redirect:" + result;
        }

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
            return "redirect:/auth/login";
        }

        boolean currentCancelStatus = orderService.cancelOrder(id, user);
        if (!currentCancelStatus) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Đơn hàng đã được giao cho đơn vị vận chuyển hoặc đã hoàn tất, không thể hủy.");
            return "redirect:/orders";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng đã được hủy thành công.");
        return "redirect:/orders";
    }

    // ==========================================
    // 5. HIỂN THỊ CHI TIẾT ĐƠN HÀNG
    // ==========================================
    @GetMapping("/orders/{id}")
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