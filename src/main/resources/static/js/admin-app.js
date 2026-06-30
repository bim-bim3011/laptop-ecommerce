document.addEventListener('DOMContentLoaded', () => {
    // Navigation
    const links = document.querySelectorAll('.nav-link');
    const sections = document.querySelectorAll('.admin-section');
    
    links.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const target = link.getAttribute('data-target');
            if(target) {
                sections.forEach(s => s.classList.add('hidden'));
                document.getElementById(target).classList.remove('hidden');
                loadData(target);
            }
        });
    });
    
    function loadData(section) {
        if(section === 'section-brand') loadBrands();
        if(section === 'section-category') loadCategories();
        if(section === 'section-user') loadUsers();
        if(section === 'section-laptop') loadLaptops();
        if(section === 'section-promotion') loadPromotions();
    }
    
    // --- Brand ---
    function loadBrands() {
        fetch('/admin/api/brands')
            .then(res => res.json())
            .then(data => {
                const tbody = document.getElementById('tbody-brand');
                const brandSelects = [document.getElementById('laptopBrand'), document.getElementById('editLaptopBrand')];
                tbody.innerHTML = '';
                brandSelects.forEach(s => s.innerHTML = '<option value="">Select Brand</option>');

                data.forEach(brand => {
                    tbody.innerHTML += `
                        <tr class="border-b border-outline-variant hover:bg-surface-container">
                            <td class="p-4">${brand.brandId}</td>
                            <td class="p-4">${brand.brandName}</td>
                            <td class="p-4">
                                <button onclick="editBrand(${brand.brandId}, '${brand.brandName}')" class="text-secondary mr-2">Edit</button>
                                <button onclick="deleteBrand(${brand.brandId})" class="text-error">Delete</button>
                            </td>
                        </tr>
                    `;
                    brandSelects.forEach(s => s.innerHTML += `<option value="${brand.brandId}">${brand.brandName}</option>`);
                });
            });
    }
    
    window.createBrand = function(event) {
        event.preventDefault();
        const brandName = document.getElementById('brandName').value;
        fetch('/admin/api/brands', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({brandName})
        }).then(() => {
            document.getElementById('brandName').value = '';
            loadBrands();
        });
    };
    
    window.deleteBrand = function(id) {
        if(confirm('Delete brand?')) {
            fetch('/admin/api/brands/' + id, {method: 'DELETE'}).then(() => loadBrands());
        }
    };
    
    window.closeModal = function(modalId) {
        document.getElementById(modalId).classList.add('hidden');
    };

    window.editBrand = function(id, oldName) {
        document.getElementById('editBrandId').value = id;
        document.getElementById('editBrandName').value = oldName;
        document.getElementById('modal-edit-brand').classList.remove('hidden');
    };
    
    window.submitEditBrand = function(event) {
        event.preventDefault();
        const id = document.getElementById('editBrandId').value;
        const newName = document.getElementById('editBrandName').value;
        fetch('/admin/api/brands/' + id, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({brandName: newName})
        }).then(() => {
            closeModal('modal-edit-brand');
            loadBrands();
        });
    };
    
    // --- Category ---
    function loadCategories() {
        fetch('/admin/api/categories')
            .then(res => res.json())
            .then(data => {
                const tbody = document.getElementById('tbody-category');
                const catSelects = [document.getElementById('laptopCategory'), document.getElementById('editLaptopCategory')];
                tbody.innerHTML = '';
                catSelects.forEach(s => s.innerHTML = '<option value="">Select Category</option>');

                data.forEach(cat => {
                    tbody.innerHTML += `
                        <tr class="border-b border-outline-variant hover:bg-surface-container">
                            <td class="p-4">${cat.categoryId}</td>
                            <td class="p-4">${cat.categoryName}</td>
                            <td class="p-4">
                                <button onclick="editCategory(${cat.categoryId}, '${cat.categoryName}')" class="text-secondary mr-2">Edit</button>
                                <button onclick="deleteCategory(${cat.categoryId})" class="text-error">Delete</button>
                            </td>
                        </tr>
                    `;
                    catSelects.forEach(s => s.innerHTML += `<option value="${cat.categoryId}">${cat.categoryName}</option>`);
                });
            });
    }
    
    window.createCategory = function(event) {
        event.preventDefault();
        const categoryName = document.getElementById('categoryName').value;
        fetch('/admin/api/categories', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({categoryName})
        }).then(() => {
            document.getElementById('categoryName').value = '';
            loadCategories();
        });
    };
    
    window.deleteCategory = function(id) {
        if(confirm('Delete category?')) {
            fetch('/admin/api/categories/' + id, {method: 'DELETE'}).then(() => loadCategories());
        }
    };

    window.editCategory = function(id, oldName) {
        document.getElementById('editCategoryId').value = id;
        document.getElementById('editCategoryName').value = oldName;
        document.getElementById('modal-edit-category').classList.remove('hidden');
    };

    window.submitEditCategory = function(event) {
        event.preventDefault();
        const id = document.getElementById('editCategoryId').value;
        const newName = document.getElementById('editCategoryName').value;
        fetch('/admin/api/categories/' + id, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({categoryName: newName})
        }).then(() => {
            closeModal('modal-edit-category');
            loadCategories();
        });
    };

    // --- User ---
    function loadUsers() {
        fetch('/admin/api/users')
            .then(res => res.json())
            .then(data => {
                const tbody = document.getElementById('tbody-user');
                tbody.innerHTML = '';
                data.forEach(user => {
                    tbody.innerHTML += `
                        <tr class="border-b border-outline-variant hover:bg-surface-container">
                            <td class="p-4">${user.userId}</td>
                            <td class="p-4">${user.email}</td>
                            <td class="p-4">${user.fullName}</td>
                            <td class="p-4">${user.status}</td>
                            <td class="p-4">
                                <button onclick="deleteUser(${user.userId})" class="text-error">Delete</button>
                            </td>
                        </tr>
                    `;
                });
            });
    }

    window.deleteUser = function(id) {
        if(confirm('Delete user?')) {
            fetch('/admin/api/users/' + id, {method: 'DELETE'}).then(() => loadUsers());
        }
    };

    // --- Laptop ---
    let currentLaptops = [];
    function loadLaptops() {
        fetch('/admin/api/laptops')
            .then(res => res.json())
            .then(data => {
                currentLaptops = data;
                const tbody = document.getElementById('tbody-laptop');
                tbody.innerHTML = '';
                data.forEach(laptop => {
                    const brandName = laptop.brand ? laptop.brand.brandName : 'N/A';
                    const catName = laptop.category ? laptop.category.categoryName : 'N/A';
                    tbody.innerHTML += `
                        <tr class="border-b border-outline-variant hover:bg-surface-container">
                            <td class="p-4">${laptop.laptopId}</td>
                            <td class="p-4">${laptop.laptopName}</td>
                            <td class="p-4">${laptop.description || ''}</td>
                            <td class="p-4">${brandName}</td>
                            <td class="p-4">${catName}</td>
                            <td class="p-4">
                                <button onclick="editLaptop(${laptop.laptopId})" class="text-secondary mr-2">Edit</button>
                                <button onclick="deleteLaptop(${laptop.laptopId})" class="text-error">Delete</button>
                            </td>
                        </tr>
                    `;
                });
                
                // Populate laptop dropdown for configurations
                const confLaptopSelect = document.getElementById('confLaptop');
                if (confLaptopSelect) {
                    confLaptopSelect.innerHTML = '<option value="">Select Laptop</option>';
                    data.forEach(laptop => {
                        confLaptopSelect.innerHTML += `<option value="${laptop.laptopId}">${laptop.laptopName}</option>`;
                    });
                }
            });
    }

    window.createLaptop = function(event) {
        event.preventDefault();
        const name = document.getElementById('laptopName').value;
        const desc = document.getElementById('laptopDesc').value;
        const brandId = document.getElementById('laptopBrand').value;
        const categoryId = document.getElementById('laptopCategory').value;
        fetch('/admin/api/laptops', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                laptopName: name, 
                description: desc,
                brand: {brandId: brandId},
                category: {categoryId: categoryId}
            })
        }).then(() => {
            document.getElementById('laptopName').value = '';
            document.getElementById('laptopDesc').value = '';
            document.getElementById('laptopBrand').value = '';
            document.getElementById('laptopCategory').value = '';
            loadLaptops();
        });
    };

    window.editLaptop = function(id) {
        const laptop = currentLaptops.find(l => l.laptopId === id);
        if(!laptop) return;
        document.getElementById('editLaptopId').value = laptop.laptopId;
        document.getElementById('editLaptopName').value = laptop.laptopName;
        document.getElementById('editLaptopDesc').value = laptop.description || '';
        document.getElementById('editLaptopBrand').value = laptop.brand ? laptop.brand.brandId : '';
        document.getElementById('editLaptopCategory').value = laptop.category ? laptop.category.categoryId : '';
        document.getElementById('modal-edit-laptop').classList.remove('hidden');
    };

    window.submitEditLaptop = function(event) {
        event.preventDefault();
        const id = document.getElementById('editLaptopId').value;
        const name = document.getElementById('editLaptopName').value;
        const desc = document.getElementById('editLaptopDesc').value;
        const brandId = document.getElementById('editLaptopBrand').value;
        const categoryId = document.getElementById('editLaptopCategory').value;

        fetch(`/admin/api/laptops/${id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                laptopName: name, 
                description: desc,
                brand: {brandId: brandId},
                category: {categoryId: categoryId}
            })
        }).then(() => {
            closeModal('modal-edit-laptop');
            loadLaptops();
        });
    };
    
    window.deleteLaptop = function(id) {
        if(confirm('Delete laptop?')) {
            fetch('/admin/api/laptops/' + id, {method: 'DELETE'}).then(() => loadLaptops());
        }
    };

    // --- Laptop Configurations ---
    let currentConfigs = [];

    function loadConfigs() {
        // Fetch laptops for dropdown if not already loaded
        fetch('/admin/api/laptops')
            .then(res => res.json())
            .then(data => {
                currentLaptops = data;
                const confLaptopSelect = document.getElementById('confLaptop');
                if (confLaptopSelect) {
                    confLaptopSelect.innerHTML = '<option value="">Select Laptop</option>';
                    data.forEach(laptop => {
                        confLaptopSelect.innerHTML += `<option value="${laptop.laptopId}">${laptop.laptopName}</option>`;
                    });
                }
            });

        fetch('/admin/api/laptops/configurations')
            .then(res => res.json())
            .then(data => {
                currentConfigs = data;
                const tbody = document.getElementById('tbody-config');
                tbody.innerHTML = '';
                data.forEach(conf => {
                    const laptopName = conf.laptop ? conf.laptop.laptopName : 'N/A';
                    tbody.innerHTML += `
                        <tr class="border-b border-outline-variant hover:bg-surface-container">
                            <td class="p-4">${conf.configurationId}</td>
                            <td class="p-4">${laptopName}</td>
                            <td class="p-4">${conf.cpu}</td>
                            <td class="p-4">${conf.ram} / ${conf.storage} / ${conf.gpu || 'N/A'}</td>
                            <td class="p-4">${conf.price}</td>
                            <td class="p-4">${conf.stockQuantity}</td>
                            <td class="p-4">
                                <button onclick="editConfig(${conf.configurationId})" class="text-secondary mr-2">Edit</button>
                                <button onclick="deleteConfig(${conf.configurationId})" class="text-error">Delete</button>
                            </td>
                        </tr>
                    `;
                });
            });
    }

    window.createConfig = function(event) {
        event.preventDefault();
        const laptopId = document.getElementById('confLaptop').value;
        const cpu = document.getElementById('confCpu').value;
        const ram = document.getElementById('confRam').value;
        const storage = document.getElementById('confStorage').value;
        const gpu = document.getElementById('confGpu').value;
        const price = document.getElementById('confPrice').value;
        const stockQuantity = document.getElementById('confStock').value;
        
        fetch(`/admin/api/laptops/${laptopId}/configurations`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({cpu, ram, storage, gpu, price, stockQuantity})
        }).then(() => {
            document.getElementById('confCpu').value = '';
            document.getElementById('confRam').value = '';
            document.getElementById('confStorage').value = '';
            document.getElementById('confGpu').value = '';
            document.getElementById('confPrice').value = '';
            document.getElementById('confStock').value = '';
            loadConfigs();
        });
    };

    window.deleteConfig = function(id) {
        if(confirm('Delete configuration?')) {
            // Note: Since deleteConfiguration is mapped at /admin/api/laptops/configurations/{configId}
            fetch(`/admin/api/laptops/configurations/${id}`, {method: 'DELETE'}).then(() => loadConfigs());
        }
    };

    window.editConfig = function(id) {
        const conf = currentConfigs.find(c => c.configurationId === id);
        if(!conf) return;
        document.getElementById('editConfigId').value = conf.configurationId;
        document.getElementById('editConfCpu').value = conf.cpu;
        document.getElementById('editConfRam').value = conf.ram;
        document.getElementById('editConfStorage').value = conf.storage;
        document.getElementById('editConfGpu').value = conf.gpu || '';
        document.getElementById('editConfPrice').value = conf.price;
        document.getElementById('editConfStock').value = conf.stockQuantity;
        
        document.getElementById('modal-edit-config').classList.remove('hidden');
    };

    window.submitEditConfig = function(event) {
        event.preventDefault();
        const id = document.getElementById('editConfigId').value;
        const cpu = document.getElementById('editConfCpu').value;
        const ram = document.getElementById('editConfRam').value;
        const storage = document.getElementById('editConfStorage').value;
        const gpu = document.getElementById('editConfGpu').value;
        const price = document.getElementById('editConfPrice').value;
        const stockQuantity = document.getElementById('editConfStock').value;

        fetch(`/admin/api/laptops/configurations/${id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({cpu, ram, storage, gpu, price, stockQuantity})
        }).then(() => {
            closeModal('modal-edit-config');
            loadConfigs();
        });
    };

    // --- Promotion ---
    function loadPromotions() {
        fetch('/admin/api/promotions')
            .then(res => res.json())
            .then(data => {
                const tbody = document.getElementById('tbody-promotion');
                tbody.innerHTML = '';
                data.forEach(promo => {
                    tbody.innerHTML += `
                        <tr class="border-b border-outline-variant hover:bg-surface-container">
                            <td class="p-4">${promo.promotionId}</td>
                            <td class="p-4">${promo.couponCode}</td>
                            <td class="p-4">${promo.title}</td>
                            <td class="p-4">${promo.discountValue}</td>
                            <td class="p-4">
                                <button onclick="deletePromotion(${promo.promotionId})" class="text-error">Delete</button>
                            </td>
                        </tr>
                    `;
                });
            });
    }
    
    window.createPromotion = function(event) {
        event.preventDefault();
        const couponCode = document.getElementById('promoCode').value;
        const title = document.getElementById('promoTitle').value;
        const discountType = document.getElementById('promoDiscount').value;
        const discountValue = document.getElementById('promoValue').value;
        
        fetch('/admin/api/promotions', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                couponCode, title, discountType, discountValue,
                startDate: new Date().toISOString(),
                endDate: new Date(new Date().getTime() + 7*24*60*60*1000).toISOString()
            })
        }).then(() => {
            document.getElementById('promoCode').value = '';
            document.getElementById('promoTitle').value = '';
            document.getElementById('promoDiscount').value = '';
            document.getElementById('promoValue').value = '';
            loadPromotions();
        });
    };
    
    window.deletePromotion = function(id) {
        if(confirm('Delete promotion?')) {
            fetch('/admin/api/promotions/' + id, {method: 'DELETE'}).then(() => loadPromotions());
        }
    };
});
