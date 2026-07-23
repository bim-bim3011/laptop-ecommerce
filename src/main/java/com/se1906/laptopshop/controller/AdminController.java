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

        model.addAttribute("totalRevenue", orderRepository.calculateTotalRevenue());
        model.addAttribute("activeUsers", userRepository.countActiveUsers());
        model.addAttribute("totalInventory", configurationVersionRepository.countTotalInventory());
        model.addAttribute("totalOrders", orderRepository.countTotalOrders());


        java.math.BigDecimal totalRevenue = orderRepository.sumTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = java.math.BigDecimal.ZERO;
        }

        long pendingOrders = orderRepository.countByStatus("PENDING");
        long deliveringOrders = orderRepository.countByStatus("SHIPPING");
        if (deliveringOrders == 0) deliveringOrders = orderRepository.countByStatus("PROCESSING");
        long deliveredOrders = orderRepository.countByStatus("DELIVERED");
        if (deliveredOrders == 0) deliveredOrders = orderRepository.countByStatus("COMPLETED");

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("deliveringOrders", deliveringOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);

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
