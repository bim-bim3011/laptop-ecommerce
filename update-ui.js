const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'src', 'main', 'resources', 'templates', 'admin-dashboard.html');
let content = fs.readFileSync(filePath, 'utf8');

// 1. Sidebar round tabs
content = content.replace(/rounded-2xl/g, () => 'rounded-full');

// 2. Remove buttons
const btnRegex = /<div class="hidden sm:flex gap-3">[\s\S]*?New Entry\s*<\/button>\s*<\/div>/g;
content = content.replace(btnRegex, () => '<div class="hidden sm:flex gap-3"></div>');

// 3. Update Total Revenue
const revPattern = /<h3 class="([^"]*)">\$1\.2M<\/h3>/;
content = content.replace(revPattern, () => '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary" th:text="${totalRevenue != null ? \'$&#39; + #numbers.formatDecimal(totalRevenue, 0, \'COMMA\', 2, \'POINT\') : \'$0\'}">$1.2M</h3>');
// wait, I can just use string with no special variables if I use replacer function.
// Actually, `\'$\'` is fine if it's in a string in JS, I can just write `'$'`.
// Wait, the single quotes in Thymeleaf: `th:text="${... ? '$' + ... : '$0'}"`
// Let's escape it properly in JS template literal.

content = content.replace(/>Active Users<\/p>/g, () => '>Số Đơn Chờ Xác Nhận</p>');
const usersPattern = /<h3 class="([^"]*)">45\.2K<\/h3>/;
content = content.replace(usersPattern, () => '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-primary-container" th:text="${pendingOrders}">45.2K</h3>');

content = content.replace(/>Inventory Status<\/p>/g, () => '>Số Đơn Đang Giao</p>');
const invPattern = /<h3 class="([^"]*)">8,432<\/h3>/;
content = content.replace(invPattern, () => '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-tertiary" th:text="${deliveringOrders}">8,432</h3>');

content = content.replace(/>System Health<\/p>/g, () => '>Số Đơn Đã Giao</p>');
const healthPattern = /<h3 class="([^"]*)">99\.9%<\/h3>/;
content = content.replace(healthPattern, () => '<h3 class="font-display-lg text-display-lg text-on-surface transition-colors hover:text-secondary-container" th:text="${deliveredOrders}">99.9%</h3>');

// Update user profile in the bottom left
const oldProfileHtml = `<div class="w-10 h-10 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold">
                    A
                </div>
                <div>
                    <p class="font-label-md text-label-md text-on-surface">Admin User</p>
                    <p class="text-sm text-on-surface-variant">admin@lapzone.com</p>
                </div>`;
                
const newProfileHtml = `<div class="w-10 h-10 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold" th:text="\${session.admin.fullName != null ? #strings.substring(session.admin.fullName, 0, 1) : 'A'}">
                    A
                </div>
                <div>
                    <p class="font-label-md text-label-md text-on-surface" th:text="\${session.admin.fullName}">Admin User</p>
                    <p class="text-sm text-on-surface-variant" th:text="\${session.admin.email}">admin@lapzone.com</p>
                </div>`;

content = content.replace(oldProfileHtml, () => newProfileHtml);
if (content.indexOf('th:text="${session.admin.fullName}"') === -1) {
    content = content.replace(/<p class="font-label-md text-label-md text-on-surface">Admin User<\/p>/, () => '<p class="font-label-md text-label-md text-on-surface" th:text="${session.admin.fullName}">Admin User</p>');
    content = content.replace(/<p class="text-sm text-on-surface-variant">admin@lapzone\.com<\/p>/, () => '<p class="text-sm text-on-surface-variant" th:text="${session.admin.email}">admin@lapzone.com</p>');
    content = content.replace(/<div class="w-10 h-10 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold">\s*A\s*<\/div>/, () => '<div class="w-10 h-10 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold" th:text="${session.admin.fullName != null ? #strings.substring(session.admin.fullName, 0, 1) : \'A\'}">A</div>');
}

fs.writeFileSync(filePath, content, 'utf8');
console.log("Updated KPIs and Sidebar safely!");
