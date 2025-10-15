package lib;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ProductCatalog {
    private final ArrayList<Product> products = new ArrayList<>();
    private final String csvPath;

    public ProductCatalog() {
        this("stock.csv");
    }
    public ProductCatalog(String csvPath) {
        this.csvPath = csvPath;
        loadFromCSV();
        checkRep();
    }

    public final void reload() {
        loadFromCSV();
        checkRep();
    }

    public void addProduct(Product product) {
        if (product == null) return;
        String sku = getSku(product);
        if (sku != null && !sku.isEmpty()) {
            for (Product p : products) {
                if (sku.equalsIgnoreCase(getSku(p))) return; // กันซ้ำตาม SKU
            }
        } else {
            if (products.contains(product)) return;
        }
        products.add(product);
        checkRep();
    }

    /** ใช้ SKU เป็นตัวค้นหาหลัก */
    public Product findBySku(String sku) throws ProductNotFoundException {
        for (Product p : products) {
            if (sku != null && sku.equalsIgnoreCase(getSku(p))) return p;
        }
        throw new ProductNotFoundException("Product with SKU '" + sku + "' not found in catalog.");
    }

    /** คงเมธอดเดิม แต่ map ไปที่ SKU เพื่อไม่ให้โค้ดอื่นพัง */
    public Product findById(String productId) throws ProductNotFoundException {
        return findBySku(productId);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // ===== CSV =====
    private void loadFromCSV() {
        products.clear();

        File f1 = new File(csvPath);
        File f2 = new File("page/" + csvPath);
        File f3 = new File("stock.csv");
        File file = f1.exists() ? f1 : (f2.exists() ? f2 : (f3.exists() ? f3 : f1));
        if (file == null || !file.exists()) {
    System.err.println("stock.csv not found. Tried: " + f1.getPath() + ", " + f2.getPath() + ", " + f3.getPath());
    return; // หรือโยน exception ตามสะดวก
}
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String header = br.readLine();               // ข้ามหัวตาราง
            if (header == null) return;
            String[] heads = parseCSVLine(header);
            // ลบ BOM ถ้ามีในคอลัมน์แรก
            if (heads.length > 0 && heads[0] != null && heads[0].startsWith("\uFEFF")) {
                heads[0] = heads[0].substring(1);
}
            Map<String,Integer> idx = new HashMap<>();
            for (int i = 0; i < heads.length; i++) {
                idx.put(heads[i].trim().toLowerCase(Locale.ROOT), i);
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] cols = parseCSVLine(line);

                String sku      = get(cols, idx, "sku");
                String name     = get(cols, idx, "name");
                String brand    = get(cols, idx, "brand");
                String priceS   = get(cols, idx, "price");
                String gender   = get(cols, idx, "gender");
                String discS    = get(cols, idx, "discount");
                String qtyS     = get(cols, idx, "quantity");

                double price    = parseDouble(priceS, 0.0);
                double discount = parseDouble(discS, 0.0);
                int quantity    = (int)Math.round(parseDouble(qtyS, 0.0));

                Product p = makeProduct(sku, name, brand, price, gender, discount, quantity);
                products.add(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== แปลง CSV → Product (พยายามเรียก setter ที่มี โดยยึด setSku เป็นหลัก) =====
    // สร้าง Product ด้วย ctor 7 พารามิเตอร์ (ตรงกับคลาสของคุณ)
private Product makeProduct(String sku, String name, String brand,
                            double price, String gender, double discount, int quantity) {
    return new Product(sku, name, brand, price, gender, discount, quantity);
}
    /** ดึง SKU จากอ็อบเจ็กต์สินค้า (รองรับหลายชื่อเมธอด แต่ยึด getSku เป็นตัวหลัก) */
    private static String getSku(Product p) {
        for (String m : new String[]{"getSku", "getProductId", "getId", "getCode"}) {
            try {
                Object v = p.getClass().getMethod(m).invoke(p);
                if (v != null) return String.valueOf(v);
            } catch (Exception ignore) {}
        }
        return null;
    }

    private static String get(String[] cols, Map<String,Integer> idx, String key) {
        Integer i = idx.get(key);
        return (i != null && i < cols.length) ? cols[i].trim() : "";
    }
    private static double parseDouble(String s, double def) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; }
    }
    private static String[] parseCSVLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean q = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (q) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') { cur.append('"'); i++; }
                    else q = false;
                } else cur.append(c);
            } else {
                if (c == '"') q = true;
                else if (c == ',') { out.add(cur.toString()); cur.setLength(0); }
                else cur.append(c);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    private void checkRep() {
        if (products == null) throw new RuntimeException("RI violated: products list cannot be null.");
        Set<String> seen = new HashSet<>();
        for (Product p : products) {
            if (p == null) throw new RuntimeException("RI violated: products must not contain null.");
            String sku = getSku(p);
            if (sku != null) {
                String k = sku.toLowerCase(Locale.ROOT);
                if (!seen.add(k)) throw new RuntimeException("RI violated: duplicate SKU: " + sku);
            }
        }
    }
}