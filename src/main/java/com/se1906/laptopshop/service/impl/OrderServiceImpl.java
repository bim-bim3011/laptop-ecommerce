package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.Cart;
import com.se1906.laptopshop.entity.CartItem;
import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.GiftDetail;
import com.se1906.laptopshop.entity.Laptop;
import com.se1906.laptopshop.entity.Order;
import com.se1906.laptopshop.entity.OrderDetail;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.CartItemRepository;
import com.se1906.laptopshop.repository.OrderDetailRepository;
import com.se1906.laptopshop.repository.OrderRepository;
import com.se1906.laptopshop.service.OrderService;
import com.se1906.laptopshop.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;

import com.se1906.laptopshop.entity.Promotion;
import com.se1906.laptopshop.repository.PromotionRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    OrderDetailRepository orderDetailRepository;

    CartItemRepository cartItemRepository;
    PromotionRepository promotionRepository;
    static final List<String> CANCELLABLE_STATUSES = Arrays.asList("PENDING", "PENDING_PAYMENT", "PROCESSING");

    PaymentService paymentService;

    /**
     * Lấy danh sách toàn bộ các đơn hàng của một người dùng cụ thể.
     * Dữ liệu được sắp xếp theo ngày đặt giảm dần để hiển thị lịch sử mua hàng một cách trực quan.
     */
    @Override
    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    /**
     * Tìm kiếm và trả về thông tin chi tiết của một đơn hàng dựa trên mã đơn.
     * Hàm đảm bảo người dùng chỉ xem được thông tin đơn hàng do chính họ đặt.
     */
    @Override
    public Order getOrderByIdAndUser(int orderId, User user) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getUser() != null && order.getUser().getUserId().equals(user.getUserId())) {
            return order;
        }
        return null;
    }

    /**
     * Trích xuất danh sách các đối tượng CartItem dựa trên danh sách ID sản phẩm được chọn.
     * Hàm này được sử dụng để lấy thông tin các mặt hàng chuẩn bị cho quá trình thanh toán.
     */
    @Override
    public List<CartItem> getSelectedCartItems(List<Integer> selectedItemIds) {
        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            return new ArrayList<>();
        }
        return cartItemRepository.findAllById(selectedItemIds);
    }

    /**
     * Lấy danh sách các phần quà khuyến mãi đi kèm với các cấu hình sản phẩm đang có trong giỏ hàng.
     * Sử dụng Set để lọc trùng lặp, đảm bảo mỗi loại quà chỉ xuất hiện một lần trong danh sách.
     */
    @Override
    public List<GiftDetail> getGiftDetailsFromCartItems(List<CartItem> selectedItems) {
        List<GiftDetail> giftDetails = new ArrayList<>();
        Set<Integer> addedGiftItemIds = new HashSet<>();

        for (CartItem item : selectedItems) {
            if (item.getConfigurationVersion() != null && item.getConfigurationVersion().getLaptop() != null) {
                Laptop laptop = item.getConfigurationVersion().getLaptop();
                if (laptop.getConfigurationVersions() != null) {
                    for (ConfigurationVersion cv : laptop.getConfigurationVersions()) {
                        if (cv.getGiftDetails() != null) {
                            for (GiftDetail gd : cv.getGiftDetails()) {
                                if (gd.getGiftItem() != null) {
                                    Integer itemId = gd.getGiftItem().getGiftItemId();
                                    if (!addedGiftItemIds.contains(itemId)) {
                                        giftDetails.add(gd);
                                        addedGiftItemIds.add(itemId);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return giftDetails;
    }
    @Override
    public List<GiftDetail> getGiftDetailsFromOrder(Order order) {
        List<GiftDetail> giftDetails = new ArrayList<>();
        Set<Integer> addedGiftItemIds = new HashSet<>();

        if (order.getOrderDetails() != null) {
            for (OrderDetail detail : order.getOrderDetails()) {
                if (detail.getConfigurationVersion() != null && detail.getConfigurationVersion().getLaptop() != null) {
                    Laptop laptop = detail.getConfigurationVersion().getLaptop();
                    if (laptop.getConfigurationVersions() != null) {
                        for (ConfigurationVersion cv : laptop.getConfigurationVersions()) {
                            if (cv.getGiftDetails() != null) {
                                for (GiftDetail gd : cv.getGiftDetails()) {
                                    if (gd.getGiftItem() != null) {
                                        Integer itemId = gd.getGiftItem().getGiftItemId();
                                        if (!addedGiftItemIds.contains(itemId)) {
                                            giftDetails.add(gd);
                                            addedGiftItemIds.add(itemId);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return giftDetails;
    }
    
    /**
     * Tổng hợp và chuẩn bị đầy đủ dữ liệu (sản phẩm, tổng tiền, quà tặng) cho trang Checkout.
     * Hàm tính toán tổng tiền tạm tính và đưa tất cả vào Model để render ra giao diện.
     */
    public void prepareCheckoutData(List<Integer> selectedItemIds, Model model) {
        List<CartItem> selectedItems = cartItemRepository.findAllById(selectedItemIds);

        List<com.se1906.laptopshop.entity.GiftDetail> giftDetails = new ArrayList<>();
        Set<Integer> addedGiftItemIds = new HashSet<>();

        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            totalAmount += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();

            // GIỮ NGUYÊN GỐC: Add gifts from ALL configurations of the selected laptop
            if (item.getConfigurationVersion() != null && item.getConfigurationVersion().getLaptop() != null) {
                com.se1906.laptopshop.entity.Laptop laptop = item.getConfigurationVersion().getLaptop();
                if (laptop.getConfigurationVersions() != null) {
                    for (com.se1906.laptopshop.entity.ConfigurationVersion cv : laptop.getConfigurationVersions()) {
                        if (cv.getGiftDetails() != null) {
                            for (com.se1906.laptopshop.entity.GiftDetail gd : cv.getGiftDetails()) {
                                if (gd.getGiftItem() != null) {
                                    gd.getGiftItem().getItemName();
                                    Integer itemId = gd.getGiftItem().getGiftItemId();
                                    if (!addedGiftItemIds.contains(itemId)) {
                                        giftDetails.add(gd);
                                        addedGiftItemIds.add(itemId);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("selectedItemIds", selectedItemIds);
        model.addAttribute("giftDetails", giftDetails);
    }

    /**
     * Xử lý logic nghiệp vụ chính để tạo một đơn hàng mới từ các sản phẩm được chọn.
     * Hàm này thực hiện tính toán phí ship, mã giảm giá, lưu thông tin Order và OrderDetail, sau đó xóa sản phẩm khỏi giỏ.
     */
    @Override
    @Transactional
    public String placeOrder(List<Integer> selectedItemIds, String paymentMethod, String couponCode, User user,
            HttpServletRequest request, String receiverName, String receiverPhone, String shippingAddress, String province, String district, String ward, String shippingMethod) {
        List<CartItem> selectedItems = cartItemRepository.findAllById(selectedItemIds);

        // Tạo đơn hàng tổng
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderDate(LocalDateTime.now());

        if ("vnpay".equalsIgnoreCase(paymentMethod)) {
            newOrder.setPaymentMethod("VNPAY");
            newOrder.setPaymentStatus("PENDING");
            newOrder.setStatus("PENDING_PAYMENT");
        } else {
            newOrder.setPaymentMethod("COD");
            newOrder.setPaymentStatus("PENDING");
            newOrder.setStatus("PENDING"); // Khách đặt xong ở trạng thái CHỜ XÁC NHẬN
        }

        // Tính tổng tiền
        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            totalAmount += item.getConfigurationVersion().getPrice().doubleValue() * item.getQuantity();
        }

        // Tính toán discount
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            String trimmedCode = couponCode.trim();
            Promotion promotion = promotionRepository.findByCouponCode(trimmedCode).orElse(null);
            
            if (promotion != null && promotion.getCouponCode() != null && !promotion.getCouponCode().equals(trimmedCode)) {
                promotion = null;
            }
            
            if (promotion != null) {
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                if (!now.isBefore(promotion.getStartDate()) && !now.isAfter(promotion.getEndDate())) {
                    if (promotion.getMinOrderValue() == null
                            || java.math.BigDecimal.valueOf(totalAmount).compareTo(promotion.getMinOrderValue()) >= 0) {
                        double discountAmount = 0;
                        if ("PERCENTAGE".equalsIgnoreCase(promotion.getDiscountType())) {
                            discountAmount = totalAmount * promotion.getDiscountValue().doubleValue() / 100;
                            if (promotion.getMaxDiscountAmount() != null
                                    && discountAmount > promotion.getMaxDiscountAmount().doubleValue()) {
                                discountAmount = promotion.getMaxDiscountAmount().doubleValue();
                            }
                        } else if ("FIXED_AMOUNT".equalsIgnoreCase(promotion.getDiscountType())) {
                            discountAmount = promotion.getDiscountValue().doubleValue();
                        }

                        if (discountAmount > totalAmount)
                            discountAmount = totalAmount;

                        totalAmount -= discountAmount;
                        newOrder.setDiscountAmount(java.math.BigDecimal.valueOf(discountAmount));
                        newOrder.setPromotion(promotion);
                    }
                }
            }
        }

        if ("express".equalsIgnoreCase(shippingMethod)) {
            totalAmount += 150000;
        }

        newOrder.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));

        // Lưu đơn hàng tổng để lấy ID
        Order savedOrder = orderRepository.save(newOrder);

        // Nối chuỗi địa chỉ
        String fullAddress = (shippingAddress != null ? shippingAddress : "");
        if (ward != null && !ward.trim().isEmpty()) fullAddress += ", " + ward;
        if (district != null && !district.trim().isEmpty()) fullAddress += ", " + district;
        if (province != null && !province.trim().isEmpty()) fullAddress += ", " + province;

        // Tạo chi tiết đơn hàng (OrderDetail)
        for (CartItem item : selectedItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setConfigurationVersion(item.getConfigurationVersion());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getConfigurationVersion().getPrice());
            
            // Ghi nhận thông tin giao hàng vào chi tiết đơn
            detail.setReceiverName(receiverName);
            detail.setReceiverPhone(receiverPhone);
            detail.setShippingAddress(fullAddress);
            
            orderDetailRepository.save(detail);
        }

        // Xóa sản phẩm đã thanh toán khỏi giỏ hàng
        if (!selectedItems.isEmpty()) {
            Cart cart = selectedItems.get(0).getCart();
            if (cart != null && cart.getCartItems() != null) {
                cart.getCartItems().removeAll(selectedItems);
            }
        }
        cartItemRepository.deleteAllInBatch(selectedItems);

        // Trả kết quả chuyển hướng hoặc báo thành công
        if ("vnpay".equalsIgnoreCase(paymentMethod)) {
            return paymentService.createVnPayPaymentUrl(request, (long) totalAmount,
                    "Thanh toan don hang " + savedOrder.getOrderId(), String.valueOf(savedOrder.getOrderId()));
        }

        return "SUCCESS";
    }

    /**
     * Thực hiện hủy một đơn hàng nếu trạng thái hiện tại nằm trong danh sách cho phép hủy.
     * Nếu đơn hàng đã được thanh toán qua VNPAY, hàm sẽ đánh dấu trạng thái chờ hoàn tiền.
     */
    @Override
    @Transactional
    public boolean cancelOrder(int id, User user) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            return false;
        }

        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            return false;
        }

        // GIỮ NGUYÊN GỐC + HOÀN TIỀN: Nếu đã thanh toán qua VNPay thành công thì hoàn
        // tiền
        if ("VNPAY".equalsIgnoreCase(order.getPaymentMethod())
                && "SUCCESS".equalsIgnoreCase(order.getPaymentStatus())) {
            // Kích hoạt dòng này sau khi bạn tích hợp phương thức refund vào
            // paymentService:
            // paymentService.refundVnPay(order, user);
            order.setPaymentStatus("REFUNDED");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        return true;
    }

    /**
     * Lấy chi tiết của một đơn hàng bằng ID và đối chiếu với User để bảo mật thông tin.
     * Trả về Null nếu không tìm thấy đơn hoặc người dùng không có quyền truy cập.
     */
    @Override
    public Order getOrderDetail(int id, User user) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            return null;
        }
        return order;
    }

    /**
     * Cập nhật trạng thái của đơn hàng từ "Chờ xử lý" sang "Đang vận chuyển".
     * Chức năng này thường được sử dụng bởi Admin để quản lý trạng thái giao hàng.
     */
    @Override
    @Transactional
    public boolean shipOrder(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || !"PENDING".equalsIgnoreCase(order.getStatus())) {
            return false;
        }
        order.setStatus("SHIPPING"); // Duyệt đơn chuyển thành Đang vận chuyển
        orderRepository.save(order);
        return true;
    }

    /**
     * Đánh dấu đơn hàng là "Đã giao hàng" và hoàn tất quy trình giao dịch.
     * Đồng thời cập nhật trạng thái thanh toán thành "Thành công" đối với phương thức COD.
     */
    @Override
    @Transactional
    public boolean completeOrder(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || !"SHIPPING".equalsIgnoreCase(order.getStatus())) {
            return false;
        }
        if (order.getOrderDetails() != null) {
            for (OrderDetail detail : order.getOrderDetails()) {
                ConfigurationVersion config = detail.getConfigurationVersion();
                if (config != null) {
                    // Vì getStockQuantity() và getQuantity() trả về kiểu int nguyên thủy,
                    // không cần check != null bọc ngoài nữa.
                    int currentStock = config.getStockQuantity();
                    int orderQuantity = detail.getQuantity();

                    // Tính số lượng tồn kho mới sau khi trừ
                    int newStock = currentStock - orderQuantity;
                    if (newStock < 0) newStock = 0; // Đảm bảo kho không bị âm

                    config.setStockQuantity(newStock);
                }
            }
        }
        order.setStatus("DELIVERED"); // Đã giao hàng
        if ("COD".equalsIgnoreCase(order.getPaymentMethod())) {
            order.setPaymentStatus("SUCCESS");
        }
        orderRepository.save(order);
        return true;
    }

    /**
     * Truy xuất toàn bộ danh sách đơn hàng có trong hệ thống.
     * Hàm này phục vụ cho chức năng thống kê và quản lý của người quản trị (Admin).
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}
