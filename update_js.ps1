$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\static\js\admin-app.js"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

# Update Active Tab Navigation
$navJsOld = "            if(target) {
                sections.forEach(s => s.classList.add('hidden'));"
$navJsNew = "            if(target) {
                links.forEach(l => {
                    l.classList.remove('bg-blue-500', 'text-white', 'active-nav');
                    l.classList.add('hover:bg-surface-container-high', 'text-on-surface-variant');
                });
                link.classList.remove('hover:bg-surface-container-high', 'text-on-surface-variant');
                link.classList.add('bg-blue-500', 'text-white', 'active-nav');
                
                sections.forEach(s => s.classList.add('hidden'));"
$content = $content.Replace($navJsOld, $navJsNew)

# Enhance Edit button (transparent, nice)
$editBtnOld = 'class="text-secondary mr-2">Edit</button>'
$editBtnNew = 'class="text-secondary hover:text-blue-700 bg-transparent hover:bg-blue-50 p-2 rounded-full transition-colors mr-1 tooltip" title="Edit"><span class="material-symbols-outlined text-sm">edit</span></button>'
$content = $content.Replace($editBtnOld, $editBtnNew)

# Enhance Delete button (transparent, nice)
$deleteBtnOld = 'class="text-error">Delete</button>'
$deleteBtnNew = 'class="text-error hover:text-red-700 bg-transparent hover:bg-red-50 p-2 rounded-full transition-colors tooltip" title="Delete"><span class="material-symbols-outlined text-sm">delete</span></button>'
$content = $content.Replace($deleteBtnOld, $deleteBtnNew)

# Format User status
$userStatusOld = '<td class="p-4">${user.status}</td>'
$userStatusNew = '<td class="p-4"><span class="px-3 py-1 rounded-full border border-gray-300 bg-transparent text-sm font-medium ${user.status === ''ACTIVE'' ? ''text-green-600 border-green-200'' : ''text-red-600 border-red-200''}">${user.status}</span></td>'
$content = $content.Replace($userStatusOld, $userStatusNew)

# Update the "Gifts" button
$giftBtnOld = 'class="text-tertiary mr-2 text-sm font-medium">🎁 Gifts</button>'
$giftBtnNew = 'class="text-tertiary hover:text-yellow-700 bg-transparent hover:bg-yellow-50 p-2 rounded-full transition-colors mr-1 tooltip" title="Manage Gifts"><span class="material-symbols-outlined text-sm">redeem</span></button>'
$content = $content.Replace($giftBtnOld, $giftBtnNew)

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated JS!"
