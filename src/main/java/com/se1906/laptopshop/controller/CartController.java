package com.se1906.laptopshop.controller;

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
import com.se1906.laptopshop.repository.UserRepository;
import com.se1906.laptopshop.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    private User getOrCreateLoggedInUser(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            java.util.List<User> users = userRepository.findAll();
            if (!users.isEmpty()) {
                loggedInUser = users.get(0);
            } else {
                User mockUser = new User();
                mockUser.setFullName("Nguyễn Văn A");
                mockUser.setEmail("testuser@gmail.com");
                mockUser.setPhoneNumber("0987654321");
                mockUser.setStatus("ACTIVE");
                loggedInUser = userRepository.save(mockUser);
            }
            session.setAttribute("currentUser", loggedInUser);
        }
        return loggedInUser;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = getOrCreateLoggedInUser(session);
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

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute com.se1906.laptopshop.dto.AddToCartRequest request,
            HttpSession session,
            @RequestHeader(value = "Referer", required = false) String referer,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            User user = getOrCreateLoggedInUser(session);
            cartService.addToCart(user, request.getConfigurationId(), request.getQuantity());
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi không xác định!");
        }
        return "redirect:" + (referer != null ? referer : "/cart");
    }

    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam("itemId") int itemId, RedirectAttributes redirectAttributes) {
        try {
            cartService.removeCartItem(itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm khỏi giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa sản phẩm!");
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCartItem(@ModelAttribute com.se1906.laptopshop.dto.UpdateCartItemRequest request,
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
}
