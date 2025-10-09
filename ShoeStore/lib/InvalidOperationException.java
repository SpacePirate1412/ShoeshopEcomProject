package lib;

/**
 * Checked Exception ที่จะถูกโยนเมื่อมีการพยายามดำเนินการที่ไม่ถูกต้อง
 * เช่น การลบสินค้าที่ไม่มีอยู่ในตะกร้า
 */
public class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

