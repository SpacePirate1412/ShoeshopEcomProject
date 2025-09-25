import java.util.Objects;

/**
 * Immutable Product ADT
 */
public final class Product {
    private final String sku;
    private final String name;
    private final String brand;
    private final double price;
    private final String gender;
    private final double discount; 
    private final int quantity; 
  // Rep Invariant (RI):
    //  -sku, name, brand and gender are not null or blank.
    //  - price >= 0.
    //  - discount >= 0
    //  - quantity >= 0
    // Abstraction Function (AF):
    //  - AF(sku, name, brand, price, gender, discount, quantity) = A product with the given sku, name, brand, gender, discount, quantity and price.

/**
     * check Rep Invariant 
     */
    private void checkRep() {
        if (sku == null || sku.isBlank()) {
            throw new RuntimeException("RI violated: productId cannot be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new RuntimeException("RI violated: productName cannot be blank.");
        }
        if (brand == null || brand.isBlank()) {
            throw new RuntimeException("RI violated: productBrand cannot be blank.");
        }
        if (gender == null || gender.isBlank()) {
            throw new RuntimeException("RI violated: productName cannot be blank.");
        }
        if (discount < 0 ) {
            throw new RuntimeException("RI violated: discount cannot be negative.");
        }
        if (price < 0) {
            throw new RuntimeException("RI violated: price cannot be negative.");
        }
        if (quantity < 0) {
            throw new RuntimeException("RI violated: quantity cannot be negative.");
        }
    }
 /**
     * Create object Product
     * @param sku รnot be null or blank
     * @param name not be null or blank
     * @parem brand not be null or blank
     * @parem gender not be null or blank
     * @parem discount not be negative
     * @parem quantity not be negative
     * @param price not be negative
     * @throws IllegalArgumentException if the feature of product is error
     */
    public Product(String sku, String name, String brand, double price, String gender, double discountPercent, int quantity) {
        this.sku = sku;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.gender = gender;
        this.discount = discountPercent;
        this.quantity = quantity;
        checkRep();
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public String getGender() { return gender; }
    public double getDiscountPercent() { return discount; }
    public int getQuantity() { return quantity; }

    /** Price after the discount from the store not promocode */
    public double getPriceAfterBuiltInDiscount() {
        return price * (1.0 - (discount / 100.0));
    }
/**
     * Compare two products using sku
     * @param obj the object that you want to compare
     * @return true if sku is the same sku
     */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product other = (Product) o;
        return Objects.equals(this.sku, other.sku);
    }
/**
     * create has code from the sku
     * @return the value of hash code
     */
    @Override public int hashCode() {
        return Objects.hash(sku);
    }

    @Override public String toString() {
        return "Product{" + sku + " " + name + " " + brand + " price=" + price +
                " gender=" + gender + " discount=" + discount + " qty=" + quantity + "}";
    }
}
