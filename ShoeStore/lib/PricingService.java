package lib;
import java.util.HashMap;
import java.util.Map;
import lib.discount.*;
/**
 * คลาสสำหรับจัดการโปรโมชันและคำนวณราคา
 * จัดการกลยุทธ์ส่วนลดและคำนวณราคา
 * - ถ้ามี globalStrategy จะใช้มัน (เช่น โปรโมโค้ด)
 * - ไม่งั้นเช็คกลยุทธ์ตาม SKU
 * - ถ้ายังไม่เจอ ใช้ DefaultPricingStrategy (ดู % ส่วนลดในตัวสินค้า)
 */
public class PricingService {
    private final Map<String, DiscountStrategy> skuStrategies = new HashMap<>();
    private final DiscountStrategy defaultStrategy = new DefaultPricingStrategy();
    private DiscountStrategy globalStrategy = null; // โปรโมโค้ด/กลยุทธ์ภาพรวม

    /** ผูกกลยุทธ์ให้ SKU หนึ่งๆ (แทนที่ของเดิมอัตโนมัติ) */
    public void addStrategy(String sku, DiscountStrategy strategy) {
        if (sku == null || strategy == null) return;
        skuStrategies.put(sku, strategy);
    }
    public void removeStrategy(String sku) { skuStrategies.remove(sku); }
    public void clearSkuStrategies() { skuStrategies.clear(); }

    /** ตั้งกลยุทธ์แบบ global (ใช้กับทุกสินค้า) */
    public void setGlobalStrategy(DiscountStrategy strategy) { this.globalStrategy = strategy; }
    public void clearGlobalStrategy() { this.globalStrategy = null; }

    /** สะดวกใช้: เปิดโปรโมโค้ดแบบ global; คืน true ถ้าโค้ดมีในไฟล์ */
    public boolean applyPromoCode(String code) {
        PromoCodePricing pc = new PromoCodePricing(code);
        this.globalStrategy = pc;
        return pc.isValidCode();
    }

    /** คำนวณราคาของรายการเดียว */
    public double calculateItemPrice(CartItem item) {
        if (item == null || item.getProduct() == null) return 0.0;

        DiscountStrategy strategy = globalStrategy;                 // 1) โปรโมโค้ด/กลยุทธ์รวม
        if (strategy == null) {                                     // 2) กลยุทธ์ตาม SKU
            String sku = item.getProduct().getSku();
            strategy = skuStrategies.get(sku);
        }
        if (strategy == null) strategy = defaultStrategy;           // 3) ดีฟอลต์

        return strategy.calculatePrice(item);
    }
}
