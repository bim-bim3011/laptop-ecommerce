package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.dto.LoginRequest;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminController {

    AuthService authService;
    com.se1906.laptopshop.repository.OrderRepository orderRepository;
    com.se1906.laptopshop.repository.UserRepository userRepository;
    com.se1906.laptopshop.repository.ConfigurationVersionRepository configurationVersionRepository;

    @GetMapping("/login")
    public String login() {
        return "admin-login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        var allOrders = orderRepository.findAll();
        double totalRevenue = allOrders.stream()
                .filter(o -> "DELIVERED".equalsIgnoreCase(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount().doubleValue() : 0.0)
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("activeUsers", userRepository.countActiveUsers());
        model.addAttribute("totalInventory", configurationVersionRepository.countTotalInventory());
        model.addAttribute("totalOrders", orderRepository.countTotalOrders());
        // 1. Lấy tất cả đơn hàng từ database

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
    public String adminPage(HttpSession session, Model model) {
        return dashboard(session, model);
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {

        if (bindingResult.hasErrors()) {
            return "admin-login";
        }

        try {
            User user = authService.login(request);
            if (user != null) {
                boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));
                
                if (isAdmin) {
                    session.setAttribute("admin", user);

                    List<GrantedAuthority> authorities = user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                            .collect(Collectors.toList());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            null,
                            authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                            SecurityContextHolder.getContext());

                    return "redirect:/admin/dashboard";
                } else {
                    model.addAttribute("error", "Access Denied: You do not have administrator privileges.");
                    return "admin-login";
                }
            } else {
                model.addAttribute("error", "Invalid email or password.");
                return "admin-login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during login.");
            return "admin-login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        SecurityContextHolder.clearContext();
        return "redirect:/home-page";
    }
}
