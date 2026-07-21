$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

$sections = @(
    @{ id="section-laptop"; title="Laptops"; btn="New Laptop"; modal="laptop"; form="createLaptop(event)"; inputs='<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label class="block mb-1 text-sm font-medium">Laptop Name</label>
                    <input type="text" id="laptopName" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Brand</label>
                    <select id="laptopBrand" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required></select>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Category</label>
                    <select id="laptopCategory" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required></select>
                </div>
                <div class="md:col-span-2">
                    <label class="block mb-1 text-sm font-medium">Description</label>
                    <input type="text" id="laptopDesc" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest md:col-span-2">
                </div>
            </div>' },
    @{ id="section-laptop-config"; title="Laptop Configurations"; btn="New Config"; modal="laptop-config"; form="createConfig(event)"; inputs='<div class="grid grid-cols-2 gap-4">
                <div class="col-span-2">
                    <label class="block mb-1 text-sm font-medium">Laptop</label>
                    <select id="confLaptop" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required></select>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">CPU</label>
                    <input type="text" id="confCpu" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">RAM</label>
                    <input type="text" id="confRam" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Storage</label>
                    <input type="text" id="confStorage" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">GPU</label>
                    <input type="text" id="confGpu" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest">
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Price</label>
                    <input type="number" step="0.01" id="confPrice" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Stock</label>
                    <input type="number" id="confStock" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
            </div>' },
    @{ id="section-promotion"; title="Promotions & Gifts"; btn="New Promotion"; modal="promotion"; form="createPromotion(event)"; inputs='<div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block mb-1 text-sm font-medium">Coupon Code</label>
                    <input type="text" id="promoCode" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Title</label>
                    <input type="text" id="promoTitle" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Discount Type</label>
                    <select id="promoDiscount" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                        <option value="PERCENTAGE">PERCENTAGE</option>
                        <option value="FIXED_AMOUNT">FIXED AMOUNT</option>
                    </select>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Value</label>
                    <input type="number" step="0.01" id="promoValue" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
            </div>' },
    @{ id="section-gift-item"; title="Gift Items"; btn="New Gift Item"; modal="gift-item"; form="createGiftItem(event)"; inputs='<div class="grid grid-cols-2 gap-4">
                <div class="col-span-2">
                    <label class="block mb-1 text-sm font-medium">Item Name</label>
                    <input type="text" id="giftItemName" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Price</label>
                    <input type="number" step="0.01" id="giftItemPrice" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Image URL</label>
                    <input type="text" id="giftItemImageUrl" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest">
                </div>
                <div class="col-span-2">
                    <label class="block mb-1 text-sm font-medium">Description</label>
                    <input type="text" id="giftItemDesc" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full bg-surface-container-lowest">
                </div>
            </div>' }
)

$modals = ""

foreach ($sec in $sections) {
    # 1. Replace header
    $headerPattern = '(?s)<h2 class="font-headline-lg text-headline-lg text-primary mb-4">\s*' + [regex]::Escape($sec.title) + '\s*</h2>'
    $newHeader = '<div class="flex justify-between items-center mb-4">
      <h2 class="font-headline-lg text-headline-lg text-primary">' + $sec.title + '</h2>
      <button onclick="document.getElementById(''modal-add-' + $sec.modal + ''').classList.remove(''hidden'')" class="px-4 py-2 bg-blue-500 text-white rounded-2xl font-label-md hover:bg-blue-600 transition-colors shadow-sm flex items-center gap-2"><span class="material-symbols-outlined text-sm">add</span> ' + $sec.btn + '</button>
  </div>'
    $content = [regex]::Replace($content, $headerPattern, $newHeader)

    # 2. Delete inline form
    $formPattern = '(?s)<div class="bg-surface-container-lowest [^>]*>\s*<form onsubmit="' + [regex]::Escape($sec.form) + '".*?</form>\s*</div>'
    $content = [regex]::Replace($content, $formPattern, "")

    # 3. Add modal
    $modals += '
<!-- Add ' + $sec.btn + ' Modal -->
<div id="modal-add-' + $sec.modal + '" class="fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50">
    <div class="bg-surface p-6 rounded-3xl w-[500px] shadow-2xl animate-fade-in-up">
        <h3 class="text-xl font-bold mb-6 text-primary">' + $sec.btn + '</h3>
        <form onsubmit="' + $sec.form + '; closeModal(''modal-add-' + $sec.modal + ''');">
            ' + $sec.inputs + '
            <div class="col-span-full flex justify-end gap-3 mt-8 w-full">
                <button type="button" onclick="closeModal(''modal-add-' + $sec.modal + ''')" class="px-6 py-2.5 text-on-surface-variant bg-surface-container hover:bg-surface-container-high rounded-2xl transition-colors font-medium">Cancel</button>
                <button type="submit" class="px-6 py-2.5 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl shadow-sm hover:shadow-md transition-all font-medium">Save</button>
            </div>
        </form>
    </div>
</div>
'
}

$content = $content.Replace('</body>', $modals + '</body>')
Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Done Other Sections"
