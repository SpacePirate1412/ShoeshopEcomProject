package page;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class OrderPage extends JPanel {

    private static final Path ORDERS_CSV = Paths.get("orders.csv");

    public OrderPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("รายการคำสั่งซื้อ", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        loadOrders(listPanel);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    /** โหลดออเดอร์จากไฟล์ orders.csv (เฉพาะ user ปัจจุบัน) */
    private void loadOrders(JPanel panel) {
        try {
            //  อ่านชื่อ user ที่ล็อกอินอยู่
            String currentUser = "Guest";
            String[] session = Session.loadSession();
            if (session != null && session.length > 0) {
                currentUser = session[0];
            }

            //  ถ้าไฟล์ไม่พบ
            if (!Files.exists(ORDERS_CSV) || Files.size(ORDERS_CSV) == 0) {
                showEmptyMessage(panel);
                return;
            }

            //  โหลดชื่อสินค้าจาก stock.csv
            Map<String, String> skuToName = new HashMap<>();
            Path stockPath = Paths.get("stock.csv");
            if (Files.exists(stockPath)) {
                List<String> stockLines = Files.readAllLines(stockPath, StandardCharsets.UTF_8);
                for (int i = 1; i < stockLines.size(); i++) {
                    String[] s = stockLines.get(i).split(",", -1);
                    if (s.length >= 2) {
                        skuToName.put(s[0].replace("\"", "").trim(), s[1].replace("\"", "").trim());
                    }
                }
            }

            List<String> lines = Files.readAllLines(ORDERS_CSV, StandardCharsets.UTF_8);
            if (lines.size() <= 1) {
                showEmptyMessage(panel);
                return;
            }

            boolean hasOrder = false;

            for (int i = 1; i < lines.size(); i++) {
                String[] c = lines.get(i).split(",", -1);
                if (c.length < 7) continue;

                for (int j = 0; j < c.length; j++) {
                    c[j] = c[j].replace("\"", "").trim();
                }

                String username = c[1]; // username
                String date = c[2];
                String total = c[3];
                String status = c[4];
                String sku = c[5];
                String qty = c[6];
                String size = (c.length > 7) ? c[7] : "-";

                // กรองเฉพาะออเดอร์ของ user ปัจจุบัน และที่กำลังจัดส่ง
                if (!username.equalsIgnoreCase(currentUser)) continue;
                if (!status.equals("กำลังจัดส่ง")) continue;

                hasOrder = true;

                String name = skuToName.getOrDefault(sku, "ไม่พบชื่อสินค้า");
                ImageIcon icon = loadProductImage(sku, 120, 120);

                // การ์ดสินค้าแนวนอน
                JPanel card = new JPanel(new BorderLayout(15, 10));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

                // รูปสินค้า
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
                card.add(imageLabel, BorderLayout.WEST);

                // ข้อมูลสินค้า
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);

                JLabel nameLbl = new JLabel(name);
                nameLbl.setFont(new Font("Tahoma", Font.BOLD, 16));

                JLabel sizeLbl = new JLabel("ไซส์: " + size);
                sizeLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));

                JLabel qtyLbl = new JLabel("จำนวน: " + qty);
                qtyLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));

                JLabel priceLbl = new JLabel("ราคา: ฿" + total);
                priceLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));

                JLabel timeLbl = new JLabel("เวลา: " + date);
                timeLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));

                JLabel statusLbl = new JLabel("สถานะ: " + status);
                statusLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
                statusLbl.setForeground(new Color(0, 153, 51));

                infoPanel.add(nameLbl);
                infoPanel.add(sizeLbl);
                infoPanel.add(qtyLbl);
                infoPanel.add(priceLbl);
                infoPanel.add(timeLbl);
                infoPanel.add(statusLbl);

                card.add(infoPanel, BorderLayout.CENTER);
                panel.add(card);
            }

            if (!hasOrder) {
                showEmptyMessage(panel);
            }

        } catch (IOException e) {
            showEmptyMessage(panel);
        }
    }

    /** แสดงข้อความเมื่อยังไม่มีคำสั่งซื้อ */
    private void showEmptyMessage(JPanel panel) {
        panel.removeAll();

        JLabel msg = new JLabel("ยังไม่มีคำสั่งซื้อ", SwingConstants.CENTER);
        msg.setFont(new Font("Tahoma", Font.PLAIN, 22));
        msg.setForeground(Color.GRAY);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 10, 0);
        centerPanel.add(msg, gbc);

        gbc.gridy = 1;

        panel.setLayout(new BorderLayout());
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    /** โหลดรูปสินค้าเหมือนใน MainFrame */
    private ImageIcon loadProductImage(String sku, int w, int h) {
        try {
            java.net.URL url = getClass().getResource("/page/Picture/shoes/" + sku + ".png");
            if (url == null) {
                return placeholder(w, h, "No Image");
            }
            ImageIcon raw = new ImageIcon(url);
            Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return placeholder(w, h, "Error");
        }
    }

    /** สร้าง placeholder เมื่อไม่พบรูป */
    private ImageIcon placeholder(int w, int h, String text) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        g.drawString(text, 10, h / 2);
        g.dispose();
        return new ImageIcon(img);
    }

    /** ทดสอบรันหน้า OrderPage แยก */
    public static void main(String[] args) {
        JFrame f = new JFrame("Order Page");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1000, 700);
        f.add(new OrderPage());
        f.setVisible(true);
    }
}
