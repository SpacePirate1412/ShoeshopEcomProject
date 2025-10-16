package lib.discount;
import lib.*;

public class DefaultPricingStrategy implements DiscountStrategy {

    @Override
    public double calculatePrice(CartItem item) {
        if (item == null || item.getProduct() == null) return 0.0;

        double unit = item.getProduct().getPrice();
        double percent = clamp(item.getProduct().getDiscountPercent()); // ส่วนลดที่มากับสินค้า 0–100

        if (percent > 0) {
            unit = unit * (1.0 - (percent / 100.0));
        }
        return total(unit * item.getQuantity());
    }

    private static double clamp(double p) {
        if (p < 0) return 0;
        if (p > 100) return 100;
        return p;
    }

    private static double total(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}

