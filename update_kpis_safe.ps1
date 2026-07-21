$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$lines = Get-Content -Path $filePath -Encoding UTF8
$newLines = @()

for ($i = 0; $i -lt $lines.Count; $i++) {
    $line = $lines[$i]
    
    # Sidebar
    if ($line -match 'class="nav-link.*rounded-2xl') {
        $line = $line.Replace('rounded-2xl', 'rounded-full')
    }
    
    # Export / New Entry buttons removal
    if ($line -match '<div class="hidden sm:flex gap-3">') {
        $newLines += '<div class="hidden sm:flex gap-3"></div>'
        # Skip next 6 lines which contain the buttons
        $i += 6
        continue
    }

    # Revenue
    if ($line -match 'hover:text-secondary">\$1\.2M</h3>') {
        $line = '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary" th:text="${totalRevenue != null ? ''$'' + #numbers.formatDecimal(totalRevenue, 0, ''COMMA'', 2, ''POINT'') : ''$0''}">$1.2M</h3>'
    }

    # Active Users -> Số Đơn Chờ Xác Nhận
    if ($line -match '>Active Users</p>') {
        $line = $line.Replace('>Active Users</p>', '>Số Đơn Chờ Xác Nhận</p>')
    }
    if ($line -match 'hover:text-primary-container">45\.2K</h3>') {
        $line = '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-primary-container" th:text="${pendingOrders}">45.2K</h3>'
    }

    # Inventory Status -> Số Đơn Đang Giao
    if ($line -match '>Inventory Status</p>') {
        $line = $line.Replace('>Inventory Status</p>', '>Số Đơn Đang Giao</p>')
    }
    if ($line -match 'hover:text-tertiary">8,432</h3>') {
        $line = '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-tertiary" th:text="${deliveringOrders}">8,432</h3>'
    }

    # System Health -> Số Đơn Đã Giao
    if ($line -match '>System Health</p>') {
        $line = $line.Replace('>System Health</p>', '>Số Đơn Đã Giao</p>')
    }
    if ($line -match 'hover:text-secondary-container">99\.9%</h3>') {
        $line = '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary-container" th:text="${deliveredOrders}">99.9%</h3>'
    }

    $newLines += $line
}

Set-Content -Path $filePath -Value $newLines -Encoding UTF8
Write-Host "Updated safely!"
