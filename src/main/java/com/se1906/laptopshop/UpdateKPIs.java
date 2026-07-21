package com.se1906.laptopshop;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class UpdateKPIs {
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("src/main/resources/templates/admin-dashboard.html");
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        // 1. Remove buttons
        String btnPattern = "(?s)<div class=\"hidden sm:flex gap-3\">\\s*<button[^>]*>.*?Export Report\\s*</button>\\s*<button[^>]*>.*?New Entry\\s*</button>\\s*</div>";
        content = content.replaceAll(btnPattern, "<div class=\"hidden sm:flex gap-3\"></div>");

        // 2. Update Total Revenue
        String revPattern = "(?s)<h3 class=\"([^\"]*)\">\\$1\\.2M</h3>";
        String revReplacement = "<h3 class=\"$1\" th:text=\"\\${totalRevenue != null ? '$' + #numbers.formatDecimal(totalRevenue, 0, 'COMMA', 2, 'POINT') : '$0'}\">\\$1.2M</h3>";
        content = content.replaceAll(revPattern, revReplacement);

        // 3. Update Active Users to "Số Đơn Chờ Xác Nhận"
        content = content.replace("<p class=\"font-label-md text-label-md text-on-surface-variant\">Active Users</p>", "<p class=\"font-label-md text-label-md text-on-surface-variant\">Số Đơn Chờ Xác Nhận</p>");
        String activeUsersValPattern = "(?s)<h3 class=\"([^\"]*)\">45\\.2K</h3>";
        String activeUsersValReplacement = "<h3 class=\"$1\" th:text=\"\\${pendingOrders}\">45.2K</h3>";
        content = content.replaceAll(activeUsersValPattern, activeUsersValReplacement);

        // 4. Update Inventory Status to "Số Đơn Đang Giao"
        content = content.replace("<p class=\"font-label-md text-label-md text-on-surface-variant\">Inventory Status</p>", "<p class=\"font-label-md text-label-md text-on-surface-variant\">Số Đơn Đang Giao</p>");
        String invValPattern = "(?s)<h3 class=\"([^\"]*)\">8,432</h3>";
        String invValReplacement = "<h3 class=\"$1\" th:text=\"\\${deliveringOrders}\">8,432</h3>";
        content = content.replaceAll(invValPattern, invValReplacement);

        // 5. Update System Health to "Số Đơn Đã Giao"
        content = content.replace("<p class=\"font-label-md text-label-md text-on-surface-variant\">System Health</p>", "<p class=\"font-label-md text-label-md text-on-surface-variant\">Số Đơn Đã Giao</p>");
        String healthValPattern = "(?s)<h3 class=\"([^\"]*)\">99\\.9%</h3>";
        String healthValReplacement = "<h3 class=\"$1\" th:text=\"\\${deliveredOrders}\">99.9%</h3>";
        content = content.replaceAll(healthValPattern, healthValReplacement);

        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        System.out.println("Done updating KPIs and Sidebar");
    }
}
