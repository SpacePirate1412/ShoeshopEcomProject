package lib;
import java.util.ArrayList ;
import java.util.List;

/**
 * ADT สำหรับจัดการตะกร้าสินค้า
 */
public class ShoppingCart {
    private ArrayList<CartItem> items;
    private PricingService pricingService;
    private ProductCatalog productCatalog;

    // Rep Invariant (RI):
    //  - items, pricingService, productCatalog are not null.
    //  - no element in items is null.
    //  - no two CartItems in items have the same Product.
    //
    // Abstraction Function (AF):
    //  - AF(items, ...) = A shopping cart containing the specified items.

    private void checkRep() {
        if (items == null || pricingService == null || productCatalog == null) {
            throw new RuntimeException("RI violated: core components cannot be null.");
        }
        ArrayList<Product> seenProducts = new ArrayList<>();
        for (CartItem item : items) {
            if (item == null) {
                throw new RuntimeException("RI violated: cart contains a null item.");
            }
            if (seenProducts.contains(item.getProduct())) {
                throw new RuntimeException("RI violated: duplicate product found in cart: " + item.getProduct().getSku());
            }
            seenProducts.add(item.getProduct());
        }
    }

    /**
     * สร้างตะกร้าสินค้า
     * @param pricingService service สำหรับคำนวณราคา
     * @param productCatalog service สำหรับค้นหาสินค้า
     */
    public ShoppingCart(PricingService pricingService, ProductCatalog productCatalog) {
        this.items = new ArrayList<>();
        this.pricingService = pricingService;
        this.productCatalog = productCatalog;
        checkRep();
    }

    /**
     * เพิ่มสินค้าเข้าตะกร้าโดยใช้รหัสสินค้า
     * @param productId รหัสของสินค้า
     * @param quantity จำนวนที่ต้องการเพิ่ม
     * @throws ProductNotFoundException หากไม่พบสินค้านั้นในแคตตาล็อก
     */
    public void addItem(String productId, int quantity) throws ProductNotFoundException {
        if (quantity <= 0) {
            // ใช้ Unchecked Exception สำหรับข้อผิดพลาดของโปรแกรมเมอร์ (Precondition violation)
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        
        // findById จะโยน Exception เองถ้าไม่เจอสินค้า
        Product p = productCatalog.findById(productId);

        for (CartItem item : items) {
            if (item.getProduct().equals(p)) {
                item.increaseQuantity(quantity);
                return;
            }
        }
        items.add(new CartItem(p, quantity));
        checkRep();
    }

    /**
     * ลบสินค้า (ทั้งรายการ) ออกจากตะกร้า
     * @param sku รหัสของสินค้าที่ต้องการลบ
     * @throws InvalidOperationException หากไม่พบสินค้านั้นในตะกร้า
     */
    public void removeItem(String sku) throws InvalidOperationException {
        if (sku == null) {
             throw new IllegalArgumentException("Product ID cannot be null.");
        }

        CartItem itemToRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getSku().equals(sku)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
            checkRep();
            return ;
        }
        
        // ถ้าวนลูปจนจบแล้วยังไม่เจอ ให้โยน Exception
        throw new InvalidOperationException("Cannot remove item. Product ID '" + sku + "' not found in cart.");
    }

    /**
     * คำนวณราคารวมของสินค้าทั้งหมดในตะกร้า
     * @return ราคารวมสุทธิ
     */
    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : items) {
            total += pricingService.calculateItemPrice(item);
        }
        return total;
    }
    
    /**
     * @return จำนวนรายการสินค้าในตะกร้า
     */
    public int getItemCount() { return items.size(); }
    
    /**
     * ล้างสินค้าทั้งหมดออกจากตะกร้า
     */
    public void clearCart() {
        items.clear();
        checkRep(); // ตรวจสอบหลังการเปลี่ยนแปลง
    }

    public List<CartItem> getItems() { 
        return new ArrayList<>(items); 
    }
}
