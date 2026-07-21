import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

public class UpdateUI {
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("src/main/resources/templates/admin-dashboard.html");
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        // Define sections
        String[][] sections = {
            {"section-brand", "Brands", "New Brand", "brand", "createBrand(event)", "<input type=\"text\" id=\"brandName\" placeholder=\"Brand Name\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>"},
            {"section-category", "Categories", "New Category", "category", "createCategory(event)", "<input type=\"text\" id=\"categoryName\" placeholder=\"Category Name\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>"},
            {"section-laptop", "Laptops", "New Laptop", "laptop", "createLaptop(event)", "<div class=\"grid grid-cols-1 md:grid-cols-2 gap-4\">\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Laptop Name</label>\n                    <input type=\"text\" id=\"laptopName\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Brand</label>\n                    <select id=\"laptopBrand\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required></select>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Category</label>\n                    <select id=\"laptopCategory\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required></select>\n                </div>\n                <div class=\"md:col-span-2\">\n                    <label class=\"block mb-1 text-sm font-medium\">Description</label>\n                    <input type=\"text\" id=\"laptopDesc\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full md:col-span-2\">\n                </div>\n            </div>"},
            {"section-laptop-config", "Laptop Configurations", "New Config", "laptop-config", "createConfig(event)", "<div class=\"grid grid-cols-2 gap-4\">\n                <div class=\"col-span-2\">\n                    <label class=\"block mb-1 text-sm font-medium\">Laptop</label>\n                    <select id=\"confLaptop\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required></select>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">CPU</label>\n                    <input type=\"text\" id=\"confCpu\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">RAM</label>\n                    <input type=\"text\" id=\"confRam\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Storage</label>\n                    <input type=\"text\" id=\"confStorage\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">GPU</label>\n                    <input type=\"text\" id=\"confGpu\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\">\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Price</label>\n                    <input type=\"number\" step=\"0.01\" id=\"confPrice\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Stock</label>\n                    <input type=\"number\" id=\"confStock\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n            </div>"},
            {"section-promotion", "Promotions & Gifts", "New Promotion", "promotion", "createPromotion(event)", "<div class=\"grid grid-cols-2 gap-4\">\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Coupon Code</label>\n                    <input type=\"text\" id=\"promoCode\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Title</label>\n                    <input type=\"text\" id=\"promoTitle\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Discount Type</label>\n                    <select id=\"promoDiscount\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                        <option value=\"PERCENTAGE\">PERCENTAGE</option>\n                        <option value=\"FIXED_AMOUNT\">FIXED AMOUNT</option>\n                    </select>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Value</label>\n                    <input type=\"number\" step=\"0.01\" id=\"promoValue\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n            </div>"},
            {"section-gift-item", "Gift Items", "New Gift Item", "gift-item", "createGiftItem(event)", "<div class=\"grid grid-cols-2 gap-4\">\n                <div class=\"col-span-2\">\n                    <label class=\"block mb-1 text-sm font-medium\">Item Name</label>\n                    <input type=\"text\" id=\"giftItemName\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Price</label>\n                    <input type=\"number\" step=\"0.01\" id=\"giftItemPrice\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\" required>\n                </div>\n                <div>\n                    <label class=\"block mb-1 text-sm font-medium\">Image URL</label>\n                    <input type=\"text\" id=\"giftItemImageUrl\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\">\n                </div>\n                <div class=\"col-span-2\">\n                    <label class=\"block mb-1 text-sm font-medium\">Description</label>\n                    <input type=\"text\" id=\"giftItemDesc\" class=\"border border-outline-variant p-3 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:outline-none w-full\">\n                </div>\n            </div>"}
        };

        StringBuilder modalsHtml = new StringBuilder();

        for (String[] sec : sections) {
            String sectionId = sec[0];
            String title = sec[1];
            String btnText = sec[2];
            String modalId = sec[3];
            String formFunc = sec[4];
            String inputs = sec[5];

            // Replace header
            String headerPattern = "<h2 class=\"font-headline-lg text-headline-lg text-primary mb-4\">\\s*" + Pattern.quote(title) + "\\s*</h2>";
            String newHeader = "<div class=\"flex justify-between items-center mb-4\">\n" +
                               "      <h2 class=\"font-headline-lg text-headline-lg text-primary\">" + title + "</h2>\n" +
                               "      <button onclick=\"document.getElementById('modal-add-" + modalId + "').classList.remove('hidden')\" class=\"px-4 py-2 bg-blue-500 text-white rounded-2xl font-label-md hover:bg-blue-600 transition-colors shadow-sm flex items-center gap-2\"><span class=\"material-symbols-outlined text-sm\">add</span> " + btnText + "</button>\n" +
                               "  </div>";
            content = content.replaceFirst(headerPattern, newHeader);

            // Delete inline form
            String formDivPattern = "<div class=\"bg-surface-container-lowest p-md rounded-xl ambient-shadow mb-6\">\\s*<form onsubmit=\"" + Pattern.quote(formFunc) + "\".*?</form>\\s*</div>";
            content = content.replaceAll("(?s)" + formDivPattern, "");
            
            // Or if it lacks p-md due to some layout changes:
            String formDivPatternAlt = "<div class=\"bg-surface-container-lowest rounded-xl ambient-shadow mb-6\">\\s*<form onsubmit=\"" + Pattern.quote(formFunc) + "\".*?</form>\\s*</div>";
            content = content.replaceAll("(?s)" + formDivPatternAlt, "");

            // Build Modal
            String modal = "\n<!-- Add " + btnText + " Modal -->\n" +
                           "<div id=\"modal-add-" + modalId + "\" class=\"fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50\">\n" +
                           "    <div class=\"bg-surface p-6 rounded-3xl w-[500px] shadow-2xl animate-fade-in-up\">\n" +
                           "        <h3 class=\"text-xl font-bold mb-6 text-primary\">" + btnText + "</h3>\n" +
                           "        <form onsubmit=\"" + formFunc + "; closeModal('modal-add-" + modalId + "');\">\n" +
                           "            " + inputs + "\n" +
                           "            <div class=\"col-span-full flex justify-end gap-3 mt-8 w-full\">\n" +
                           "                <button type=\"button\" onclick=\"closeModal('modal-add-" + modalId + "')\" class=\"px-6 py-2.5 text-on-surface-variant bg-surface-container hover:bg-surface-container-high rounded-2xl transition-colors font-medium\">Cancel</button>\n" +
                           "                <button type=\"submit\" class=\"px-6 py-2.5 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl shadow-sm hover:shadow-md transition-all font-medium\">Save</button>\n" +
                           "            </div>\n" +
                           "        </form>\n" +
                           "    </div>\n" +
                           "</div>\n";
            modalsHtml.append(modal);
        }

        content = content.replace("</body>", modalsHtml.toString() + "\n</body>");

        // Some minor updates to search bar removal (already done in html but just to ensure it looks good)
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        System.out.println("Done!");
    }
}
