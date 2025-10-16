package lib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private static int counter = 1; // ใช้สร้าง orderId อัตโนมัติ
    private String orderId;
    private String username;
    private LocalDateTime date;
    private List<CartItem> items;
    private double totalPrice;
    private String status;

    // ✅ Constructor หลัก
    public Order(String username, LocalDateTime date, List<CartItem> items, double totalPrice, String status) {
        this.orderId = generateOrderId();
        this.username = username;
        this.date = date;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // ✅ สร้างรหัสคำสั่งซื้ออัตโนมัติ เช่น ORD0001, ORD0002
    private static String generateOrderId() {
        return String.format("ORD%04d", counter++);
    }

    // ✅ Getter
    public String getOrderId() { return orderId; }
    public String getUsername() { return username; }
    public LocalDateTime getDate() { return date; }
    public List<CartItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    // ✅ แปลงเป็นข้อความ CSV สำหรับบันทึก
    public String toCSVString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String itemList = items.stream()
                .map(i -> i.getProduct().getSku() + "x" + i.getQuantity())
                .reduce((a, b) -> a + ";" + b)
                .orElse("");
        return String.join(",",
                orderId,
                username,
                date.format(fmt),
                String.format("%.2f", totalPrice),
                status,
                itemList
        );
    }

    // ✅ ใช้เพื่อแสดงผลในคอนโซลหรือ debug
    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", username='" + username + '\'' +
                ", date=" + date +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                '}';
    }
}
