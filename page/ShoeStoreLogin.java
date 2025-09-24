package page;

import javax.swing.*;
import java.awt.*;

public class ShoeStoreLogin extends base {

    public ShoeStoreLogin() {
        super("Login");
         setExtendedState(JFrame.MAXIMIZED_BOTH);
        // ===== พื้นหลังด้านซ้าย =====
        BackgroundPanel leftPanel = new BackgroundPanel("Picture/bg_login.jpg");
        leftPanel.setPreferredSize(new Dimension(600, 600));

        // รูปรองเท้า
        ImageIcon shoeImg = new ImageIcon("Picture/login5.png");
        JLabel shoeLabel = new JLabel(shoeImg);
        shoeLabel.setBounds(250, 50, 900, 900);
        leftPanel.add(shoeLabel);

        add(leftPanel, BorderLayout.CENTER);

        // ===== ฟอร์ม Login =====
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(400, 600));

        JLabel title = new JLabel("เข้าสู่ระบบ / Sign In");
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        title.setBounds(80, 40, 300, 30);
        rightPanel.add(title);

        JLabel userLabel = new JLabel("ชื่อผู้ใช้งาน");
        userLabel.setBounds(50, 100, 200, 20);
        rightPanel.add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 125, 250, 35);
        rightPanel.add(usernameField);

        JLabel passLabel = new JLabel("รหัสผ่าน");
        passLabel.setBounds(50, 180, 200, 20);
        rightPanel.add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 205, 250, 35);
        rightPanel.add(passwordField);

        JButton loginButton2 = new JButton("เข้าสู่ระบบ");
        loginButton2.setBounds(50, 260, 250, 40);
        loginButton2.setBackground(Color.BLACK);
        loginButton2.setForeground(Color.WHITE);
        rightPanel.add(loginButton2);

        JButton registerLabel = new JButton("ยังไม่ได้สร้างบัญชีใช่ไหม? สมัครสมาชิก");
        registerLabel.setBounds(20, 310, 300, 30);
        registerLabel.setBorderPainted(false);
        registerLabel.setContentAreaFilled(false);
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addActionListener(e -> {
            dispose();
            new signup().setVisible(true);
        });
        rightPanel.add(registerLabel);

        add(rightPanel, BorderLayout.EAST);

        setSize(900, 600);
        setLocationRelativeTo(null);
    }
}
