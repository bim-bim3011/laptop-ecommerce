$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

$searchPattern = '(?s)<!-- Search on left as per JSON -->.*?</div>\s*</div>'
$content = [regex]::Replace($content, $searchPattern, "")

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Removed search bar!"
