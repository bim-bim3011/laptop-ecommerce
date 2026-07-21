$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

$content = $content.Replace('rounded-2xl', 'rounded-full')

$oldBtns = '<div class="hidden sm:flex gap-3">
  <button class="px-4 py-2 border border-secondary text-secondary rounded font-label-md text-label-md hover:bg-secondary hover:text-on-secondary transition-all duration-300 shadow-sm hover:shadow-md active:scale-95 flex items-center gap-2 group">
  <span class="material-symbols-outlined text-sm group-hover:-translate-y-1 transition-transform duration-300">download</span> Export Report
                      </button>
  <button class="px-4 py-2 bg-primary-container text-on-primary-container rounded font-label-md text-label-md hover:bg-primary hover:text-on-primary transition-all duration-300 shadow-sm hover:shadow-md active:scale-95 flex items-center gap-2 group">
  <span class="material-symbols-outlined text-sm group-hover:rotate-90 transition-transform duration-300">add</span> New Entry
                      </button>
  </div>'
$newBtns = '<div class="hidden sm:flex gap-3"></div>'
$content = $content.Replace($oldBtns, $newBtns)

$oldRev = '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary">$1.2M</h3>'
$newRev = '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary" th:text="${totalRevenue != null ? ''$'' + #numbers.formatDecimal(totalRevenue, 0, ''COMMA'', 2, ''POINT'') : ''$0''}">$1.2M</h3>'
$content = $content.Replace($oldRev, $newRev)

$oldUsers = '<p class="font-label-md text-label-md text-on-surface-variant">Active Users</p>
  <div class="w-8 h-8 rounded bg-primary-fixed flex items-center justify-center text-primary transition-transform duration-500 hover:rotate-12 hover:scale-110">
  <span class="material-symbols-outlined">group</span>
  </div>
  </div>
  <div class="flex items-baseline gap-2">
  <h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-primary-container">45.2K</h3>'
$newUsers = '<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Chờ Xác Nhận</p>
  <div class="w-8 h-8 rounded bg-primary-fixed flex items-center justify-center text-primary transition-transform duration-500 hover:rotate-12 hover:scale-110">
  <span class="material-symbols-outlined">group</span>
  </div>
  </div>
  <div class="flex items-baseline gap-2">
  <h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-primary-container" th:text="${pendingOrders}">45.2K</h3>'
$content = $content.Replace($oldUsers, $newUsers)

$oldInv = '<p class="font-label-md text-label-md text-on-surface-variant">Inventory Status</p>
  <div class="w-8 h-8 rounded bg-tertiary-fixed flex items-center justify-center text-tertiary transition-transform duration-500 hover:-rotate-12 hover:scale-110">
  <span class="material-symbols-outlined">inventory_2</span>
  </div>
  </div>
  <div class="flex items-baseline gap-2">
  <h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-tertiary">8,432</h3>'
$newInv = '<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Đang Giao</p>
  <div class="w-8 h-8 rounded bg-tertiary-fixed flex items-center justify-center text-tertiary transition-transform duration-500 hover:-rotate-12 hover:scale-110">
  <span class="material-symbols-outlined">inventory_2</span>
  </div>
  </div>
  <div class="flex items-baseline gap-2">
  <h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-tertiary" th:text="${deliveringOrders}">8,432</h3>'
$content = $content.Replace($oldInv, $newInv)

$oldHealth = '<p class="font-label-md text-label-md text-on-surface-variant">System Health</p>
  <div class="w-8 h-8 rounded bg-secondary-fixed flex items-center justify-center text-secondary transition-transform duration-500 group-hover:scale-110">
  <span class="material-symbols-outlined">dns</span>
  </div>
  </div>
  <div class="flex items-baseline gap-2">
  <h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary-container">99.9%</h3>'
$newHealth = '<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Đã Giao</p>
  <div class="w-8 h-8 rounded bg-secondary-fixed flex items-center justify-center text-secondary transition-transform duration-500 group-hover:scale-110">
  <span class="material-symbols-outlined">dns</span>
  </div>
  </div>
  <div class="flex items-baseline gap-2">
  <h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary-container" th:text="${deliveredOrders}">99.9%</h3>'
$content = $content.Replace($oldHealth, $newHealth)

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated KPIs!"
