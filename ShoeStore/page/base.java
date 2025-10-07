package page;

import javax.swing.*;
import java.awt.*;

public class base extends JFrame {

    public base(String title, boolean showTopBar) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setUIFont(); // เซ็ตฟอนต์

        if (showTopBar) {
            add(createTopBar(), BorderLayout.NORTH);
        }
    }

    private void setUIFont() {
        UIManager.put("Label.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("PasswordField.font", new Font("Tahoma", Font.PLAIN, 14));
    }

    protected ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(230, 230, 230));

        JPanel leftMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftMenu.setOpaque(false);
        leftMenu.add(new JButton("หน้าหลัก"));
        leftMenu.add(new JButton("สินค้าทั้งหมด"));
        leftMenu.add(new JTextField("ค้นหาสินค้า", 25));

        JPanel rightMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightMenu.setOpaque(false);
        JButton loginBtn = new JButton("เข้าสู่ระบบ");
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setForeground(Color.WHITE);
        rightMenu.add(loginBtn);

        topBar.add(leftMenu, BorderLayout.WEST);
        topBar.add(rightMenu, BorderLayout.EAST);

        return topBar;
    }
}
