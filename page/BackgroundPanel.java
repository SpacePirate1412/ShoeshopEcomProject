package page;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
        setLayout(null); // จะวาง component ข้างในแบบกำหนดเองได้
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // วาดรูปเต็ม Panel ตามขนาด
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
