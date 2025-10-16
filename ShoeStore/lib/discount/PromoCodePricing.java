package lib.discount;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lib.CartItem;

/**
 * กลยุทธ์คิดราคาที่อ่านโค้ดโปรโมชั่นจากไฟล์ CSV เท่านั้น
 * ไฟล์ที่ค้นหา (ตามลำดับ):
 *  1) classpath: /lib/discount/promocodes.csv  หรือ  ในแพ็กเกจเดียวกัน "promocodes.csv"
 *  2) ไฟล์ระบบ:  lib/discount/promocodes.csv
 *  3) ไฟล์ระบบ:  promocodes.csv
 *
 * รูปแบบ CSV:
 *   code,percent
 *   SAVE10,10
 *   WELCOME20,20
 */
public class PromoCodePricing implements DiscountStrategy{

    private final String promoCode;                 // โค้ดที่ผู้ใช้กรอก (uppercase แล้ว)
    private final Map<String, Double> codePercents = new HashMap<>(); // map โค้ด → ส่วนลด %

    public PromoCodePricing(String promoCode) {
        this.promoCode = promoCode == null ? "" : promoCode.trim().toUpperCase(Locale.ROOT);
        loadFromCsvStrict(); // โหลดจากไฟล์เท่านั้น
    }

    /// โหลดจาก CSV เท่านั้น 
    private void loadFromCsvStrict() {
        InputStream in = null;

        // 1) จาก classpath (วาง CSV ไว้ในแพ็กเกจเดียวกัน หรือใต้ /lib/discount)
        in = PromoCodePricing.class.getResourceAsStream("promocodes.csv");
        if (in == null) in = PromoCodePricing.class.getResourceAsStream("/lib/discount/promocodes.csv");

        if (in == null) {
            System.err.println("[PromoCodePricing] promocodes.csv not found.");
            return; // ไม่มีไฟล์, ไม่มีส่วนลดใดๆ
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String header = br.readLine();
            if (header == null) return;
            if (header.startsWith("\uFEFF")) header = header.substring(1); // ตัด BOM ถ้ามี

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] cols = line.split(",", -1);
                if (cols.length < 2) continue;

                String code = cols[0].trim().toUpperCase(Locale.ROOT);
                double percent = clampPercent(parseDouble(cols[1], 0));

                if (!code.isEmpty()) codePercents.put(code, percent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double calculatePrice(CartItem item) {
        if (item == null || item.getProduct() == null) return 0.0;

        double base = item.getProduct().getPrice() * item.getQuantity();

        if (promoCode.isEmpty()) return round2(base);

        Double percent = codePercents.get(promoCode);
        if (percent == null) return round2(base); // โค้ดไม่เจอ => ไม่ลด

        double discounted = base * (1.0 - (percent / 100.0));

        return round2(discounted);
    }

    /// โค้ดนี้มีในไฟล์ไหม 
    public boolean isValidCode() {
        return codePercents.containsKey(promoCode);
    }

    /// เปอร์เซ็นต์ส่วนลดของโค้ด (0 ถ้าไม่เจอ) 
    public double getCodePercent() {
        return codePercents.getOrDefault(promoCode, 0.0);
    }

    /// ===== helpers =====
    private static double parseDouble(String s, double def) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; }
    }

    private static double clampPercent(double p) {
        if (p < 0) return 0;
        if (p > 100) return 100;
        return p;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
