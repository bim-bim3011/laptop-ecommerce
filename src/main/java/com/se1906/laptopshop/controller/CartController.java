package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.dto.AddToCartRequest;
import com.se1906.laptopshop.dto.UpdateCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.se1906.laptopshop.entity.Cart;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;



    /**
     * Hiển thị trang giỏ hàng của người dùng đang đăng nhập.
     * Tính toán tổng tiền tạm tính của các sản phẩm có trong giỏ.
     */
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Cart cart = cartService.getCartByUser(user);

        model.addAttribute("cart", cart);

        double subtotal = 0;
        for (com.se1906.laptopshop.entity.CartItem item : cart.getCartItems()) {
            subtotal += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();
        }

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", subtotal);

        return "cart";
    }

    /**
     * Thêm sản phẩm (với cấu hình tương ứng) vào giỏ hàng của người dùng.
     */
    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute AddToCartRequest request,
            HttpSession session,
            @RequestHeader(value = "Referer", required = false) String referer,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/auth/login";
            }
            cartService.addToCart(user, request.getConfigurationId(), request.getQuantity());
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi không xác định!");
        }
        return "redirect:" + (referer != null ? referer : "/cart");
    }

    /**
     * Xóa một sản phẩm cụ thể khỏi giỏ hàng dựa trên ID của sản phẩm trong giỏ (itemId).
     */
    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam("itemId") int itemId) {
        try {
            cartService.removeCartItem(itemId);
        } catch (Exception e) {
            // Ignore error
        }
        return "redirect:/cart";
    }

    /**
     * Cập nhật số lượng (tăng hoặc giảm) của một sản phẩm trong giỏ hàng.
     * Kiểm tra số lượng tồn kho trước khi cập nhật.
     */
    @PostMapping("/cart/update")
    public String updateCartItem(@ModelAttribute UpdateCartItemRequest request,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            cartService.updateCartItemQuantity(request.getItemId(), request.getAction());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể cập nhật số lượng!");
        }
        return "redirect:/cart";
    }

    /**
     * Xóa toàn bộ sản phẩm có trong giỏ hàng của người dùng hiện tại.
     */
    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/auth/login";
            }
            cartService.clearCart(user);
        } catch (Exception e) {
            // Ignore error
        }
        return "redirect:/cart";
    }
}
