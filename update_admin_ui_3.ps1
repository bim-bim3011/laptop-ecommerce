$filePath = "d:\HSF PE\HSF Project\laptop-ecommerce\src\main\resources\templates\admin-dashboard.html"
$content = Get-Content -Path $filePath -Raw -Encoding UTF8

# 1. Update Tailwind config
$oldTailwind = '"borderRadius": {
                        "DEFAULT": "0.125rem",
                        "lg": "0.25rem",
                        "xl": "0.5rem",
                        "full": "0.75rem"
                    }'
$newTailwind = '"borderRadius": {
                        "DEFAULT": "0.5rem",
                        "lg": "0.75rem",
                        "xl": "1rem",
                        "2xl": "1.5rem",
                        "3xl": "2rem",
                        "full": "9999px"
                    }'
$content = $content.Replace($oldTailwind, $newTailwind)

# 2. Fix session.user to session.admin
$content = $content.Replace('session.user', 'session.admin')

# 3. Transparent badges and buttons
$content = $content.Replace('text-blue-600 border border-blue-200 bg-transparent hover:bg-blue-50', 'text-blue-600 border-none bg-blue-500/10 hover:bg-blue-500/20')
$content = $content.Replace('text-red-600 border border-red-200 bg-transparent hover:bg-red-50', 'text-red-600 border-none bg-red-500/10 hover:bg-red-500/20')

# For Success / Completed (dcfce7 / 166534)
$content = $content.Replace('bg-[#dcfce7] text-[#166534] text-xs font-label-md border border-[#bbf7d0]', 'bg-[#166534]/10 text-[#166534] text-xs font-label-md border-transparent')
$content = $content.Replace('bg-error-container text-on-error-container text-xs font-label-md border border-red-200', 'bg-red-500/10 text-red-700 text-xs font-label-md border-transparent')
$content = $content.Replace('bg-secondary-fixed text-on-secondary-fixed text-xs font-label-md border border-blue-200', 'bg-blue-500/10 text-blue-700 text-xs font-label-md border-transparent')

# 4. Remove active/inactive borders on sidebar links
$content = $content.Replace('class="nav-link flex items-center gap-3 px-4 py-3 text-on-surface-variant hover:bg-surface-container-high hover:text-on-surface rounded-2xl font-body-md text-body-md"', 'class="nav-link flex items-center gap-3 px-4 py-3 text-on-surface-variant hover:bg-surface-container-high hover:text-on-surface rounded-2xl font-body-md text-body-md border-none"')
$content = $content.Replace('class="nav-link flex items-center gap-3 px-4 py-3 bg-blue-500 text-white rounded-2xl font-body-md text-body-md shadow-sm active-nav"', 'class="nav-link flex items-center gap-3 px-4 py-3 bg-blue-500 text-white rounded-2xl font-body-md text-body-md shadow-sm active-nav border-none"')

# 5. Fix Layout for Brand, Category etc. - they were grid grid-cols-1 lg:grid-cols-3
$content = $content.Replace('grid grid-cols-1 xl:grid-cols-3 gap-gutter', 'flex flex-col gap-gutter')
$content = $content.Replace('grid grid-cols-1 lg:grid-cols-3 gap-gutter', 'flex flex-col gap-gutter')
$content = $content.Replace('xl:col-span-2', 'w-full')
$content = $content.Replace('lg:col-span-2', 'w-full')

Set-Content -Path $filePath -Value $content -Encoding UTF8
Write-Host "Updated Dashboard UI structure!"
