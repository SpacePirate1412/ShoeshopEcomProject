package lib;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Immutable Product ADT */
public final class Product {
    private final String sku;
    private final String name;
    private final String brand;
    private final double price;
    private final String gender;
    private final double discount;   // percent 0..100
    private final int quantity;      // stock qty

    // RI:
    //  - sku, name, brand, gender not null/blank
    //  - price >= 0
    //  - 0 <= discount <= 100
    //  - quantity >= 0
    // AF:
    //  - Product with given attributes

    /** check Rep Invariant */
    private void checkRep() {
        if (sku == null || sku.isBlank()) {
            throw new RuntimeException("RI violated: sku cannot be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new RuntimeException("RI violated: name cannot be blank.");
        }
        if (brand == null || brand.isBlank()) {
            throw new RuntimeException("RI violated: brand cannot be blank.");
        }
        if (gender == null || gender.isBlank()) {
            throw new RuntimeException("RI violated: gender cannot be blank.");
        }
        if (price < 0) {
            throw new RuntimeException("RI violated: price cannot be negative.");
        }
        if (discount < 0 || discount > 100) {
            throw new RuntimeException("RI violated: discount must be in 0..100.");
        }
        if (quantity < 0) {
            throw new RuntimeException("RI violated: quantity cannot be negative.");
        }
    }

    /**
     * Create Product
     * @param sku not null/blank
     * @param name not null/blank
     * @param brand not null/blank
     * @param price >= 0
     * @param gender not null/blank
     * @param discountPercent 0..100
     * @param quantity >= 0
     */
    public Product(String sku, String name, String brand, double price,
                   String gender, double discountPercent, int quantity) {
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

    /** ราคาหลังหักส่วนลด (built-in discount จากสต็อก ไม่ใช่โปรโมโค้ด) */
    public double getPriceAfterBuiltInDiscount() {
        double net = price * (1.0 - (discount / 100.0));
        return round2(net);
    }

    private static double round2(double v) {
        return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /** Compare two products by sku */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product other = (Product) o;
        return Objects.equals(this.sku, other.sku);
    }

    /** hashCode from sku */
    @Override public int hashCode() {
        return Objects.hash(sku);
    }

    @Override public String toString() {
        return "Product{" + sku + " " + name + " " + brand + " price=" + price +
               " gender=" + gender + " discount=" + discount + " qty=" + quantity + "}";
    }
}
