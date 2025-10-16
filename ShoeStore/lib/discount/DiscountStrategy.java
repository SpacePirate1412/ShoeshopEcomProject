package lib.discount;
import lib.*;
/**
 * Interface  สำหรับกลยุทธ์การคำนวณราคาสินค้า
 */
public interface DiscountStrategy {
    /**
     * คำนวณราคาสุทธิสำหรับสินค้า 1 รายการ
     * @param item รายการสินค้าในตะกร้า
     * @return ราคาสุทธิหลังใช้โปรโมชัน
     */
    double calculatePrice(CartItem item);
}
