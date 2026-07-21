$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

# 1. Remove Search Bar
$searchPattern = '<!-- Search on left as per JSON -->\s*<div class="relative w-full max-w-md[^>]*>[\s\S]*?</div>'
$content = [regex]::Replace($content, $searchPattern, '<!-- Search Bar Removed -->')

# 2. Update User Details
$userOld = '<div class="w-10 h-10 rounded-full bg-secondary-container flex items-center justify-center text-on-secondary-container font-bold font-label-md group-hover:scale-110 transition-transform duration-300">\s*AU\s*</div>\s*<div>\s*<p class="font-label-md text-label-md font-bold text-on-surface group-hover:text-secondary transition-colors">Admin User</p>\s*<p class="text-xs text-on-surface-variant">admin@lapzone.com</p>\s*</div>'
$userNew = '<div class="w-10 h-10 rounded-full bg-secondary-container flex items-center justify-center text-on-secondary-container font-bold font-label-md group-hover:scale-110 transition-transform duration-300" th:text="${session.user != null ? #strings.substring(session.user.fullName,0,2) : ''AU''}">AU</div>
<div>
<p class="font-label-md text-label-md font-bold text-on-surface group-hover:text-secondary transition-colors" th:text="${session.user != null ? session.user.fullName : ''Admin User''}">Admin User</p>
<p class="text-xs text-on-surface-variant" th:text="${session.user != null ? session.user.email : ''admin@lapzone.com''}">admin@lapzone.com</p>
</div>'
$content = [regex]::Replace($content, $userOld, $userNew)

# 3. Nav link
$navOld = '<a class="nav-link flex items-center gap-3 px-4 py-3 bg-secondary-container text-on-secondary-container rounded-lg font-body-md text-body-md shadow-sm"'
$navNew = '<a class="nav-link flex items-center gap-3 px-4 py-3 bg-blue-500 text-white rounded-2xl font-body-md text-body-md shadow-sm active-nav"'
$content = $content.Replace($navOld, $navNew)
$content = $content.Replace('rounded-lg font-body-md text-body-md', 'rounded-2xl font-body-md text-body-md')

# 4. Modals and Buttons
$sections = @(
    @{Title="Laptop"; Id="laptop"; Pattern='<form onsubmit="createLaptop\(event\)" class="flex gap-4 flex-wrap">[\s\S]*?</form>'},
    @{Title="Laptop Configuration"; Id="laptop-config"; Pattern='<form onsubmit="createConfig\(event\)" class="grid grid-cols-2 md:grid-cols-4 gap-4">[\s\S]*?</form>'},
    @{Title="Promotion"; Id="promotion"; Pattern='<form onsubmit="createPromotion\(event\)" class="flex gap-4 flex-wrap">[\s\S]*?</form>'},
    @{Title="Brand"; Id="brand"; Pattern='<form onsubmit="createBrand\(event\)" class="flex gap-4">[\s\S]*?</form>'},
    @{Title="Category"; Id="category"; Pattern='<form onsubmit="createCategory\(event\)" class="flex gap-4">[\s\S]*?</form>'},
    @{Title="Gift Item"; Id="gift-item"; Pattern='<form onsubmit="createGiftItem\(event\)" class="grid grid-cols-2 gap-4">[\s\S]*?</form>'}
)

foreach($sec in $sections) {
    $titlePart = $sec.Title
    $secId = $sec.Id
    $pattern = $sec.Pattern

    $titleDisplay = $titlePart
    if ($titlePart -eq "Category") { $titleDisplay = "Categories" }
    elseif ($titlePart -notin @("Laptop Configuration", "User Management")) { $titleDisplay = $titlePart + "s" }

    if ($secId -eq "laptop-config") {
        $h2Old = '<h2 class="font-headline-lg text-headline-lg text-primary mb-4" id="config-title">Laptop Configurations</h2>'
    } else {
        $h2Old = '<h2 class="font-headline-lg text-headline-lg text-primary mb-4">' + $titleDisplay + '</h2>'
    }

    $h2New = '<div class="flex justify-between items-center mb-6">
<h2 class="font-headline-lg text-headline-lg text-primary">' + $titleDisplay + '</h2>
<button onclick="document.getElementById(''modal-add-' + $secId + ''').classList.remove(''hidden'')" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-full shadow flex items-center gap-2 transition-colors">
<span class="material-symbols-outlined text-sm">add</span> Add New
</button>
</div>'
    if ($secId -eq "laptop-config") {
        $h2New = $h2New.Replace('">' + $titleDisplay + '</h2>', '" id="config-title">Laptop Configurations</h2>')
    }

    $content = $content.Replace($h2Old, $h2New)

    $match = [regex]::Match($content, $pattern)
    if ($match.Success) {
        $formContent = $match.Value
        
        $modalHtml = "
<!-- Add Modal -->
<div id=""modal-add-$secId"" class=""fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50"">
    <div class=""bg-surface p-6 rounded-2xl w-full max-w-2xl shadow-lg"">
        <h3 class=""text-xl font-bold mb-4"">Add $titlePart</h3>
        $formContent
        <div class=""flex justify-end gap-2 mt-4"">
            <button type=""button"" onclick=""closeModal('modal-add-$secId')"" class=""px-4 py-2 text-secondary bg-transparent hover:bg-gray-100 rounded-lg"">Cancel</button>
        </div>
    </div>
</div>
"
        $content = $content.Replace('<!-- Modals -->', "<!-- Modals -->`n" + $modalHtml)
        
        $divPattern = '<div class="bg-surface-container-lowest p-md rounded-xl ambient-shadow mb-6">\s*' + $pattern + '\s*</div>'
        $content = [regex]::Replace($content, $divPattern, '')
    }
}

# 5. Fix submit buttons in add modals
$content = [regex]::Replace($content, '<button type="submit" class="bg-primary text-on-primary px-4 py-2 rounded(.*?)">(.*?)</button>', '<button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-2xl$1">$2</button>')

# 6. Change rounded-xl to rounded-2xl
$content = $content.Replace('rounded-xl', 'rounded-2xl')

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated UI!"
