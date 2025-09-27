package page;

import javax.swing.*;
import java.awt.*;

public class ShoeStoreLogin extends base {

    public ShoeStoreLogin() {
        super("Login",false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ===== ฝั่งซ้าย =====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(600, 600));
        leftPanel.setOpaque(true);
        leftPanel.setBackground(new Color(240, 240, 240)); // สีเทาอ่อน

        // รูปรองเท้า
        ImageIcon shoeImg = new ImageIcon("Picture/login5.png");
        JLabel shoeLabel = new JLabel(shoeImg);
        shoeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shoeLabel.setVerticalAlignment(SwingConstants.CENTER);
        leftPanel.add(shoeLabel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.CENTER);

        // ===== ฝั่งขวา =====
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(500, 600));

        JLabel title = new JLabel("เข้าสู่ระบบ / Login");
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        title.setBounds(150, 30, 300, 30);
        rightPanel.add(title);

        // ===== Username =====
        JLabel userLabel = new JLabel("ชื่อผู้ใช้งาน / Username");
        userLabel.setBounds(50, 100, 200, 20);
        rightPanel.add(userLabel);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 120, 330, 35);
        rightPanel.add(usernameField);

        // ===== Password =====
        JLabel passLabel = new JLabel("รหัสผ่าน / Password");
        passLabel.setBounds(50, 170, 200, 20);
        rightPanel.add(passLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 190, 330, 35);
        rightPanel.add(passwordField);

        // ===== Login Button =====
        JButton loginBtn = new JButton("เข้าสู่ระบบ");
        loginBtn.setBounds(50, 250, 330, 40);
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setForeground(Color.WHITE);
        rightPanel.add(loginBtn);

        // ===== Signup Link =====
        JButton signupLink = new JButton("ยังไม่มีบัญชี? สมัครสมาชิก");
        signupLink.setBounds(50, 310, 300, 30);
        signupLink.setBorderPainted(false);
        signupLink.setContentAreaFilled(false);
        signupLink.setForeground(Color.BLUE);
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLink.addActionListener(e -> {
            dispose();
            new signup().setVisible(true);
        });
        rightPanel.add(signupLink);

        // ===== JScrollPane (เลื่อนได้ แต่ไม่โชว์ scrollbar) =====
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);

        add(scrollPane, BorderLayout.EAST);

        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ShoeStoreLogin().setVisible(true);
        });
    }
}
