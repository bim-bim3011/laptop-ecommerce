const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'src', 'main', 'resources', 'templates', 'admin-dashboard.html');
let content = fs.readFileSync(filePath, 'utf8');

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

content = content.replace(oldProfileHtml, newProfileHtml);
// If it didn't match with exactly that spacing, we can fallback to regex
if (content.indexOf('th:text="${session.admin.fullName}"') === -1) {
    content = content.replace(/<p class="font-label-md text-label-md text-on-surface">Admin User<\/p>/, '<p class="font-label-md text-label-md text-on-surface" th:text="${session.admin.fullName}">Admin User</p>');
    content = content.replace(/<p class="text-sm text-on-surface-variant">admin@lapzone\.com<\/p>/, '<p class="text-sm text-on-surface-variant" th:text="${session.admin.email}">admin@lapzone.com</p>');
    content = content.replace(/<div class="w-10 h-10 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold">\s*A\s*<\/div>/, '<div class="w-10 h-10 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold" th:text="${session.admin.fullName != null ? #strings.substring(session.admin.fullName, 0, 1) : \'A\'}">A</div>');
}

fs.writeFileSync(filePath, content, 'utf8');
console.log("Updated User Profile!");
