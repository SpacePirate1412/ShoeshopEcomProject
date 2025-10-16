package page;

import lib.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class ProductFilterManager {

    private final ProductCatalog catalog;

    public ProductFilterManager(ProductCatalog catalog) {
        this.catalog = catalog;
    }

    /**
     *  ฟิลเตอร์สินค้า
     * @param filterPanel JPanel ของ checkbox
     * @param category    หมวดสินค้า เช่น "new", "men", "women", "special"
     * @return รายการสินค้าที่กรองแล้ว
     */
    public List<Product> filterProducts(JPanel filterPanel, String category) {
        catalog.reload();
        List<Product> allProducts = catalog.getAllProducts();

        // เลือกสินค้าตามหมวดก่อน
        List<Product> categoryProducts = allProducts.stream()
                .filter(p -> {
                    if (category.equalsIgnoreCase("new")) return true; // หน้า New เอาทั้งหมด
                    if (category.equalsIgnoreCase("men")) return "Men".equalsIgnoreCase(p.getGender());
                    if (category.equalsIgnoreCase("women")) return "Women".equalsIgnoreCase(p.getGender());
                    if (category.equalsIgnoreCase("special")) return p.getDiscountPercent() != 0.0;
                    return true;
                })
                .collect(Collectors.toList());

        // อ่าน checkbox
        List<String> selectedBrands = new ArrayList<>();
        List<double[]> selectedPrices = new ArrayList<>();

        for (Component c : filterPanel.getComponents()) {
            if (c instanceof JCheckBox cb && cb.isSelected()) {
                String text = cb.getText().trim();
                if (text.matches("(?i)adidas|nike|puma|converse|new balance")) {
                    selectedBrands.add(text);
                } else if (text.matches(".*\\d+.*")) {
                    String[] parts = text.replace("฿", "")
                            .replaceAll("[^0-9\\-]", "")
                            .split("-");
                    if (parts.length == 2) {
                        double min = Double.parseDouble(parts[0]);
                        double max = Double.parseDouble(parts[1]);
                        selectedPrices.add(new double[]{min, max});
                    }
                }
            }
        }

        // ถ้าไม่ติ๊กอะไรเลย ให้ส่ง null เพื่อให้ MainFrame โหลดหน้าใหม่
        if (selectedBrands.isEmpty() && selectedPrices.isEmpty()) {
            return null;
        }

        // กรองตามเงื่อนไข
        return categoryProducts.stream()
                .filter(p -> {
                    boolean brandOk = selectedBrands.isEmpty() || selectedBrands.contains(p.getBrand());
                    double price = p.getPriceAfterBuiltInDiscount();
                    boolean priceOk = selectedPrices.isEmpty() ||
                            selectedPrices.stream().anyMatch(r -> price >= r[0] && price <= r[1]);
                    return brandOk && priceOk;
                })
                .collect(Collectors.toList());
    }
}
