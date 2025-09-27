package page;

import javax.swing.*;
import java.awt.*;
import java.text.Collator;
import java.util.List;
import java.util.*;

public class signup extends base {

    private Map<String, Map<String, Map<String, String>>> thaiAddresses;

    public signup() {
        super("Sign Up", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // โหลดข้อมูลจังหวัด/อำเภอ/ตำบล/รหัสไปรษณีย์
        thaiAddresses = address.loadAddresses("thai_addresses.csv");

        // ===== พื้นหลังด้านซ้าย =====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(600, 600));
        leftPanel.setOpaque(true);
        leftPanel.setBackground(new Color(240, 240, 240)); // <<< สีพื้นหลังเทาอ่อน

        // รูปรองเท้า
        ImageIcon shoeImg = new ImageIcon("Picture/login5.png");
        JLabel shoeLabel = new JLabel(shoeImg);
        shoeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shoeLabel.setVerticalAlignment(SwingConstants.CENTER);
        leftPanel.add(shoeLabel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.CENTER);

        // ===== Panel ด้านขวา =====
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(500, 1000)); // สูงกว่าหน้าจอ

        JLabel title = new JLabel("สมัครสมาชิก / Sign Up");
        title.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 18));
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
        addressLabel.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
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

        // ===== Province (จังหวัด) =====
        JLabel provinceLabel = new JLabel("จังหวัด");
        provinceLabel.setBounds(50, 510, 200, 20);
        rightPanel.add(provinceLabel);

        JComboBox<String> provinceBox = new JComboBox<>();
        provinceBox.setBounds(50, 530, 330, 35);
        rightPanel.add(provinceBox);

        // ===== District (เขต/อำเภอ) =====
        JLabel districtLabel = new JLabel("เขต/อำเภอ");
        districtLabel.setBounds(50, 570, 200, 20);
        rightPanel.add(districtLabel);

        JComboBox<String> districtBox = new JComboBox<>();
        districtBox.setBounds(50, 590, 330, 35);
        rightPanel.add(districtBox);

        // ===== Subdistrict (แขวง/ตำบล) =====
        JLabel subdistrictLabel = new JLabel("แขวง/ตำบล");
        subdistrictLabel.setBounds(50, 630, 200, 20);
        rightPanel.add(subdistrictLabel);

        JComboBox<String> subdistrictBox = new JComboBox<>();
        subdistrictBox.setBounds(50, 650, 330, 35);
        rightPanel.add(subdistrictBox);

        // ===== Zipcode (รหัสไปรษณีย์) =====
        JLabel zipcodeLabel = new JLabel("รหัสไปรษณีย์");
        zipcodeLabel.setBounds(50, 690, 200, 20);
        rightPanel.add(zipcodeLabel);

        JTextField zipcodeField = new JTextField();
        zipcodeField.setBounds(50, 710, 330, 35);
        zipcodeField.setEditable(false);
        rightPanel.add(zipcodeField);

        provinceBox.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 14));
        districtBox.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 14));
        subdistrictBox.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 14));


        // ===== Phone (โทรศัพท์) =====
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

        // ===== JScrollPane ฝั่งขวา =====
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);

        add(scrollPane, BorderLayout.EAST);

        // ===== เติมจังหวัด (เรียง ก-ฮ) =====
        Locale thaiLocale = new Locale.Builder().setLanguage("th").setRegion("TH").build();
        List<String> provinces = new ArrayList<>(thaiAddresses.keySet());
        Collections.sort(provinces, Collator.getInstance(thaiLocale));
        for (String province : provinces) {
            provinceBox.addItem(province);
        }

        // Province → District
        provinceBox.addActionListener(e -> {
            districtBox.removeAllItems();
            subdistrictBox.removeAllItems();
            zipcodeField.setText("");
            String selectedProvince = (String) provinceBox.getSelectedItem();
            if (selectedProvince != null) {
                Map<String, Map<String, String>> districts = thaiAddresses.get(selectedProvince);
                List<String> districtList = new ArrayList<>(districts.keySet());
                Collections.sort(districtList, Collator.getInstance(thaiLocale));
                for (String district : districtList) {
                    districtBox.addItem(district);
                }
            }
        });

        // District → Subdistrict
        districtBox.addActionListener(e -> {
            subdistrictBox.removeAllItems();
            zipcodeField.setText("");
            String selectedProvince = (String) provinceBox.getSelectedItem();
            String selectedDistrict = (String) districtBox.getSelectedItem();
            if (selectedProvince != null && selectedDistrict != null) {
                Map<String, String> subdistricts = thaiAddresses.get(selectedProvince).get(selectedDistrict);
                List<String> subdistrictList = new ArrayList<>(subdistricts.keySet());
                Collections.sort(subdistrictList, Collator.getInstance(thaiLocale));
                for (String subdistrict : subdistrictList) {
                    subdistrictBox.addItem(subdistrict);
                }
            }
        });

        // Subdistrict → Zipcode
        subdistrictBox.addActionListener(e -> {
            String selectedProvince = (String) provinceBox.getSelectedItem();
            String selectedDistrict = (String) districtBox.getSelectedItem();
            String selectedSubdistrict = (String) subdistrictBox.getSelectedItem();
            if (selectedProvince != null && selectedDistrict != null && selectedSubdistrict != null) {
                String zipcode = thaiAddresses.get(selectedProvince).get(selectedDistrict).get(selectedSubdistrict);
                zipcodeField.setText(zipcode);
            }
        });

        setSize(1100, 950);
        setLocationRelativeTo(null);
    }
}
