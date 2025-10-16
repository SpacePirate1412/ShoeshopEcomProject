package lib;
/**
 * Mutable ADT for one cart line (Product + quantity).
 */
public class CartItem {
    private final Product product;
    private int quantity;
  
  private void checkRep() {
        if (product == null) {
            throw new RuntimeException("RI violated: product cannot be null.");
        }
        if (quantity <= 0) {
            throw new RuntimeException("RI violated: quantity must be positive.");
        }
    }
/**
     * Create the list in the shoppingcart
     * @param product Objectproduct
     * @param quantity must be positive
     */
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        checkRep();
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    
     public void setQuantity(int quantity) {
        if (quantity < 0) quantity = 0;   // กันค่าติดลบ
        this.quantity = quantity;
    }
/**
     * increase the amount in this list
     * @param amount the amount that you want to increase(must be positive)
     */
    public void increaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Increase must be positive.");
        this.quantity += amount;
        checkRep();
    }
}