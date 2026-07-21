$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

# 1. Remove the "Export Report" and "New Entry" buttons
$btnPattern = '(?s)<div class="hidden sm:flex gap-3">\s*<button[^>]*>.*?Export Report\s*</button>\s*<button[^>]*>.*?New Entry\s*</button>\s*</div>'
$content = [regex]::Replace($content, $btnPattern, "")

# 2. Update Total Revenue
$revPattern = '(?s)<h3 class="([^"]*)">\$1\.2M</h3>'
$revReplacement = '<h3 class="$1" th:text="${totalRevenue != null ? ''$'' + #numbers.formatDecimal(totalRevenue, 0, ''COMMA'', 2, ''POINT'') : ''$0''}">$1.2M</h3>'
$content = [regex]::Replace($content, $revPattern, $revReplacement)

# 3. Update Active Users to "Số Đơn Chờ Xác Nhận"
$content = $content.Replace('<p class="font-label-md text-label-md text-on-surface-variant">Active Users</p>', '<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Chờ Xác Nhận</p>')
$activeUsersValPattern = '(?s)<h3 class="([^"]*)">45\.2K</h3>'
$activeUsersValReplacement = '<h3 class="$1" th:text="${pendingOrders}">45.2K</h3>'
$content = [regex]::Replace($content, $activeUsersValPattern, $activeUsersValReplacement)

# 4. Update Inventory Status to "Số Đơn Đang Giao"
$content = $content.Replace('<p class="font-label-md text-label-md text-on-surface-variant">Inventory Status</p>', '<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Đang Giao</p>')
$invValPattern = '(?s)<h3 class="([^"]*)">8,432</h3>'
$invValReplacement = '<h3 class="$1" th:text="${deliveringOrders}">8,432</h3>'
$content = [regex]::Replace($content, $invValPattern, $invValReplacement)

# 5. Update System Health to "Số Đơn Đã Giao"
$content = $content.Replace('<p class="font-label-md text-label-md text-on-surface-variant">System Health</p>', '<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Đã Giao</p>')
$healthValPattern = '(?s)<h3 class="([^"]*)">99\.9%</h3>'
$healthValReplacement = '<h3 class="$1" th:text="${deliveredOrders}">99.9%</h3>'
$content = [regex]::Replace($content, $healthValPattern, $healthValReplacement)

# 6. Sidebar active tab rounding: "bo tròn góc ở khung chọn"
$content = $content.Replace('bg-blue-500 text-white rounded-2xl font-body-md text-body-md shadow-sm active-nav', 'bg-blue-500 text-white rounded-full font-body-md text-body-md shadow-sm active-nav')
$content = $content.Replace('text-on-surface-variant hover:bg-surface-container-high hover:text-on-surface rounded-2xl', 'text-on-surface-variant hover:bg-surface-container-high hover:text-on-surface rounded-full')

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated KPIs and Sidebar!"
