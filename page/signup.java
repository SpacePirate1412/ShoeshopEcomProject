package page;

import javax.swing.*;
import java.awt.*;

public class signup extends base {

    public signup() {
        super("Sign Up");
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

        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(500, 750));

        JLabel title = new JLabel("สมัครสมาชิก / Sign Up");
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        title.setBounds(80, 30, 300, 30);
        rightPanel.add(title);

        // ===== Email =====
        JLabel emailLabel = new JLabel("อีเมล");
        emailLabel.setBounds(50, 80, 200, 20);
        rightPanel.add(emailLabel);
        JTextField emailField = new JTextField();
        emailField.setBounds(50, 100, 330, 35);
        rightPanel.add(emailField);

        // ===== Username =====
        JLabel userLabel = new JLabel("ชื่อผู้ใช้งาน");
        userLabel.setBounds(50, 150, 200, 20);
        rightPanel.add(userLabel);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 170, 330, 35);
        rightPanel.add(usernameField);

        // ===== Password =====
        JLabel passLabel = new JLabel("รหัสผ่าน");
        passLabel.setBounds(50, 220, 200, 20);
        rightPanel.add(passLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 240, 330, 35);
        rightPanel.add(passwordField);

        JLabel repassLabel = new JLabel("ยืนยันรหัสผ่าน");
        repassLabel.setBounds(50, 290, 200, 20);
        rightPanel.add(repassLabel);
        JPasswordField repasswordField = new JPasswordField();
        repasswordField.setBounds(50, 310, 330, 35);
        rightPanel.add(repasswordField);

        // ===== Address Section =====
        JLabel addressLabel = new JLabel("ที่อยู่ในการจัดส่ง");
        addressLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        addressLabel.setBounds(50, 360, 200, 20);
        rightPanel.add(addressLabel);

        JLabel nameLabel = new JLabel("ชื่อ - นามสกุล");
        nameLabel.setBounds(50, 390, 200, 20);
        rightPanel.add(nameLabel);
        JTextField fullnameField = new JTextField();
        fullnameField.setBounds(50, 410, 330, 35);
        rightPanel.add(fullnameField);

        JLabel houseLabel = new JLabel("บ้านเลขที่");
        houseLabel.setBounds(50, 450, 200, 20);
        rightPanel.add(houseLabel);
        JTextField houseField = new JTextField();
        houseField.setBounds(50, 470, 330, 35);
        rightPanel.add(houseField);

        JLabel subdistrictLabel = new JLabel("แขวง/ตำบล");
        subdistrictLabel.setBounds(50, 510, 200, 20);
        rightPanel.add(subdistrictLabel);
        JTextField subdistrictField = new JTextField();
        subdistrictField.setBounds(50, 530, 330, 35);
        rightPanel.add(subdistrictField);

        JLabel districtLabel = new JLabel("เขต/อำเภอ");
        districtLabel.setBounds(50, 570, 200, 20);
        rightPanel.add(districtLabel);
        JTextField districtField = new JTextField();
        districtField.setBounds(50, 590, 330, 35);
        rightPanel.add(districtField);

        JLabel provinceLabel = new JLabel("จังหวัด");
        provinceLabel.setBounds(50, 630, 200, 20);
        rightPanel.add(provinceLabel);
        JTextField provinceField = new JTextField();
        provinceField.setBounds(50, 650, 330, 35);
        rightPanel.add(provinceField);

        JLabel zipcodeLabel = new JLabel("รหัสไปรษณีย์");
        zipcodeLabel.setBounds(50, 690, 200, 20);
        rightPanel.add(zipcodeLabel);
        JTextField zipcodeField = new JTextField();
        zipcodeField.setBounds(50, 710, 330, 35);
        rightPanel.add(zipcodeField);

        JLabel phoneLabel = new JLabel("หมายเลขโทรศัพท์");
        phoneLabel.setBounds(50, 750, 200, 20);
        rightPanel.add(phoneLabel);
        JTextField phoneField = new JTextField();
        phoneField.setBounds(50, 770, 330, 35);
        rightPanel.add(phoneField);

        // ===== Signup Button =====
        JButton signupBtn = new JButton("สมัครสมาชิก");
        signupBtn.setBounds(50, 820, 330, 40);
        signupBtn.setBackground(Color.BLACK);
        signupBtn.setForeground(Color.WHITE);
        rightPanel.add(signupBtn);

        // ===== Back to Login =====
        JButton loginLink = new JButton("มีบัญชีอยู่แล้วใช่ไหม? เข้าสู่ระบบ");
        loginLink.setBounds(50, 870, 300, 30);
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addActionListener(e -> {
            dispose();
            new ShoeStoreLogin().setVisible(true);
        });
        rightPanel.add(loginLink);

        add(rightPanel, BorderLayout.EAST);

        setSize(1100, 950);
        setLocationRelativeTo(null);
    }
}
