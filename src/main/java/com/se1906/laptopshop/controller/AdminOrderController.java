package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    // =========================================================
    // 0. HIỂN THỊ TRANG DANH SÁCH ĐƠN HÀNG (KHI CLICK TỪ OVERVIEW)
    // =========================================================
    @GetMapping("")
    public String getAllOrders(Model model) {
        // Lấy toàn bộ đơn hàng từ Database thông qua Service của bạn
        List<Order> orders = orderService.getAllOrders();

        // Đẩy danh sách đơn hàng sang file HTML với tên biến là "orders"
        model.addAttribute("orders", orders);

        // Trả về file giao diện orders.html nằm trong thư mục templates/admin/
        return "admin/order-list";
    }
    // ==========================================
    // 1. ADMIN DUYỆT ĐƠN -> CHUYỂN THÀNH SHIPPING
    // ==========================================
    @PostMapping("/{id}/ship")
    public String shipOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean success = orderService.shipOrder(id);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Đã duyệt đơn hàng! Trạng thái: ĐANG VẬN CHUYỂN.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể duyệt đơn. Đơn hàng không tồn tại hoặc không ở trạng thái PENDING.");
        }

        // Chuyển hướng về trang danh sách quản lý đơn hàng của Admin (thay đổi url tùy dự án của bạn)
        return "redirect:/admin/orders";
    }
    // ==========================================
    // 2. ADMIN XÁC NHẬN HOÀN THÀNH -> CHUYỂN THÀNH DELIVERED
    // ==========================================
    @PostMapping("/{id}/complete")
    public String completeOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean success = orderService.completeOrder(id);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật đơn hàng thành công! Trạng thái: ĐÃ GIAO HÀNG.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể cập nhật. Đơn hàng phải ở trạng thái SHIPPING.");
        }

        // Chuyển hướng về trang danh sách quản lý đơn hàng của Admin
        return "redirect:/admin/orders";
    }
}
