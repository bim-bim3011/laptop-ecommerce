const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'src', 'main', 'resources', 'templates', 'admin-dashboard.html');
let content = fs.readFileSync(filePath, 'utf8');

content = content.split('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary">$1.2M</h3>')
                 .join('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary" th:text="${totalRevenue != null ? \'$\' + #numbers.formatDecimal(totalRevenue, 0, \'COMMA\', 2, \'POINT\') : \'$0\'}">$1.2M</h3>');

content = content.split('<p class="font-label-md text-label-md text-on-surface-variant">Active Users</p>')
                 .join('<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Chờ Xác Nhận</p>');
                 
content = content.split('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-primary-container">45.2K</h3>')
                 .join('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-primary-container" th:text="${pendingOrders}">45.2K</h3>');

content = content.split('<p class="font-label-md text-label-md text-on-surface-variant">Inventory Status</p>')
                 .join('<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Đang Giao</p>');

content = content.split('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-tertiary">8,432</h3>')
                 .join('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-tertiary" th:text="${deliveringOrders}">8,432</h3>');
                 
content = content.split('<p class="font-label-md text-label-md text-on-surface-variant">System Health</p>')
                 .join('<p class="font-label-md text-label-md text-on-surface-variant">Số Đơn Đã Giao</p>');

content = content.split('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary-container">99.9%</h3>')
                 .join('<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary-container" th:text="${deliveredOrders}">99.9%</h3>');

fs.writeFileSync(filePath, content, 'utf8');
console.log("Updated KPIs safely via split/join!");
