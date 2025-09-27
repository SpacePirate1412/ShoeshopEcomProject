/*package page;
import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String path) {
        backgroundImage = new ImageIcon(path).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int imgWidth = backgroundImage.getWidth(this);
        int imgHeight = backgroundImage.getHeight(this);

        if (imgWidth > 0 && imgHeight > 0) {
            // คำนวณ scale แบบ cover
            double scaleX = (double) panelWidth / imgWidth;
            double scaleY = (double) panelHeight / imgHeight;
            double scale = Math.max(scaleX, scaleY);

            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);

            // วาดจากมุมซ้ายบน ไม่จัดกลาง
            g.drawImage(backgroundImage, 0, 0, newWidth, newHeight, this);
        }
    }
}*/
