package com.se1906.laptopshop.service;

import com.se1906.laptopshop.config.VNPayConfig;
import com.se1906.laptopshop.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

@Service
public class PaymentService {

    @Autowired
    private VNPayConfig vnPayConfig;

    public String createVnPayPaymentUrl(HttpServletRequest request, long amount, String orderInfo, String vnpTxnRef) {
        long amountInVND = amount * 100;
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInVND));
        vnpParamsMap.put("vnp_OrderInfo", orderInfo);
        vnpParamsMap.put("vnp_OrderType", "other"); // Required field by VNPay
        vnpParamsMap.put("vnp_BankCode", "NCB"); // Added BankCode as requested
        vnpParamsMap.put("vnp_TxnRef", vnpTxnRef);

        String ipAddr = VNPayUtil.getIpAddress(request);
        if (ipAddr != null && ipAddr.equals("0:0:0:0:0:0:0:1")) {
            ipAddr = "127.0.0.1";
        }
        vnpParamsMap.put("vnp_IpAddr", ipAddr);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);

        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.secretKey, hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        return vnPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    public boolean verifyVNPayCallback(Map<String, String> fields) {
        if (!fields.containsKey("vnp_SecureHash")) {
            return false;
        }
        String vnp_SecureHash = fields.get("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        fields.remove("vnp_SecureHash");

        String signValue = VNPayUtil.hmacSHA512(vnPayConfig.secretKey, VNPayUtil.getPaymentURL(fields, false));
        return signValue.equals(vnp_SecureHash);
    }
}
