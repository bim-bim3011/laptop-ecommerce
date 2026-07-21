package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.repository.OrderRepository;
import com.se1906.laptopshop.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/vnpay-return")
    public String paymentReturn(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        if (paymentService.verifyVNPayCallback(fields)) {
            String transactionStatus = request.getParameter("vnp_ResponseCode");
            String vnpTxnRef = request.getParameter("vnp_TxnRef");
            
            try {
                int orderId = Integer.parseInt(vnpTxnRef);
                Optional<Order> orderOptional = orderRepository.findById(orderId);
                
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    if ("00".equals(transactionStatus)) {
                        order.setPaymentStatus("PAID");
                        order.setStatus("PROCESSING");
                        orderRepository.save(order);
                        redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công! Đơn hàng của bạn đang được xử lý.");
                    } else {
                        order.setPaymentStatus("FAILED");
                        order.setStatus("CANCELLED");
                        orderRepository.save(order);
                        redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thất bại hoặc đã bị hủy. Đơn hàng đã bị hủy.");
                    }
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xử lý đơn hàng.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Chữ ký bảo mật VNPay không hợp lệ!");
        }

        return "redirect:/orders";
    }
}
