package com.se1906.laptopshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import com.se1906.laptopshop.entity.Order;

@Controller
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminController {

    com.se1906.laptopshop.repository.OrderRepository orderRepository;
    com.se1906.laptopshop.repository.UserRepository userRepository;
    com.se1906.laptopshop.repository.ConfigurationVersionRepository configurationVersionRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalRevenue", orderRepository.calculateTotalRevenue());
        model.addAttribute("activeUsers", userRepository.countActiveUsers());
        model.addAttribute("totalInventory", configurationVersionRepository.countTotalInventory());
        model.addAttribute("totalOrders", orderRepository.countTotalOrders());
        // 1. Lấy tất cả đơn hàng từ database
        List<Order> allOrders = orderRepository.findAll();

        // 2. Tổng order đang chờ xác nhận (PENDING)
        long pendingCount = allOrders.stream()
                .filter(o -> "PENDING".equalsIgnoreCase(o.getStatus()))
                .count();

        // 3. Tổng order đang vận chuyển (SHIPPING)
        long shippingCount = allOrders.stream()
                .filter(o -> "SHIPPING".equalsIgnoreCase(o.getStatus()))
                .count();

        // 4. Tổng order đã giao (DELIVERED)
        long deliveredCount = allOrders.stream()
                .filter(o -> "DELIVERED".equalsIgnoreCase(o.getStatus()))
                .count();

        // 5. Tổng tiền đã refund (Tính tổng amount của các đơn hàng đã bị hủy - CANCELLED)
        double totalRefund = allOrders.stream()
                .filter(o -> "CANCELLED".equalsIgnoreCase(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount().doubleValue() : 0.0)
                .sum();

        // Đẩy 4 biến mới này sang giao diện HTML công thức Thymeleaf
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("shippingCount", shippingCount);
        model.addAttribute("deliveredCount", deliveredCount);
        model.addAttribute("totalRefund", totalRefund);
        return "admin-dashboard";
    }

    @GetMapping
    public String adminPage(Model model) {
        return dashboard(model);
    }
}
