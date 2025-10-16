package page;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lib.CartItem;
import lib.Order;

public class Collectorder {

    private static final Path ORDERS_CSV = Paths.get("orders.csv");

    /**
     * saveOrder รองรับ sizeSnap จาก MainFrame
     */
    public static boolean saveOrder(Order order, Map<String, String> sizeSnap) {
        try {
            boolean newFile = !Files.exists(ORDERS_CSV);

            // เขียนไฟล์แบบ Append
            try (BufferedWriter writer = Files.newBufferedWriter(
                    ORDERS_CSV,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                // ถ้ายังไม่มีไฟล์ให้เขียน header ก่อน
                if (newFile) {
                    writer.write("orderId,username,date,total,status,sku,qty,size");
                    writer.newLine();
                }

                // แปลงวันที่เป็น string
                String dateStr = order.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // เขียนสินค้าทุกชิ้นในออเดอร์
                for (CartItem item : order.getItems()) {
                    // ล้างข้อมูล SKU ให้สะอาด
                    String sku = item.getProduct().getSku().trim().replace("\"", "");
                    int qty = item.getQuantity();

                    // หาไซส์แบบ normalize ทั้งสองฝั่ง
                    String size = "-";
                    for (Map.Entry<String, String> entry : sizeSnap.entrySet()) {
                        String key = entry.getKey().trim().replace("\"", "");
                        if (key.equalsIgnoreCase(sku)) {
                            size = entry.getValue();
                            break;
                        }
                    }

                    // เขียนบรรทัดใหม่ใน CSV
                    writer.write(String.format(
                            "\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",%d,\"%s\"",
                            order.getOrderId(),
                            order.getUsername(),
                            dateStr,
                            order.getTotalPrice(),
                            order.getStatus(),
                            sku,
                            qty,
                            size
                    ));
                    writer.newLine();
                }
            }

            return true;

        } catch (IOException e) {
            System.err.println(" บันทึกออเดอร์ล้มเหลว: " + e.getMessage());
            return false;
        }
    }
}
