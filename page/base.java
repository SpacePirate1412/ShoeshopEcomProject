package page;

import javax.swing.*;
import java.awt.*;

public class base extends JFrame {

    public base(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== ตั้งฟอนต์ไทยเป็นค่าเริ่มต้น =====
        UIManager.put("Label.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("PasswordField.font", new Font("Tahoma", Font.PLAIN, 14));

        // ===== สร้าง TopBar =====
        add(createTopBar(), BorderLayout.NORTH);
    }

    // 🔹 เมธอดช่วย resize ไอคอน
    protected ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // 🔹 TopBar (ด้านบน)
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(230, 230, 230));

        // ฝั่งซ้าย (เมนู, ค้นหา)
        JPanel leftMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftMenu.setOpaque(false);
        JButton homeBtn = new JButton("หน้าหลัก", resizeIcon("Picture/home.png", 20, 20));
        JButton allProductsBtn = new JButton("สินค้าทั้งหมด", resizeIcon("Picture/shop.png", 20, 20));
        JTextField searchField = new JTextField("ค้นหาสินค้า", 25);
        JButton searchBtn = new JButton(resizeIcon("Picture/search.png", 16, 16));

        leftMenu.add(homeBtn);
        leftMenu.add(allProductsBtn);
        leftMenu.add(searchField);
        leftMenu.add(searchBtn);
            allProductsBtn.addActionListener(e -> {
            dispose(); // ปิดหน้าปัจจุบัน
            new AllProducts(); // เปิดหน้าสินค้าทั้งหมด
        });


        // ฝั่งขวา (หัวใจ, ตะกร้า, login)
        JPanel rightMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightMenu.setOpaque(false);
        JButton heartBtn = new JButton(resizeIcon("Picture/heart.png", 20, 20));
        JButton cartBtn = new JButton(resizeIcon("Picture/basket.png", 20, 20));
        JButton loginBtn = new JButton("เข้าสู่ระบบ");
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setForeground(Color.WHITE);
        

            // กด "เข้าสู่ระบบ" -> เปิดหน้า Login
            loginBtn.addActionListener(e -> {
                dispose();
                new ShoeStoreLogin().setVisible(true); // หน้า Login
            });

        // กดแล้วกลับหน้า Login
        loginBtn.addActionListener(e -> {
            dispose();
            new ShoeStoreLogin().setVisible(true);
        });

        rightMenu.add(heartBtn);
        rightMenu.add(cartBtn);
        rightMenu.add(loginBtn);

        topBar.add(leftMenu, BorderLayout.WEST);
        topBar.add(rightMenu, BorderLayout.EAST);

        return topBar;
        

    }
}
