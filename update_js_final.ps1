$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\static\js\admin-app.js"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

# 1. Update Active Tab Navigation
$content = [regex]::Replace($content, '(?s)if\(target\)\s*\{\s*sections\.forEach\(s => s\.classList\.add\(''hidden''\)\);', "if(target) {
                links.forEach(l => {
                    l.classList.remove('bg-blue-500', 'text-white', 'active-nav');
                    l.classList.add('hover:bg-surface-container-high', 'text-on-surface-variant');
                });
                link.classList.remove('hover:bg-surface-container-high', 'text-on-surface-variant');
                link.classList.add('bg-blue-500', 'text-white', 'active-nav');
                
                sections.forEach(s => s.classList.add('hidden'));")

# Enhance Edit button (pill)
$editBtnOld = 'class="text-secondary mr-2">Edit</button>'
$editBtnNew = 'class="text-blue-600 border border-blue-200 bg-transparent hover:bg-blue-50 px-3 py-1 rounded-full transition-colors mr-2 tooltip inline-flex items-center gap-1" title="Edit"><span class="material-symbols-outlined text-[16px]">edit</span> <span class="text-sm font-medium">Edit</span></button>'
$content = $content.Replace($editBtnOld, $editBtnNew)

# Enhance Delete button (pill)
$deleteBtnOld = 'class="text-error">Delete</button>'
$deleteBtnNew = 'class="text-red-600 border border-red-200 bg-transparent hover:bg-red-50 px-3 py-1 rounded-full transition-colors tooltip inline-flex items-center gap-1" title="Delete"><span class="material-symbols-outlined text-[16px]">delete</span> <span class="text-sm font-medium">Delete</span></button>'
$content = $content.Replace($deleteBtnOld, $deleteBtnNew)

# Enhance Gift button (pill)
$giftBtnOld = 'class="text-tertiary mr-2 text-sm font-medium">🎁 Gifts</button>'
$giftBtnNew = 'class="text-yellow-600 border border-yellow-200 bg-transparent hover:bg-yellow-50 px-3 py-1 rounded-full transition-colors mr-2 tooltip inline-flex items-center gap-1" title="Manage Gifts"><span class="material-symbols-outlined text-[16px]">redeem</span> <span class="text-sm font-medium">Gifts</span></button>'
$content = $content.Replace($giftBtnOld, $giftBtnNew)

# Format User status
$userStatusOld = '<td class="p-4">${user.status}</td>'
$userStatusNew = '<td class="p-4"><span class="px-3 py-1 rounded-full border border-gray-300 bg-transparent text-sm font-medium ${user.status === ''ACTIVE'' ? ''text-green-600 border-green-200'' : ''text-red-600 border-red-200''}">${user.status}</span></td>'
$content = $content.Replace($userStatusOld, $userStatusNew)

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated JS Final!"
