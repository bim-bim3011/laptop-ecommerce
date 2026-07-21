import re
from bs4 import BeautifulSoup
import os

html_path = r"src/main/resources/templates/admin-dashboard.html"

with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# 1. Update Tailwind borderRadius
html = re.sub(
    r'"borderRadius":\s*\{[^}]*\}',
    r'"borderRadius": {\n                        "DEFAULT": "0.5rem",\n                        "lg": "0.75rem",\n                        "xl": "1rem",\n                        "2xl": "1.5rem",\n                        "full": "9999px"\n                    }',
    html
)

# 2. Update session.user to session.admin
html = html.replace('session.user', 'session.admin')

# 3. Update Edit/Delete buttons to be more transparent and beautiful
html = html.replace('text-blue-600 border border-blue-200 bg-transparent hover:bg-blue-50', 'text-blue-600 border-none bg-blue-500/10 hover:bg-blue-500/20')
html = html.replace('text-red-600 border border-red-200 bg-transparent hover:bg-red-50', 'text-red-600 border-none bg-red-500/10 hover:bg-red-500/20')
# Success/Completed badges
html = html.replace('bg-[#dcfce7] text-[#166534] text-xs font-label-md border border-[#bbf7d0]', 'bg-green-500/10 text-green-700 text-xs font-label-md border-none')
html = html.replace('bg-error-container text-on-error-container text-xs font-label-md border border-red-200', 'bg-red-500/10 text-red-700 text-xs font-label-md border-none')
html = html.replace('bg-secondary-fixed text-on-secondary-fixed text-xs font-label-md border border-blue-200', 'bg-blue-500/10 text-blue-700 text-xs font-label-md border-none')

soup = BeautifulSoup(html, 'html.parser')

# Inactive Tab Styling Fix: 
# Currently inactive tabs are: text-on-surface-variant hover:bg-surface-container-high hover:text-on-surface rounded-2xl
# In screenshot, they have black border. I need to make sure there's no border.
for a in soup.find_all('a', class_='nav-link'):
    classes = a.get('class', [])
    if 'border' in classes:
        classes.remove('border')
    if 'border-outline-variant' in classes:
        classes.remove('border-outline-variant')
    a['class'] = classes

# Processing Sections for Modals
sections_to_process = [
    ('section-brand', 'Add Brand', 'Brand'),
    ('section-category', 'Add Category', 'Category'),
    ('section-laptop', 'Add Laptop', 'Laptop'),
    ('section-laptop-config', 'Add Laptop Config', 'Config'),
    ('section-promotion', 'Add Promotion', 'Promotion'),
    ('section-gift-item', 'Add Gift Item', 'Gift Item'),
]

for section_id, btn_text, entity_cap in sections_to_process:
    section = soup.find(id=section_id)
    if not section:
        continue
    
    # Check for split layout grid
    grid = section.find('div', class_=re.compile(r'grid.*lg:grid-cols-3'))
    if not grid:
        grid = section.find('div', class_=re.compile(r'grid.*xl:grid-cols-3'))
        
    if grid:
        # Change to flex column
        classes = grid.get('class', [])
        classes = [c for c in classes if 'grid' not in c and 'col' not in c]
        classes.extend(['flex', 'flex-col'])
        grid['class'] = classes
        
        # Identify the form container (usually the one with <form>)
        form_div = None
        for div in grid.find_all('div', recursive=False):
            if div.find('form'):
                form_div = div
                break
        
        if form_div:
            # Extract form inner HTML
            form_el = form_div.find('form')
            form_inner_html = ""
            if form_el:
                # Add rounded-2xl to all inputs and selects
                for inp in form_el.find_all(['input', 'select']):
                    inp_class = inp.get('class', [])
                    inp_class = [c for c in inp_class if 'rounded' not in c]
                    inp_class.append('rounded-2xl')
                    inp_class.append('bg-surface-container-lowest')
                    if 'border-outline-variant' not in inp_class:
                        inp_class.append('border-outline-variant')
                    inp['class'] = inp_class
                
                # Replace the generic Add button in the form with our modal buttons
                btn = form_el.find('button', type='submit')
                if btn:
                    btn_div = btn.parent
                    btn_div.clear()
                    new_btns = BeautifulSoup(f"""
                        <div class="flex justify-end gap-3 mt-8">
                            <button type="button" onclick="closeModal('modal-add-{section_id}')" class="px-6 py-2.5 text-on-surface-variant bg-surface-container hover:bg-surface-container-high rounded-2xl transition-colors font-medium">Cancel</button>
                            <button type="submit" class="px-6 py-2.5 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl shadow-md hover:shadow-lg transition-all font-medium">{btn_text}</button>
                        </div>
                    """, 'html.parser')
                    btn_div.append(new_btns)

                form_inner_html = str(form_el)

            # Insert Modal at the end of body
            modal_html = f"""
            <div id="modal-add-{section_id}" class="fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50 transition-opacity">
                <div class="bg-surface p-6 rounded-3xl w-[500px] shadow-2xl">
                    <h3 class="text-xl font-bold mb-6 text-primary">{btn_text}</h3>
                    {form_inner_html}
                </div>
            </div>
            """
            soup.body.append(BeautifulSoup(modal_html, 'html.parser'))
            
            # Add top right button
            header_div = section.find('div', class_='mb-lg flex justify-between items-end')
            if header_div:
                btn_container = header_div.find('div', class_=re.compile(r'flex.*gap-3'))
                if not btn_container:
                    btn_container = soup.new_tag('div', attrs={'class': 'flex gap-3'})
                    header_div.append(btn_container)
                
                add_btn = soup.new_tag('button', attrs={
                    'class': 'px-4 py-2 bg-blue-500 text-white rounded-2xl font-label-md hover:bg-blue-600 transition-colors shadow-sm flex items-center gap-2',
                    'onclick': f"document.getElementById('modal-add-{section_id}').classList.remove('hidden')"
                })
                span = soup.new_tag('span', attrs={'class': 'material-symbols-outlined text-sm'})
                span.string = 'add'
                add_btn.append(span)
                add_btn.append(f' New {entity_cap}')
                btn_container.append(add_btn)

            # Remove the old inline form
            form_div.decompose()
            
            # Make table full width
            for div in grid.find_all('div', recursive=False):
                classes = div.get('class', [])
                classes = [c for c in classes if 'col-span' not in c]
                classes.append('w-full')
                div['class'] = classes

# Update the Edit modals to be beautifully rounded
for modal in soup.find_all('div', id=re.compile(r'^modal-edit-')):
    inner_div = modal.find('div', class_=re.compile(r'bg-surface'))
    if inner_div:
        classes = inner_div.get('class', [])
        classes = [c for c in classes if 'rounded' not in c]
        classes.append('rounded-3xl')
        inner_div['class'] = classes

# Update input corners in all modals
for form in soup.find_all('form'):
    for inp in form.find_all(['input', 'select']):
        inp_class = inp.get('class', [])
        if inp_class:
            inp_class = [c for c in inp_class if 'rounded' not in c]
            inp_class.append('rounded-2xl')
            inp['class'] = inp_class

with open(html_path, "w", encoding="utf-8") as f:
    f.write(str(soup))
