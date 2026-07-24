package com.se1906.laptopshop.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface PaymentService {
    String createVnPayPaymentUrl(HttpServletRequest request, long amount, String orderInfo, String vnpTxnRef);
    boolean verifyVNPayCallback(Map<String, String> fields);
}
