package page;

import java.awt.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;

public class signup extends base {

    private Map<String, Map<String, Map<String, String>>> thaiAddresses;

        // ===== Validation Methods =====
        private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;

        // Normalize → ตัวเล็กทั้งหมด
        email = email.toLowerCase();

        // Regex พื้นฐาน
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex) || email.length() > 100) return false;

        // จำกัดโดเมนที่อนุญาต
         String[] allowedDomains = {"@gmail.com", "@hotmail.com", "@ku.th"};
         for (String domain : allowedDomains) {
            if (email.endsWith(domain)) {
                 return true;
            }
        }
        return false;
    }


    private boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        String regex = "^[a-zA-Z0-9._-]{4,20}$"; // 4–20 ตัว
        return username.matches(regex);
    }

    private boolean isValidPassword(String password, String email, String username) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");

        // ห้ามซ้ำกับ Email หรือ Username
        if (password.equalsIgnoreCase(email) || password.equalsIgnoreCase(username)) {
            return false;
        }

        // ห้ามเป็นอักขระเดียวกันทั้งหมด เช่น 11111111 หรือ aaaaaaaa
        if (password.chars().distinct().count() == 1) {
            return false;
        }

        return hasUpper && hasLower && hasDigit;
    }

    private boolean isValidFullName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.matches("^[ก-๙a-zA-Z\\s]{1,100}$");
    }

    private boolean isValidHouse(String house) {
        if (house == null || house.trim().isEmpty()) return false;
        return house.length() <= 200;
    }

    private boolean isValidPhone(String phone) {
        if (phone == null) return false;

        // ต้องขึ้นต้นด้วย 0 และมี 10 หลัก
        if (!phone.matches("^0[0-9]{9}$")) {
            return false;
        }

        // ห้ามเป็นตัวเลขซ้ำทุกตัว เช่น "0000000000"
        if (phone.chars().distinct().count() == 1) {
            return false;
        }

        return true;
    }

    public signup() {
        super("Sign Up", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // โหลดข้อมูลจังหวัด/อำเภอ/ตำบล/รหัสไปรษณีย์
        thaiAddresses = address.loadAddresses("page/thai_addresses.csv");

        // ===== พื้นหลังด้านซ้าย =====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(600, 600));
        leftPanel.setOpaque(true);
        leftPanel.setBackground(new Color(240, 240, 240));

        ImageIcon shoeImg = new ImageIcon(getClass().getResource("/page/Picture/login5.png"));
        JLabel shoeLabel = new JLabel(shoeImg);
        shoeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shoeLabel.setVerticalAlignment(SwingConstants.CENTER);
        leftPanel.add(shoeLabel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.CENTER);

        // ===== Panel ด้านขวา =====
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(500, 1000));

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

        JLabel houseLabel = new JLabel("หมู่ / บ้านเลขที่");
        houseLabel.setBounds(50, 450, 200, 20);
        rightPanel.add(houseLabel);
        JTextField houseField = new JTextField();
        houseField.setBounds(50, 470, 330, 35);
        rightPanel.add(houseField);

        // Province
        JLabel provinceLabel = new JLabel("จังหวัด");
        provinceLabel.setBounds(50, 510, 200, 20);
        rightPanel.add(provinceLabel);
        JComboBox<String> provinceBox = new JComboBox<>();
        provinceBox.setBounds(50, 530, 330, 35);
        provinceBox.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 14));
        rightPanel.add(provinceBox);

        // District
        JLabel districtLabel = new JLabel("เขต/อำเภอ");
        districtLabel.setBounds(50, 570, 200, 20);
        rightPanel.add(districtLabel);
        JComboBox<String> districtBox = new JComboBox<>();
        districtBox.setBounds(50, 590, 330, 35);
        districtBox.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 14));
        rightPanel.add(districtBox);

        // Subdistrict
        JLabel subdistrictLabel = new JLabel("แขวง/ตำบล");
        subdistrictLabel.setBounds(50, 630, 200, 20);
        rightPanel.add(subdistrictLabel);
        JComboBox<String> subdistrictBox = new JComboBox<>();
        subdistrictBox.setBounds(50, 650, 330, 35);
        subdistrictBox.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 14));
        rightPanel.add(subdistrictBox);

        // Zipcode
        JLabel zipcodeLabel = new JLabel("รหัสไปรษณีย์");
        zipcodeLabel.setBounds(50, 690, 200, 20);
        rightPanel.add(zipcodeLabel);
        JTextField zipcodeField = new JTextField();
        zipcodeField.setBounds(50, 710, 330, 35);
        zipcodeField.setEditable(false);
        rightPanel.add(zipcodeField);

        // Phone
        JLabel phoneLabel = new JLabel("หมายเลขโทรศัพท์");
        phoneLabel.setBounds(50, 750, 200, 20);
        rightPanel.add(phoneLabel);
        JTextField phoneField = new JTextField();
        phoneField.setBounds(50, 770, 330, 35);
        rightPanel.add(phoneField);

        // Signup Button
        JButton signupBtn = new JButton("สมัครสมาชิก");
        signupBtn.setBounds(50, 820, 330, 40);
        signupBtn.setBackground(Color.BLACK);
        signupBtn.setForeground(Color.WHITE);
        rightPanel.add(signupBtn);

        // Back to Login
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

        // ScrollPane
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

        // ===== ปุ่มสมัครสมาชิก (ตรวจสอบ + บันทึก CSV) =====
        signupBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String repassword = new String(repasswordField.getPassword());
            String fullname = fullnameField.getText().trim();
            String house = houseField.getText().trim();
            String phone = phoneField.getText().trim();

            String province = (String) provinceBox.getSelectedItem();
            String district = (String) districtBox.getSelectedItem();
            String subdistrict = (String) subdistrictBox.getSelectedItem();
            String zipcode = zipcodeField.getText();

            // role ของผู้สมัครเอง
            String role = "user";

            // Validation
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกอีเมลให้ถูกต้อง", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidUsername(username)) {
                JOptionPane.showMessageDialog(this, "ชื่อผู้ใช้ต้องมี 4-20 ตัว และใช้ได้เฉพาะ a-z, A-Z, 0-9, ., _, -", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidPassword(password, email, username)) {
                JOptionPane.showMessageDialog(this, "รหัสผ่านต้อง ≥ 8 ตัว มีทั้งตัวใหญ่ ตัวเล็ก ตัวเลข และห้ามซ้ำกับอีเมล/ชื่อผู้ใช้", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(repassword)) {
                JOptionPane.showMessageDialog(this, "รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidFullName(fullname)) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกชื่อ-นามสกุล (ภาษาไทย/อังกฤษ)", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidHouse(house)) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกบ้านเลขที่ (ไม่เกิน 200 ตัวอักษร)", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "หมายเลขโทรศัพท์ต้องขึ้นต้นด้วย 0 และมี 10 หลัก", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // ส่ง role = user ไปบันทึก
                boolean success = User.register(username, password, email, fullname, house,
                        subdistrict, district, province, zipcode, phone, role);

                if (success) {
                    JOptionPane.showMessageDialog(this, "สมัครสมาชิกสำเร็จ!", "สำเร็จ", JOptionPane.INFORMATION_MESSAGE);

                    // กลับไปหน้า Login
                    dispose();
                    new ShoeStoreLogin().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการบันทึกข้อมูล", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            }
        });

        setSize(1100, 950);
        setLocationRelativeTo(null);
    }
}
