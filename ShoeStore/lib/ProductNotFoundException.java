package lib;
/**
 * Checked Exception ที่จะถูกโยนเมื่อไม่พบสินค้าที่ต้องการ
 */
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}