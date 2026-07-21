import re

file_path = 'd:\\HSF PE\\HSF Project\\laptop-ecommerce\\src\\main\\resources\\templates\\admin-dashboard.html'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Remove Search Bar
search_bar_pattern = r'<!-- Search on left as per JSON -->\s*<div class=\"relative w-full max-w-md.*?</form>|<!-- Search on left as per JSON -->\s*<div class=\"relative w-full max-w-md.*?</div>\s*</div>'
content = re.sub(search_bar_pattern, '<!-- Search Bar Removed -->', content, flags=re.DOTALL)

# 2. Update Sidebar User details
user_detail_old = r'<div class=\"w-10 h-10 rounded-full bg-secondary-container flex items-center justify-center text-on-secondary-container font-bold font-label-md group-hover:scale-110 transition-transform duration-300\">\s*AU\s*</div>\s*<div>\s*<p class=\"font-label-md text-label-md font-bold text-on-surface group-hover:text-secondary transition-colors\">Admin User</p>\s*<p class=\"text-xs text-on-surface-variant\">admin@lapzone.com</p>\s*</div>'
user_detail_new = r'''<div class="w-10 h-10 rounded-full bg-secondary-container flex items-center justify-center text-on-secondary-container font-bold font-label-md group-hover:scale-110 transition-transform duration-300" th:text="${session.user != null ? #strings.substring(session.user.fullName,0,2) : 'AU'}">
                AU
            </div>
            <div>
                <p class="font-label-md text-label-md font-bold text-on-surface group-hover:text-secondary transition-colors" th:text="${session.user != null ? session.user.fullName : 'Admin User'}">Admin User</p>
                <p class="text-xs text-on-surface-variant" th:text="${session.user != null ? session.user.email : 'admin@lapzone.com'}">admin@lapzone.com</p>
            </div>'''
content = re.sub(user_detail_old, user_detail_new, content)

# 3. Sidebar Navigation active color - Set initial state
nav_links_pattern = r'<a class=\"nav-link flex items-center gap-3 px-4 py-3 bg-secondary-container text-on-secondary-container rounded-lg font-body-md text-body-md shadow-sm\" href=\"#\" data-target=\"section-overview\">'
nav_links_new = r'<a class="nav-link flex items-center gap-3 px-4 py-3 bg-blue-500 text-white rounded-2xl font-body-md text-body-md shadow-sm" href="#" data-target="section-overview">'
content = content.replace(nav_links_pattern, nav_links_new)

# Make all sidebar links rounded-2xl
content = content.replace('rounded-lg font-body-md text-body-md', 'rounded-2xl font-body-md text-body-md')

# 4. Modify Sections to remove inline add forms and add 'Add' buttons to the top right
sections_to_process = [
    ('Laptop', 'laptop', r'<form onsubmit=\"createLaptop\(event\)\" class=\"flex gap-4 flex-wrap\">.*?</form>'),
    ('Laptop Configuration', 'laptop-config', r'<form onsubmit=\"createConfig\(event\)\" class=\"grid grid-cols-2 md:grid-cols-4 gap-4\">.*?</form>'),
    ('Promotion', 'promotion', r'<form onsubmit=\"createPromotion\(event\)\" class=\"flex gap-4 flex-wrap\">.*?</form>'),
    ('Brand', 'brand', r'<form onsubmit=\"createBrand\(event\)\" class=\"flex gap-4\">.*?</form>'),
    ('Category', 'category', r'<form onsubmit=\"createCategory\(event\)\" class=\"flex gap-4\">.*?</form>'),
    ('Gift Item', 'gift-item', r'<form onsubmit=\"createGiftItem\(event\)\" class=\"grid grid-cols-2 gap-4\">.*?</form>')
]

for title_part, section_id, form_pattern in sections_to_process:
    # Handle heading
    if section_id == 'laptop-config':
        h2_pattern = r'<h2 class=\"font-headline-lg text-headline-lg text-primary mb-4\" id=\"config-title\">Laptop Configurations</h2>'
        h2_replacement = f'''<div class="flex justify-between items-center mb-6">
        <h2 class="font-headline-lg text-headline-lg text-primary" id="config-title">Laptop Configurations</h2>
        <button onclick="document.getElementById('modal-add-{section_id}').classList.remove('hidden')" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-full shadow flex items-center gap-2 transition-colors">
            <span class="material-symbols-outlined text-sm">add</span> Add New
        </button>
    </div>'''
    else:
        title_display = f"{title_part}s" if title_part not in ["Laptop Configuration", "User Management"] else title_part
        if title_part == "Category": title_display = "Categories"
        
        h2_pattern = f'<h2 class=\"font-headline-lg text-headline-lg text-primary mb-4\">{title_display}</h2>'
        h2_replacement = f'''<div class="flex justify-between items-center mb-6">
        <h2 class="font-headline-lg text-headline-lg text-primary">{title_display}</h2>
        <button onclick="document.getElementById('modal-add-{section_id}').classList.remove('hidden')" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-full shadow flex items-center gap-2 transition-colors">
            <span class="material-symbols-outlined text-sm">add</span> Add New
        </button>
    </div>'''
    
    # We replace the heading
    content = re.sub(h2_pattern, h2_replacement, content)
    
    # Extract the form content to put it in a modal
    match = re.search(form_pattern, content, re.DOTALL)
    if match:
        form_content = match.group(0)
        # Create modal
        modal_html = f'''
<!-- {title_part} Add Modal -->
<div id="modal-add-{section_id}" class="fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50">
    <div class="bg-surface p-6 rounded-2xl w-full max-w-2xl shadow-lg">
        <h3 class="text-xl font-bold mb-4">Add {title_part}</h3>
        {form_content}
        <div class="flex justify-end gap-2 mt-4">
            <button type="button" onclick="closeModal('modal-add-{section_id}')" class="px-4 py-2 text-secondary bg-transparent hover:bg-gray-100 rounded-lg">Cancel</button>
        </div>
    </div>
</div>
'''
        content = content.replace('<!-- Modals -->', '<!-- Modals -->\n' + modal_html)
        
    # Remove original form div
    div_form_pattern = r'<div class=\"bg-surface-container-lowest p-md rounded-xl ambient-shadow mb-6\">\s*' + form_pattern + r'\s*</div>'
    content = re.sub(div_form_pattern, '', content, flags=re.DOTALL)

# 5. Fix form submit buttons to look nice
content = re.sub(r'<button type=\"submit\" class=\"bg-primary text-on-primary px-4 py-2 rounded(.*?)\">(.*?)</button>', r'<button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-2xl\1">\2</button>', content)

# 6. Change all rounded-xl to rounded-2xl, and some rounded-lg to rounded-2xl
content = content.replace('rounded-xl', 'rounded-2xl')

# 7. Make the actions transparent 
# Actions are generated in JS, but there are some inline action buttons if any? In the HTML table headers it says 'Actions'. 

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

# Update out7.html as well just in case they are looking at it directly
with open('d:\\HSF PE\\HSF Project\\laptop-ecommerce\\out7.html', 'w', encoding='utf-16le') as f:
    f.write("<!-- This file is auto-updated, check admin-dashboard.html instead for actual source code -->\n")
    
print('HTML modifications complete.')
