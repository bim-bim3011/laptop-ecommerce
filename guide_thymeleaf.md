# Hướng Dẫn Sử Dụng Thymeleaf & Luồng Dữ Liệu Biến (Variables Flow)

Tài liệu này giải thích cách dữ liệu (biến) chảy từ Controller xuống View (HTML) và các cú pháp Thymeleaf cốt lõi được sử dụng trong 3 trang: `home-page`, `cart`, và `laptop-detail`.

---

## 1. Biến Toàn Cục (Global Variables)
Có những biến xuất hiện ở phần Header/Nav của **tất cả các trang** mà không cần Controller riêng lẻ nào phải truyền vào:
- `session.user`: Được lưu trữ trong `HttpSession` lúc user đăng nhập thành công. Thymeleaf truy cập bằng `${session.user}`. Dùng để ẩn/hiện nút Đăng nhập/Đăng xuất (`th:if="${session.user != null}"`).
- `cartItemCount`: Số lượng tổng các item trong giỏ hàng. Được đẩy vào mọi giao diện nhờ `@ModelAttribute("cartItemCount")` trong file `GlobalControllerAdvice.java`. Dùng để hiển thị số trên icon giỏ hàng: `th:text="'Giỏ hàng (' + ${cartItemCount} + ')'"`.

---

## 2. Trang Chủ (home-page.html)

**Luồng biến:** 
`HomeController` xử lý truy vấn DB và đẩy các biến sau vào đối tượng `Model`:
- `categories`: Danh sách các hãng (Category).
- `laptops`: Danh sách laptop (chịu ảnh hưởng bởi bộ lọc keyword, categoryId).
- `keyword` / `selectedCategoryId`: Lưu trạng thái bộ lọc đang được kích hoạt.

**Cú pháp Thymeleaf nổi bật:**
1. **`th:each` (Vòng lặp):** 
   - Duyệt qua danh sách mảng để in ra các thẻ HTML lặp lại.
   - Ví dụ: `th:each="cat : ${categories}"` lặp ra danh sách các hãng bên thanh sidebar.
2. **`th:text` (Render text an toàn):** 
   - Chèn văn bản vào thẻ.
   - Ví dụ: `th:text="${cat.categoryName}"`.
3. **`th:href` và `th:action` (Đường dẫn động):**
   - Tạo URL an toàn, hỗ trợ nối tham số (query params) dễ dàng.
   - Ví dụ: `th:href="@{/home-page(categoryId=${cat.categoryId})}"` sẽ render ra URL `/home-page?categoryId=1`.
4. **`th:class` (Class động có điều kiện):**
   - Viết biểu thức toán tử 3 ngôi (Ternary) để đổi màu UI.
   - Ví dụ: `th:class="${selectedCategoryId == cat.categoryId ? 'text-blue-500' : 'text-gray-900'}"`. Trùng ID thì bôi xanh, không thì màu xám.
5. **`th:if` / `th:unless` (Render có điều kiện):**
   - `th:if`: Thẻ chỉ tồn tại trên DOM nếu điều kiện ĐÚNG. 
   - `th:unless`: Thẻ chỉ tồn tại nếu điều kiện SAI (tương đương else).
   - Ví dụ: Hiển thị giao diện "Không tìm thấy" dùng `th:if="${#lists.isEmpty(laptops)}"`.

---

## 3. Trang Chi Tiết Sản Phẩm (laptop-detail.html)

**Luồng biến:**
`LaptopController` đẩy vào Model:
- `laptop`: Chứa mọi thông tin chi tiết của Laptop (cấu hình, mô tả).
- `similarLaptops`: Danh sách 4 laptop cùng danh mục để gợi ý.
- `giftDetails`: Các phần quà tặng kèm theo cấu hình laptop.

**Cú pháp Thymeleaf nổi bật:**
1. **`th:utext` (Unescaped Text - Text không bị escape):**
   - Rất quan trọng khi hiển thị `laptop.description`. 
   - Thông thường `th:text` sẽ biến thẻ `<p>` thành chuỗi văn bản `<p>` thô. Còn `th:utext` sẽ parse nó thành thẻ HTML thật. Giúp bài viết mô tả có thể xuống dòng, in đậm, chèn ảnh được.
2. **`th:block` (Thẻ tàng hình):**
   - Dùng khi bạn cần tạo một câu lệnh `th:if` hoặc `th:each` nhóm nhiều thẻ con, nhưng lại **không muốn** sinh ra thêm một thẻ `<div>` bọc ngoài gây hỏng bố cục CSS.
   - Ví dụ ở đường dẫn Breadcrumb: 
     ```html
     <th:block th:if="${laptop.category != null}">
         <a ...>Danh mục</a> &gt; 
     </th:block>
     ```
3. **Xử lý Utility Objects (`#lists`, `#numbers`):**
   - `#lists.isEmpty(laptop.configurationVersions)`: Chặn lỗi văng NullPointerException trước khi lấy ra phần tử thứ `[0]`.
   - `#numbers.formatDecimal(price, 0, 'COMMA', 0, 'POINT')`: Format số tiền chuẩn Việt Nam (vd: 38.990.000).

---

## 4. Trang Giỏ Hàng (cart.html)

**Luồng biến:**
`CartController` lấy giỏ hàng của User và đẩy vào Model:
- `cart`: Đối tượng chứa thông tin tổng thể.
- `successMessage` / `errorMessage`: Biến Flash attribute thông báo kết quả (màu xanh/màu đỏ).

**Cú pháp Thymeleaf nổi bật:**
1. **Gọi thuộc tính đối tượng lồng nhau sâu (Deep nested properties):**
   - Khi có các quan hệ trong Hibernate/JPA, Thymeleaf có thể móc sâu vào object.
   - Ví dụ lấy tên hãng của laptop trong 1 dòng cart: `${item.configurationVersion.laptop.brand.brandName}`.
2. **`th:data-*` (Nhúng dữ liệu cho Javascript):**
   - Rất hữu dụng khi cần truyền data cho script dưới font-end.
   - Ví dụ: `th:data-item-id="${item.cartItemId}"` và `th:data-price="${item.configurationVersion.price * item.quantity}"`. Lúc Javascript tính lại tổng tiền sẽ móc vào các `data-price` này.
3. **`th:value` và `th:disabled` (Form động):**
   - `th:value="${item.cartItemId}"` cho input type hidden để gửi lên Server.
   - `th:disabled="${item.quantity <= 1}"`: Vô hiệu hoá nút trừ `-` nếu số lượng đang là 1 để chặn người dùng giảm xuống 0. Tương tự, chặn nút `+` nếu vượt quá `stockQuantity` (tồn kho).
