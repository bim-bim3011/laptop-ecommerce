const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'src', 'main', 'resources', 'templates', 'admin-dashboard.html');
let content = fs.readFileSync(filePath, 'utf8');

const regex = /th:text="\$\{totalRevenue != null \? ' \+ #numbers\.formatDecimal\(totalRevenue, 0, 'COMMA', 2, 'POINT'\) : '\$0'\}"/g;
const replacement = 'th:text="${totalRevenue != null ? \'$\' + #numbers.formatDecimal(totalRevenue, 0, \'COMMA\', 2, \'POINT\') : \'$0\'}"';

content = content.replace(regex, replacement);

fs.writeFileSync(filePath, content, 'utf8');
console.log("Fixed Thymeleaf syntax!");
