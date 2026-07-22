$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\static\js\admin-app.js"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

# Update Edit button
$editBtnOld = 'class="text-secondary hover:text-blue-700 bg-transparent hover:bg-blue-50 p-2 rounded-full transition-colors mr-1 tooltip" title="Edit"><span class="material-symbols-outlined text-sm">edit</span></button>'
$editBtnNew = 'class="text-blue-600 border border-blue-200 bg-transparent hover:bg-blue-50 px-3 py-1 rounded-full transition-colors mr-2 tooltip flex items-center gap-1" title="Edit"><span class="material-symbols-outlined text-[16px]">edit</span> <span class="text-sm font-medium">Edit</span></button>'
$content = $content.Replace($editBtnOld, $editBtnNew)

# Update Delete button
$deleteBtnOld = 'class="text-error hover:text-red-700 bg-transparent hover:bg-red-50 p-2 rounded-full transition-colors tooltip" title="Delete"><span class="material-symbols-outlined text-sm">delete</span></button>'
$deleteBtnNew = 'class="text-red-600 border border-red-200 bg-transparent hover:bg-red-50 px-3 py-1 rounded-full transition-colors tooltip flex items-center gap-1" title="Delete"><span class="material-symbols-outlined text-[16px]">delete</span> <span class="text-sm font-medium">Delete</span></button>'
$content = $content.Replace($deleteBtnOld, $deleteBtnNew)

# Update Gift button
$giftBtnOld = 'class="text-tertiary hover:text-yellow-700 bg-transparent hover:bg-yellow-50 p-2 rounded-full transition-colors mr-1 tooltip" title="Manage Gifts"><span class="material-symbols-outlined text-sm">redeem</span></button>'
$giftBtnNew = 'class="text-yellow-600 border border-yellow-200 bg-transparent hover:bg-yellow-50 px-3 py-1 rounded-full transition-colors mr-2 tooltip flex items-center gap-1" title="Manage Gifts"><span class="material-symbols-outlined text-[16px]">redeem</span> <span class="text-sm font-medium">Gifts</span></button>'
$content = $content.Replace($giftBtnOld, $giftBtnNew)

# In case the action cell container needs to flex
$content = $content.Replace('<td class="p-4">', '<td class="p-4 align-middle">')
# Actually let's just make the action cell flex
$content = [regex]::Replace($content, '<td class="p-4">\s*(<button.*?edit.*?</button>)\s*(<button.*?delete.*?</button>)\s*</td>', '<td class="p-4 align-middle"><div class="flex items-center gap-2">$1$2</div></td>')
$content = [regex]::Replace($content, '<td class="p-4">\s*(<button.*?edit.*?</button>)\s*(<button.*?redeem.*?</button>)\s*(<button.*?delete.*?</button>)\s*</td>', '<td class="p-4 align-middle"><div class="flex items-center gap-2">$1$2$3</div></td>')

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated JS!"
