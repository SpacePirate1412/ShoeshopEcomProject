package page;

import javax.swing.*;
import java.awt.*;

public class AllProducts extends base {

    public AllProducts() {
        super("สินค้าทั้งหมด",true);
        setLayout(new BorderLayout());

        // ฝั่งซ้าย = Filter
        add(createFilterPanel(), BorderLayout.WEST);

        // ฝั่งขวา = Product Grid
        add(createProductPanel(), BorderLayout.CENTER);

        // ขยายเต็มจอ
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // 🔹 Filter Panel (ฝั่งซ้าย)
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setPreferredSize(new Dimension(200, 0));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        filterPanel.add(new JLabel("FILTER"));

        // Brand
        filterPanel.add(new JLabel("BRAND"));
        String[] brands = {"Adidas", "Nike", "ASICS", "New Balance", "Puma", "Converse"};
        for (String b : brands) {
            filterPanel.add(new JCheckBox(b));
        }

        // Size
        filterPanel.add(Box.createVerticalStrut(10));
        filterPanel.add(new JLabel("SIZE"));
        int[] sizes = {35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46};
        for (int s : sizes) {
            filterPanel.add(new JCheckBox(String.valueOf(s)));
        }

        // Price
        filterPanel.add(Box.createVerticalStrut(10));
        filterPanel.add(new JLabel("PRICE"));
        JTextField minPrice = new JTextField("ราคาต่ำสุด");
        JTextField maxPrice = new JTextField("ราคาสูงสุด");
        JButton applyBtn = new JButton("ตกลง");

        filterPanel.add(minPrice);
        filterPanel.add(maxPrice);
        filterPanel.add(applyBtn);

        return filterPanel;
    }

    // 🔹 Product Panel (ฝั่งขวา)
    private JScrollPane createProductPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 4, 15, 15)); // 4 คอลัมน์
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ✅ สินค้าตัวอย่าง
        panel.add(createProductCard("Air Jordan 1 Low", "4,300.00 บาท", "Picture/shoe1.png"));
        panel.add(createProductCard("New Balance 740", "4,300.00 บาท", "Picture/shoe2.png"));
        panel.add(createProductCard("Chuck Taylor All Star", "2,150.00 บาท", "Picture/shoe3.png"));
        panel.add(createProductCard("New Balance 2002R", "5,500.00 บาท", "Picture/shoe4.png"));
        panel.add(createProductCard("Nike Dunk Low Retro SE", "3,049.00 บาท", "Picture/shoe5.png"));
        panel.add(createProductCard("Chuck 70 De Luxe", "3,780.00 บาท", "Picture/shoe6.png"));
        panel.add(createProductCard("Chuck Taylor All Star Lift", "2,700.00 บาท", "Picture/shoe7.png"));
        panel.add(createProductCard("Adidas Duramo SL 2", "2,500.00 บาท", "Picture/shoe8.png"));

        return new JScrollPane(panel); // มี scroll ได้
    }

    // 🔹 การ์ดสินค้าแต่ละอัน
private JPanel createProductCard(String name, String price, String imagePath) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

    // รูปสินค้า
    ImageIcon icon = new ImageIcon(imagePath);
    Image img = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
    JLabel imgLabel = new JLabel(new ImageIcon(img));
    imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(imgLabel);

    // ชื่อสินค้า
    JLabel nameLabel = new JLabel(name);
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
    card.add(nameLabel);

    // ราคา
    JLabel priceLabel = new JLabel(price);
    priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    priceLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
    card.add(priceLabel);

    // ปุ่ม
    JButton addBtn = new JButton("เพิ่มไปยังตะกร้า");
    addBtn.setBackground(Color.BLACK);
    addBtn.setForeground(Color.WHITE);
    addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(Box.createVerticalStrut(5));
    card.add(addBtn);

    return card;
}
}
