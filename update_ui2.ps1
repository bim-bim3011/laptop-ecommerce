$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

# 1. Update Tables to be table-fixed
$content = $content.Replace('<table class="w-full text-left">', '<table class="w-full text-left table-fixed">')

# 2. Add Modals button layout fix
$addPattern = '(?s)<button type="submit"[^>]*>(.*?)</button>\s*</form>\s*<div class="flex justify-end gap-2 mt-4">\s*<button type="button" onclick="([^"]+)"[^>]*>Cancel</button>\s*</div>'
$addNew = '<div class="col-span-full flex justify-end gap-3 mt-6 w-full">
    <button type="button" onclick="$2" class="px-6 py-2.5 text-on-surface-variant bg-surface-container hover:bg-surface-container-high rounded-2xl transition-colors font-medium">Cancel</button>
    <button type="submit" class="px-6 py-2.5 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl shadow-sm transition-colors font-medium">$1</button>
</div>
</form>'
$content = [regex]::Replace($content, $addPattern, $addNew)

# 3. Edit Modals button layout fix
$editBtnGrp = '(?s)<div class="flex justify-end gap-2[^"]*">\s*<button type="button" onclick="([^"]+)"[^>]*>Cancel</button>\s*<button type="submit"[^>]*>(.*?)</button>\s*</div>'
$editBtnGrpNew = '<div class="col-span-full flex justify-end gap-3 mt-6 w-full">
    <button type="button" onclick="$1" class="px-6 py-2.5 text-on-surface-variant bg-surface-container hover:bg-surface-container-high rounded-2xl transition-colors font-medium">Cancel</button>
    <button type="submit" class="px-6 py-2.5 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl shadow-sm transition-colors font-medium">$2</button>
</div>'
$content = [regex]::Replace($content, $editBtnGrp, $editBtnGrpNew)

# 4. Form Layouts
$content = $content.Replace('class="flex gap-4"', 'class="grid grid-cols-1 gap-4 mt-4"')
$content = $content.Replace('class="flex gap-4 flex-wrap"', 'class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4"')

# 5. Inputs and Selects aesthetics
$inputPattern = 'class="border p-2 rounded"'
$inputNew = 'class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full"'
$content = $content.Replace($inputPattern, $inputNew)

$inputPattern2 = 'class="border w-full p-2 rounded mb-4"'
$inputNew2 = 'class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full mb-4"'
$content = $content.Replace($inputPattern2, $inputNew2)

$inputPattern3 = 'class="border w-full p-2 rounded"'
$inputNew3 = 'class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full"'
$content = $content.Replace($inputPattern3, $inputNew3)

# 6. Specific case for Laptop Add modal col-span
# In Add Laptop, some fields might need to span full
$content = $content.Replace('<input type="text" id="laptopDesc" placeholder="Description"', '<input type="text" id="laptopDesc" placeholder="Description" class="md:col-span-2"')

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated UI 2!"
