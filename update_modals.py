import re

file_path = r"src/main/resources/templates/admin-dashboard.html"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

sections = [
    {
        "id": "section-brand",
        "title": "Brands",
        "btn_text": "New Brand",
        "modal_id": "brand",
        "form_func": "createBrand(event)",
        "inputs": '<input type="text" id="brandName" placeholder="Brand Name" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>'
    },
    {
        "id": "section-category",
        "title": "Categories",
        "btn_text": "New Category",
        "modal_id": "category",
        "form_func": "createCategory(event)",
        "inputs": '<input type="text" id="categoryName" placeholder="Category Name" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>'
    },
    {
        "id": "section-laptop",
        "title": "Laptops",
        "btn_text": "New Laptop",
        "modal_id": "laptop",
        "form_func": "createLaptop(event)",
        "inputs": '''<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label class="block mb-1 text-sm font-medium">Laptop Name</label>
                    <input type="text" id="laptopName" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Brand</label>
                    <select id="laptopBrand" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required></select>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Category</label>
                    <select id="laptopCategory" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required></select>
                </div>
                <div class="md:col-span-2">
                    <label class="block mb-1 text-sm font-medium">Description</label>
                    <input type="text" id="laptopDesc" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full md:col-span-2">
                </div>
            </div>'''
    },
    {
        "id": "section-laptop-config",
        "title": "Laptop Configurations",
        "btn_text": "New Config",
        "modal_id": "laptop-config",
        "form_func": "createConfig(event)",
        "inputs": '''<div class="grid grid-cols-2 gap-4">
                <div class="col-span-2">
                    <label class="block mb-1 text-sm font-medium">Laptop</label>
                    <select id="confLaptop" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required></select>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">CPU</label>
                    <input type="text" id="confCpu" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">RAM</label>
                    <input type="text" id="confRam" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Storage</label>
                    <input type="text" id="confStorage" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">GPU</label>
                    <input type="text" id="confGpu" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full">
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Price</label>
                    <input type="number" step="0.01" id="confPrice" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Stock</label>
                    <input type="number" id="confStock" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
            </div>'''
    },
    {
        "id": "section-promotion",
        "title": "Promotions & Gifts",
        "btn_text": "New Promotion",
        "modal_id": "promotion",
        "form_func": "createPromotion(event)",
        "inputs": '''<div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block mb-1 text-sm font-medium">Coupon Code</label>
                    <input type="text" id="promoCode" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Title</label>
                    <input type="text" id="promoTitle" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Discount Type</label>
                    <select id="promoDiscount" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                        <option value="PERCENTAGE">PERCENTAGE</option>
                        <option value="FIXED_AMOUNT">FIXED AMOUNT</option>
                    </select>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Value</label>
                    <input type="number" step="0.01" id="promoValue" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
            </div>'''
    },
    {
        "id": "section-gift-item",
        "title": "Gift Items",
        "btn_text": "New Gift Item",
        "modal_id": "gift-item",
        "form_func": "createGiftItem(event)",
        "inputs": '''<div class="grid grid-cols-2 gap-4">
                <div class="col-span-2">
                    <label class="block mb-1 text-sm font-medium">Item Name</label>
                    <input type="text" id="giftItemName" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Price</label>
                    <input type="number" step="0.01" id="giftItemPrice" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full" required>
                </div>
                <div>
                    <label class="block mb-1 text-sm font-medium">Image URL</label>
                    <input type="text" id="giftItemImageUrl" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full">
                </div>
                <div class="col-span-2">
                    <label class="block mb-1 text-sm font-medium">Description</label>
                    <input type="text" id="giftItemDesc" class="border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full">
                </div>
            </div>'''
    }
]

modals_html = []

for sec in sections:
    # 1. Replace the header and delete the inline form
    # We look for <h2 class="...">{title}</h2> and the subsequent <div class="bg-surface-container-lowest ..."><form ...></div>
    
    # Header replacement
    header_pattern = r'<h2 class="font-headline-lg text-headline-lg text-primary mb-4">\s*' + re.escape(sec['title']) + r'\s*</h2>'
    new_header = f'''<div class="flex justify-between items-center mb-4">
      <h2 class="font-headline-lg text-headline-lg text-primary">{sec['title']}</h2>
      <button onclick="document.getElementById('modal-add-{sec['modal_id']}').classList.remove('hidden')" class="px-4 py-2 bg-blue-500 text-white rounded-2xl font-label-md hover:bg-blue-600 transition-colors shadow-sm flex items-center gap-2"><span class="material-symbols-outlined text-sm">add</span> {sec['btn_text']}</button>
  </div>'''
    content = re.sub(header_pattern, new_header, content, count=1)
    
    # Form deletion
    # We delete from <div class="bg-surface-container-lowest p-md rounded-xl ambient-shadow mb-6"> to the closing </div> of that div.
    form_div_pattern = r'<div class="bg-surface-container-lowest p-md rounded-xl ambient-shadow mb-6">\s*<form onsubmit="' + re.escape(sec['form_func']) + r'".*?</form>\s*</div>'
    content = re.sub(form_div_pattern, '', content, flags=re.DOTALL)
    
    # Build the modal HTML
    modal = f'''
<!-- Add {sec['btn_text']} Modal -->
<div id="modal-add-{sec['modal_id']}" class="fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50">
    <div class="bg-surface p-6 rounded-3xl w-[500px] shadow-2xl">
        <h3 class="text-xl font-bold mb-6 text-primary">{sec['btn_text']}</h3>
        <form onsubmit="{sec['form_func']}; closeModal('modal-add-{sec['modal_id']}');">
            {sec['inputs']}
            <div class="col-span-full flex justify-end gap-3 mt-8 w-full">
                <button type="button" onclick="closeModal('modal-add-{sec['modal_id']}')" class="px-6 py-2.5 text-on-surface-variant bg-surface-container hover:bg-surface-container-high rounded-2xl transition-colors font-medium">Cancel</button>
                <button type="submit" class="px-6 py-2.5 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl shadow-sm hover:shadow-md transition-all font-medium">Save</button>
            </div>
        </form>
    </div>
</div>
'''
    modals_html.append(modal)

# Insert modals before </body>
all_modals = '\n'.join(modals_html)
content = content.replace('</body>', all_modals + '\n</body>')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)
