$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

$sections = @(
    @{ id="section-brand"; title="Brands"; btn="New Brand"; modal="brand"; form="createBrand(event)"; inputs='<input type="text" id="brandName" placeholder="Brand Name" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>' },
    @{ id="section-category"; title="Categories"; btn="New Category"; modal="category"; form="createCategory(event)"; inputs='<input type="text" id="categoryName" placeholder="Category Name" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>' }
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
Write-Host "Done Basic Sections"
