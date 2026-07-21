const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'src', 'main', 'resources', 'templates', 'admin-dashboard.html');
let content = fs.readFileSync(filePath, 'utf8');

content = content.replace(/\$&#39;/g, "$'");

fs.writeFileSync(filePath, content, 'utf8');
console.log("Fixed quote!");
