/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package page;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.text.NumberFormat;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.stream.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import lib.*;
import lib.discount.DefaultPricingStrategy;

/**
 *
 * @author Apisit Sumawan
 */

public class MainFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());
        private final ProductCatalog catalog = new ProductCatalog();          // โหลดจาก stock.csv
        private final JPanel womenGrid = new JPanel(new GridBagLayout());    // กริดของหน้า Women
        private final JPanel dealGrid  = new JPanel(new GridBagLayout());    // กริดของหน้า Special Deal
        private final NumberFormat THB = NumberFormat.getNumberInstance(new Locale("th","TH")); //สร้างตัวจัดรูปแบบตัวเลขสำหรับ รูปแบบราคาตัวเลขแบบของไทยแล้วเก็บไว้ในตัวแปร THB
        private final ProductFilterManager filterManager = new ProductFilterManager(catalog);
        //  Shopping cart 
        private final PricingService pricing = new PricingService();
        private final ShoppingCart cart = new ShoppingCart(pricing, catalog);
        private static final double SHIPPING_FEE = 50.0;//ค่าจัดส่ง

        // ป้ายจำนวนบนการ์ดสินค้า (key = SKU)
        private final Map<String, javax.swing.JLabel> skuToQtyLabel = new HashMap<>();
        // จดจำไซส์ต่อ SKU ที่ลูกค้าเลือก
        private final Map<String, String> skuToSize = new HashMap<>();
        /** ดึง snapshot ของ map SKU→Size ปัจจุบัน */
        private Map<String, String> getSkuToSizeSnapshot() {
        return new HashMap<>(skuToSize);
        }



        // หา combobox ไซส์ของหน้าปัจจุบัน จากปุ่มที่กด
        private javax.swing.JComboBox<String> resolveSizeCombo(java.awt.Component source) {
            if (javax.swing.SwingUtilities.isDescendingFrom(source, NewShowProduct))  return sizeComboBox;
            if (javax.swing.SwingUtilities.isDescendingFrom(source, NewShowProduct2)) return sizeComboBoxMen;
            if (javax.swing.SwingUtilities.isDescendingFrom(source, NewShowProduct3)) return sizeComboBoxWomen;
            if (javax.swing.SwingUtilities.isDescendingFrom(source, NewShowProduct4)) return sizeComboBoxSD;
            if (javax.swing.SwingUtilities.isDescendingFrom(source, motherpanel)) return sizeComboBox;
            return null;
        }
        private static final Path STOCK_CSV = Paths.get("stock.csv"); // path หาไฟล์ stock.csv
        // ดึงข้อมูลผู้ใช้
        private static final Path USERS_CSV = Paths.get("users.csv"); // path หาไฟล์ user.csv
        private UserProfile currentUserProfile = null; // profile ของ user
        private static class UserProfile {
    String username, password, email, fullname, house, subdistrict, district, province, zipcode, phone, role;
    String[] toArray() {
        return new String[]{ username, password, email, fullname, house, subdistrict, district, province, zipcode, phone, role };
    }
}
    private void setupNewArea() {
            ProductNew.removeAll();
            ProductNew.setLayout(new BorderLayout());
            ProductNew.add(jTextField4, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(NewShowProduct);
            sp.setBorder(null);
            sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            sp.getVerticalScrollBar().setUnitIncrement(24);
            ProductNew.add(sp, BorderLayout.CENTER);

}

        private void setupMenArea() {
            ProductMen.removeAll();
            ProductMen.setLayout(new BorderLayout());
            ProductMen.add(jTextField3, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(NewShowProduct2);
            sp.setBorder(null);
            sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            sp.getVerticalScrollBar().setUnitIncrement(24);
            ProductMen.add(sp, BorderLayout.CENTER);
}



        private void setupWomenArea() {
    ProductWomen.removeAll();
    ProductWomen.setLayout(new BorderLayout());
    ProductWomen.add(jTextField5, BorderLayout.NORTH);
    JScrollPane sp = new JScrollPane(NewShowProduct3);
    sp.setBorder(null);
    sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sp.getVerticalScrollBar().setUnitIncrement(24);
    ProductWomen.add(sp, BorderLayout.CENTER);
}

        private void setupDealArea() {
    ProductSD.removeAll();
    ProductSD.setLayout(new BorderLayout());
    ProductSD.add(jTextField6, BorderLayout.NORTH);

    JScrollPane sp = new JScrollPane(NewShowProduct4);
    sp.setBorder(null);
    sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sp.getVerticalScrollBar().setUnitIncrement(24);

    ProductSD.add(sp, BorderLayout.CENTER);
}
       

        private void showNew() {
            catalog.reload();
            ArrayList<Product> onlyNoDiscount = new ArrayList<>(catalog.getAllProducts());
            Collections.reverse(onlyNoDiscount);
            ensureGridBag(NewShowProduct);
            populateGrid(NewShowProduct, onlyNoDiscount);

}

        private void showMen() {
           catalog.reload();
            ArrayList<Product> men = new ArrayList<Product>();
            List<Product> all = catalog.getAllProducts();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
        if ("Men".equalsIgnoreCase(p.getGender())) {
            men.add(p);
        }
    }
    ensureGridBag(NewShowProduct2);
    populateGrid(NewShowProduct2, men);
}

        private void showWomen() {
             catalog.reload();
            ArrayList<Product> women = new ArrayList<Product>();
            List<Product> all = catalog.getAllProducts();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
        if ("Women".equalsIgnoreCase(p.getGender())) {
            women.add(p);
        }
    }
    ensureGridBag(NewShowProduct3);
    populateGrid(NewShowProduct3, women);
}

        private void showSpecial() {
            catalog.reload();
            ArrayList<Product> deals = new ArrayList<Product>();
            List<Product> all = catalog.getAllProducts();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
        if (p.getDiscountPercent() != 0.0) {
            deals.add(p);
        }
    }
    ensureGridBag(NewShowProduct4);
    populateGrid(NewShowProduct4, deals);
}



private void updateCartSummary() {
    updateOrderSummary();
    double total = cart.getTotalPrice();                 // จะคิดส่วนลด/โปรโมโค้ดให้เอง
    jLabel58.setText(THB.format(Math.round(total)));  // Grand Total
    // ถ้าจะโชว์ subtotal/discount แยก ค่อยคำนวณเพิ่มทีหลังได้
}


private void ensureGridBag(JPanel panel) {
    if (!(panel.getLayout() instanceof GridBagLayout)) {
        panel.setLayout(new GridBagLayout());
    }
}
private void configureSpacedGrid(JPanel grid) {
    GridBagLayout gbl = new GridBagLayout();
    grid.setLayout(gbl);
    grid.setBackground(Color.WHITE);
}

//เพิ่มช่องว่างระหว่างการ์ด
private void populateGrid(JPanel grid, List<Product> items) {
    grid.removeAll();

    GridBagConstraints gc = new GridBagConstraints();
    gc.insets = new Insets(15, 15, 15, 15); // เพิ่มช่องว่างรอบการ์ด
    gc.fill = GridBagConstraints.BOTH;      // ให้การ์ดยืดเต็ม cell
    gc.weightx = 1.0;                       // ให้ขยายตามแนวนอน
    gc.weighty = 0;                         // แนวตั้งขยายเฉพาะเท่าที่ต้อง
    gc.anchor = GridBagConstraints.NORTHWEST;

    int col = 0, row = 0;
    final int COLS = 4; // แถวละ 4 ช่อง (จะขยายเต็มได้เมื่อหน้าจอกว้าง)
    for (int i = 0; i < items.size(); i++) {
        Product p = items.get(i);
        gc.gridx = col;
        gc.gridy = row;
        grid.add(createProductCard(p), gc);

        col++;
        if (col == COLS) { 
            col = 0; 
            row++; 
        }
    }

    // ดันให้เต็มด้านล่าง (ไม่ให้การ์ดลอย)
    gc.weighty = 1;
    gc.gridy = row + 1;
    grid.add(Box.createVerticalGlue(), gc);

    grid.revalidate();
    grid.repaint();
}



private static final int IMG_H   = 100;
private static final int BRAND_H = 16;
private static final int NAME_H  = 28;  // เผื่อ 2 บรรทัด
private static final int PRICE_H = 18;
private static final int CARD_W = 220;
private static final int CARD_H = 240;
private static final int P = 10;        // padding ภายในการ์ด
private static final int BTN_H = 32;

private static String htmlWrap(String text, int widthPx) {
    if (text == null) text = "";
    // ทำให้ JLabel แสดงหลายบรรทัดตามความกว้างที่กำหนด
    return "<html><div style='width:" + widthPx + "px'>" + text + "</div></html>";
}

private static final String PROMO_PLACEHOLDER = "Enter code...";
//ตรวจว่าโค้ดว่างจริงไหม
private boolean isPromoBlank() {
    String t = jTextField2.getText();
    return t == null || t.trim().isEmpty() || PROMO_PLACEHOLDER.equals(t.trim());
}
private void applyPromoFromField() {
    if (isPromoBlank()) {          //ว่างหรือเป็น placeholder = ไม่ต้องเช็คโค้ด
        updateCartSummary();
        return;
    }
    String code = jTextField2.getText().trim();
    boolean ok = pricing.applyPromoCode(code);
    if (!ok) {
        javax.swing.JOptionPane.showMessageDialog(this, "Invalid promo code!");
        jTextField2.requestFocus();
        jTextField2.selectAll();
        return;
    }
    updateCartSummary();
}
/** โหลดรูปจาก /page/Picture/shoes/<sku>.png และย่อให้พอดีกล่อง */
private javax.swing.ImageIcon loadProductIcon(String sku, int maxW, int maxH) {
    try {
        java.net.URL url = getClass().getResource("/page/Picture/shoes/" + sku + ".png");
        if (url == null) return null;

        javax.swing.ImageIcon raw = new javax.swing.ImageIcon(url);
        int w = raw.getIconWidth(), h = raw.getIconHeight();
        if (w <= 0 || h <= 0) return null;

        double scale = Math.min((double) maxW / w, (double) maxH / h);
        if (scale < 1.0) {
            java.awt.Image scaled = raw.getImage().getScaledInstance(
                (int)Math.round(w * scale),
                (int)Math.round(h * scale),
                java.awt.Image.SCALE_SMOOTH
            );
            return new javax.swing.ImageIcon(scaled);
        }
        return raw;
    } catch (Exception ex) {
        return null;
    }
}
//ตั้งภาษาไทย
private static String pickThaiFontFamily() {
    String[] preferred = {
        "Sarabun", "Noto Sans Thai", "TH Sarabun New",
        "Leelawadee UI", "Leelawadee", "Tahoma", "Segoe UI",
        "Cordia New", "Arial Unicode MS"
    };
    String probe = "ภาษาไทยทดสอบ";
    java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
    for (String f : preferred) {
        try {
            java.awt.Font ft = new java.awt.Font(f, java.awt.Font.PLAIN, 16);
            if (ft.canDisplayUpTo(probe) == -1) return f;
        } catch (Exception ignore) {}
    }
    // ถ้าไม่เจออะไรเลย ให้ใช้ฟอนต์ระบบ
    return new java.awt.Font(null, java.awt.Font.PLAIN, 16).getFamily();
}
//บังคับ font ให้อ่านภาษาไทยได้
private void forceThai(Component root) {
    String family = pickThaiFontFamily();
    applyFontRecursively(root, family);
    javax.swing.SwingUtilities.updateComponentTreeUI(root);
}
private static void applyFontRecursively(java.awt.Component c, String family) {
    java.awt.Font f = c.getFont();
    if (f != null) {
        c.setFont(new java.awt.Font(family, f.getStyle(), f.getSize()));
    }
    if (c instanceof javax.swing.JComponent) {
        javax.swing.border.Border b = ((javax.swing.JComponent) c).getBorder();
        if (b instanceof javax.swing.border.TitledBorder tb) {
            java.awt.Font tf = tb.getTitleFont();
            if (tf != null) tb.setTitleFont(new java.awt.Font(family, tf.getStyle(), tf.getSize()));
        }
    }
    if (c instanceof java.awt.Container cont) {
        for (java.awt.Component ch : cont.getComponents()) {
            applyFontRecursively(ch, family);
        }
    }
}
public static void installThaiUIFont(float sizePts) {
    String family = pickThaiFontFamily();
    java.awt.Font base = new java.awt.Font(family, java.awt.Font.PLAIN, Math.round(sizePts))
            .deriveFont(sizePts);

    javax.swing.UIDefaults d = javax.swing.UIManager.getDefaults();
    java.util.Enumeration<?> e = d.keys();
    while (e.hasMoreElements()) {
        Object key = e.nextElement();
        Object val = d.get(key);
        if (val instanceof javax.swing.plaf.FontUIResource) {
            d.put(key, new javax.swing.plaf.FontUIResource(base));
        }
    }
}

// สร้างการ์ดสินค้า,ปรับขนาดคงที่ของการ์ด/ปุ่ม
private JPanel createProductCard(final Product p) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(230,230,230)));
    card.setPreferredSize(new Dimension(CARD_W, CARD_H));
    card.setMinimumSize(new Dimension(CARD_W, CARD_H));
    card.setMaximumSize(new Dimension(CARD_W, CARD_H));

    // ===== เนื้อการ์ด =====
    Box box = Box.createVerticalBox();
    box.setBorder(javax.swing.BorderFactory.createEmptyBorder(P, P, P, P));

    int imgBoxW = CARD_W - 2 * P;
    int imgBoxH = 110; // ความสูงพื้นที่รูป
    // เซ็ตรูป
    javax.swing.JLabel img = new javax.swing.JLabel("", javax.swing.SwingConstants.CENTER);
    img.setOpaque(true);
    img.setBackground(new java.awt.Color(245, 245, 245));
    img.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    img.setPreferredSize(new java.awt.Dimension(imgBoxW, imgBoxH));

    javax.swing.ImageIcon icon = loadProductIcon(p.getSku(), imgBoxW, imgBoxH);
    if (icon != null) {
        img.setIcon(icon);
    } else {
        img.setText("IMAGE"); // ถ้าไม่พบไฟล์ให้ขึ้นข้อความ
    }
    img.setToolTipText(p.getSku()); // โชว์รหัสเวลา hover
    JLabel brand = new JLabel(p.getBrand());
    brand.setFont(brand.getFont().deriveFont(Font.BOLD, 12f));
    brand.setAlignmentX(Component.LEFT_ALIGNMENT);

    // ชื่อสินค้า 
    JLabel name = new JLabel(htmlWrap(p.getName(), CARD_W - 2*P));
    name.setFont(name.getFont().deriveFont(Font.PLAIN, 11f)); 
    name.setForeground(new Color(70, 70, 70));
    name.setAlignmentX(Component.LEFT_ALIGNMENT);

    // ราคา
    JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    pricePanel.setOpaque(false);
    pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    if (p.getDiscountPercent() != 0.0) {
        JLabel orig = new JLabel("฿ " + THB.format(Math.round(p.getPrice())));
        orig.setFont(orig.getFont().deriveFont(Font.PLAIN, 10f));
        orig.setForeground(Color.GRAY);
        orig.setText("<html><strike>" + orig.getText() + "</strike></html>");

        // มีส่วนลด
        double after = p.getPriceAfterBuiltInDiscount();
        JLabel now = new JLabel("฿ " + THB.format(Math.round(after)));
        now.setFont(now.getFont().deriveFont(Font.BOLD, 11f));
        pricePanel.add(orig); pricePanel.add(now);

    } else {
        //ไม่มีส่วนลด
        JLabel now = new JLabel("฿ " + THB.format(Math.round(p.getPrice())));
        now.setFont(now.getFont().deriveFont(Font.BOLD, 11f));
        pricePanel.add(now);
    }

    //ปุ่ม Add
    JButton add = new JButton("Add to Cart");
    add.setBackground(Color.BLACK);
    add.setForeground(Color.WHITE);
    add.setFocusPainted(false);
    add.setAlignmentX(Component.LEFT_ALIGNMENT);


    // ให้ปุ่มกว้าง “เต็มการ์ด”
    add.setPreferredSize(new Dimension(CARD_W - 2*P, BTN_H));
    add.setMaximumSize(new Dimension(CARD_W - 2*P, BTN_H));

    final JLabel qtyLabel = new JLabel();
    qtyLabel.setFont(qtyLabel.getFont().deriveFont(Font.BOLD, 10f));
    qtyLabel.setForeground(new Color(60,60,60));
    qtyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    int startQty = getQtyInCart(p.getSku());
    qtyLabel.setText("In cart: " + startQty);
    qtyLabel.setVisible(startQty > 0);
    skuToQtyLabel.put(p.getSku(), qtyLabel);

    //เพิ่มสินค้าลงตะกร้า
add.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // ต้องเลือกไซส์ก่อน
            javax.swing.JComboBox<String> cb = resolveSizeCombo((Component) e.getSource());
            if (cb == null || cb.getSelectedIndex() <= 0) {
                javax.swing.JOptionPane.showMessageDialog(MainFrame.this, "Please choose shoes size");
                if (cb != null) cb.requestFocus();
                return;
            }
            String chosenSize = String.valueOf(cb.getSelectedItem());
           int inCart = getQtyInCart(p.getSku());   // จำนวนที่มีอยู่ในตะกร้า
            int stock = p.getQuantity();             // จำนวนใน stock (มาจาก Product)

            // ตรวจว่ามี stock พอไหม
            if (inCart + 1 > stock) {
                javax.swing.UIManager.put("OptionPane.messageFont", new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));
                javax.swing.JOptionPane.showMessageDialog(
                    MainFrame.this,
                    "สินค้าหมด หรือเกินจำนวนที่มีในสต็อก (" + stock + " ชิ้น)",
                    "Out of stock",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            // เพิ่มลงตะกร้า + จดไซส์
            cart.addItem(p.getSku(), 1);
            skuToSize.put(p.getSku(), chosenSize);
            updatePurchaseButton();
            int q = getQtyInCart(p.getSku());
            JLabel lbl = skuToQtyLabel.get(p.getSku());
            if (lbl != null) {
                lbl.setText("In cart: " + q);
                lbl.setVisible(q > 0);
            }
            refreshCartListPanel();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(
                MainFrame.this,
                "Add to cart failed: " + ex.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
});


     box.add(img);
    box.add(Box.createVerticalStrut(6));
    box.add(brand);
    box.add(name);
    box.add(Box.createVerticalStrut(4));
    box.add(pricePanel);
    box.add(Box.createVerticalStrut(8));
    box.add(add);
    box.add(Box.createVerticalStrut(4));
    box.add(qtyLabel);
    card.add(box, BorderLayout.CENTER);
    return card;
        }

// อ่านทั้งไฟล์ (ข้าม header แถวแรก)
private List<String> readAllLinesSmart(Path path) throws IOException {
    byte[] data = Files.readAllBytes(path);

    // มี BOM → UTF-8 แน่นอน(อ่านภาษาไทยได้)
    if (data.length >= 3 && (data[0] & 0xFF) == 0xEF && (data[1] & 0xFF) == 0xBB && (data[2] & 0xFF) == 0xBF) {
        return Arrays.asList(new String(data, StandardCharsets.UTF_8).split("\\R"));
    }

    String asUtf8 = new String(data, StandardCharsets.UTF_8);
    if (asUtf8.indexOf('\uFFFD') >= 0) {
        return Files.readAllLines(path, java.nio.charset.Charset.forName("TIS-620")); // หรือ "x-windows-874"
    }
    return Arrays.asList(asUtf8.split("\\R"));
}

private List<UserProfile> readAllUsers() {
    try {
        if (!Files.exists(USERS_CSV)) return new ArrayList<>();
        List<String> lines = readAllLinesSmart(USERS_CSV);
        if (lines.isEmpty()) return new ArrayList<>();
        // กันกรณี header มี BOM ค้าง
        lines.set(0, lines.get(0).replace("\uFEFF", ""));

        List<UserProfile> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            String[] c = line.split(",", -1);
            if (c.length < 11) continue;

            UserProfile u = new UserProfile();
            u.username    = c[0].trim();
            u.password    = c[1].trim();
            u.email       = c[2].trim();
            u.fullname    = c[3].trim();
            u.house       = c[4].trim();
            u.subdistrict = c[5].trim();
            u.district    = c[6].trim();
            u.province    = c[7].trim();
            u.zipcode     = c[8].trim();
            u.phone       = c[9].trim();
            u.role        = c[10].trim();
            list.add(u);
        }
        return list;
    } catch (IOException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
// สร้างตัวแปรของที่อยู่ที่ต้องส่ง ตาม user
private String buildShippingAddress() {
    String full;
    String house;
    String sub;
    String dist;
    String prov;
    String zip;

    if (currentUserProfile != null && currentUserProfile.fullname != null && !currentUserProfile.fullname.trim().isEmpty()) {
        full = currentUserProfile.fullname;
    } else {
        full = nvl(jTextField1.getText());
    }

    if (currentUserProfile != null && currentUserProfile.house != null && !currentUserProfile.house.trim().isEmpty()) {
        house = currentUserProfile.house;
    } else {
        house = nvl(jTextField7.getText());
    }

    if (currentUserProfile != null && currentUserProfile.subdistrict != null && !currentUserProfile.subdistrict.trim().isEmpty()) {
        sub = currentUserProfile.subdistrict;
    } else {
        sub = nvl(jTextField8.getText());
    }

    if (currentUserProfile != null && currentUserProfile.district != null && !currentUserProfile.district.trim().isEmpty()) {
        dist = currentUserProfile.district;
    } else {
        dist = nvl(jTextField9.getText());
    }

    if (currentUserProfile != null && currentUserProfile.province != null && !currentUserProfile.province.trim().isEmpty()) {
        prov = currentUserProfile.province;
    } else {
        prov = nvl(jTextField12.getText());
    }

    if (currentUserProfile != null && currentUserProfile.zipcode != null && !currentUserProfile.zipcode.trim().isEmpty()) {
        zip = currentUserProfile.zipcode;
    } else {
        zip = nvl(jTextField11.getText());
    }

    StringBuilder sb = new StringBuilder();
    sb.append(full).append(",")
      .append(house).append(",")
      .append(sub).append(",")
      .append(dist).append(",")
      .append(prov).append(",")
      .append(zip);
    return sb.toString();
}

// เติมค่าลงในหน้า confirm
private void fillConfirmSummary(List<CartItem> snapshot, Map<String,String> sizeSnap, double grandTotal) {
    StringBuilder items = new StringBuilder();

    for (int i = 0; i < snapshot.size(); i++) {
        CartItem it = snapshot.get(i);
        Product pr = it.getProduct();

        String size = sizeSnap.get(pr.getSku());
        if (size == null || size.trim().isEmpty()) {
            size = "-";
        }

        double sub = pr.getPriceAfterBuiltInDiscount() * it.getQuantity();

        items.append(pr.getBrand()).append(" ").append(pr.getName())
             .append(" x").append(it.getQuantity())
             .append(" (Size: ").append(size).append(") – ฿ ")
             .append(THB.format(Math.round(sub)))
             .append("<br/>");
    }

    String totalLine = "<br/><b>Price :</b> ฿ " + THB.format(Math.round(grandTotal));

    jLabel74.setText("Order information");
    jLabel75.setText("<html>" + items.toString() + totalLine + "</html>");

    String email = "";
    if (currentUserProfile != null && currentUserProfile.email != null) {
        email = nvl(currentUserProfile.email);
    }
    jLabel76.setText("Email : " + email);

    jLabel77.setText("Shipping Address : " + buildShippingAddress());

    String phoneToShow = nvl(jTextField13.getText());
    jLabel78.setText("Phone number : " + phoneToShow);
}

// ✅ อ่าน stock.csv แล้วหักจำนวนตามที่ซื้อ; ถ้าจำนวนใหม่ = 0 จะลบแถวนั้นทิ้ง
private void reduceStockByCartAndClear() {
    Map<String, Integer> buyMap = new HashMap<>();
    for (CartItem it : cart.getItems()) {
        buyMap.merge(it.getProduct().getSku(), it.getQuantity(), Integer::sum);
    }
    if (buyMap.isEmpty()) return;

    try {
        if (!Files.exists(STOCK_CSV)) {
            JOptionPane.showMessageDialog(this, "ไม่พบไฟล์ stock.csv");
            return;
        }

        List<String> lines = Files.readAllLines(STOCK_CSV, StandardCharsets.UTF_8);
        if (lines.isEmpty()) return;

        String header = lines.get(0);
        String[] cols = header.split(",", -1);
        int idxSku = -1, idxQty = -1;

        // ✅ หา index ของคอลัมน์ (ไม่สนตัวพิมพ์เล็ก/ใหญ่ + ลบ ")
        for (int i = 0; i < cols.length; i++) {
            String c = cols[i].replace("\"", "").trim().toLowerCase();
            if (c.equals("sku") || c.equals("id")) idxSku = i;
            if (c.equals("quantity") || c.equals("qty") || c.equals("stock")) idxQty = i;
        }

        if (idxSku < 0 || idxQty < 0) {
            JOptionPane.showMessageDialog(this, "❌ stock.csv ต้องมีคอลัมน์ SKU และ Quantity/Qty/Stock");
            return;
        }

        List<String> out = new ArrayList<>();
        out.add(header); // ✅ header เดิมไม่ต้องแตะ

        // ✅ วนลูปแต่ละแถวเพื่อลดจำนวน
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] c = line.split(",", -1);
            if (c.length < cols.length) {
                out.add(line);
                continue;
            }

            // ✅ ลบ " ออกทุกช่อง
            for (int k = 0; k < c.length; k++) {
                c[k] = c[k].replace("\"", "").trim();
            }

            String sku = c[idxSku];
            Integer bought = buyMap.get(sku);
            if (bought == null) {
                out.add(formatCsvLine(c));
                continue;
            }

            int oldQty = 0;
            try {
                oldQty = Integer.parseInt(c[idxQty].trim());
            } catch (Exception ignore) {}

            int newQty = Math.max(0, oldQty - bought);

            // ถ้าเหลือ 0 ไม่เขียนแถวนั้นออกไป = ลบสินค้า
            if (newQty > 0) {
                c[idxQty] = String.valueOf(newQty);
                out.add(formatCsvLine(c));
            }
        }

        // เขียนกลับไฟล์ (พร้อมครอบ ")
        Files.write(STOCK_CSV, out, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "อัปเดตสต็อกไม่สำเร็จ: " + e.getMessage());
    }

    //  ล้างตะกร้า + อัปเดต UI
    cart.clearCart();
    for (JLabel lbl : skuToQtyLabel.values()) {
        lbl.setText("In cart: 0");
        lbl.setVisible(false);
    }
    skuToSize.clear();
    refreshCartListPanel();
    updateOrderSummary();
    updatePurchaseButton();
}

/*ช่วยฟังก์ชัน — ใส่เครื่องหมาย " ครอบทุกค่าในบรรทัด CSV */
private String formatCsvLine(String[] fields) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < fields.length; i++) {
        if (i > 0) sb.append(",");
        sb.append("\"").append(fields[i].replace("\"", "")).append("\"");
    }
    return sb.toString();
}


private void writeAllUsers(List<UserProfile> users) {
    try {
        List<String> out = new ArrayList<>();
        out.add("username,password,email,fullname,house,subdistrict,district,province,zipcode,phone,role");
        for (UserProfile u : users) {
            out.add(Arrays.stream(u.toArray())
                    .map(s -> s == null ? "" : s.replace(",", " "))
                    .collect(Collectors.joining(",")));
        }

        Path parent = USERS_CSV.getParent();
        if (parent != null) Files.createDirectories(parent);

        // เขียนแบบ UTF-8 + BOM
        try (BufferedWriter w = Files.newBufferedWriter(
                USERS_CSV, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            w.write('\uFEFF');                 // BOM
            for (String line : out) {
                w.write(line);
                w.newLine();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "บันทึก users.csv ไม่สำเร็จ: " + e.getMessage());
    }
}

// อ่าน users ทั้งหมดจาก users.csv แล้วหา "ผู้ใช้ที่ล็อกอินอยู่" จาก Session
private UserProfile findCurrentUser(List<UserProfile> all) {
    String sessionUsername = null;
    try {
        String[] sess = Session.loadSession();   // ← อยู่ package เดียวกัน page.Session
        if (sess != null && sess.length >= 1) {
            sessionUsername = sess[0];           // username จากไฟล์ session.csv
        }
    } catch (Exception ignore) {}

    if (sessionUsername != null) {
        for (UserProfile u : all) {
            if (sessionUsername.equalsIgnoreCase(u.username)) {
                return u;
            }
        }
    }
    // เผื่อกรณีไม่มี session/หาไม่เจอ
    return all.isEmpty() ? null : all.get(0);
}

// โหลดจากไฟล์ใส่ลงฟอร์ม
private void loadProfileToForm() {
    List<UserProfile> all = readAllUsers();
    currentUserProfile = findCurrentUser(all);
    if (currentUserProfile == null) return;

    jTextField1.setText(currentUserProfile.fullname);     // Full Name
    jTextField7.setText(currentUserProfile.house);        // Street / House No.
    jTextField8.setText(currentUserProfile.subdistrict);  // Sub-area
    jTextField9.setText(currentUserProfile.district);     // City
    jTextField12.setText(currentUserProfile.province);    // Province
    jTextField11.setText(currentUserProfile.zipcode);     // ZIP
    jTextField13.setText(currentUserProfile.phone);       // Phone
}

// เก็บค่าที่แก้จากฟอร์มกลับเข้า currentUserProfile แล้วเขียนลงไฟล์
private void saveProfileFromForm() {
    if (currentUserProfile == null) return;

    currentUserProfile.fullname    = nvl(jTextField1.getText());
    currentUserProfile.house       = nvl(jTextField7.getText());
    currentUserProfile.subdistrict = nvl(jTextField8.getText());
    currentUserProfile.district    = nvl(jTextField9.getText());
    currentUserProfile.province    = nvl(jTextField12.getText());
    currentUserProfile.zipcode     = nvl(jTextField11.getText());
    currentUserProfile.phone       = nvl(jTextField13.getText());

    List<UserProfile> all = readAllUsers();
    boolean replaced = false;
    for (int i = 0; i < all.size(); i++) {
        if (all.get(i).username.equalsIgnoreCase(currentUserProfile.username)) {
            all.set(i, currentUserProfile);
            replaced = true;
            break;
        }
    }
    if (!replaced) {
        all.add(currentUserProfile); // ถ้าไม่เจอ แทรกใหม่ (กันข้อมูลหาย)
    }
    writeAllUsers(all);
}

// เมธอดอัปเดตสถานะปุ่ม Purchase อัตโนมัติ
private void updatePurchaseButton() {
    if (cart.getItemCount() == 0) {
        jButton13.setEnabled(false);
        jButton13.setToolTipText("กรุณาเพิ่มสินค้าในตะกร้าก่อนทำการสั่งซื้อ");
    } else {
        jButton13.setEnabled(true);
        jButton13.setToolTipText(null);
    }
}

// ระบบค้นหาสินค้าตามชื่อหรือแบรนด์
private void applySearch(String keyword) {
    if (keyword == null) {
        JOptionPane.showMessageDialog(this, "กรุณากรอกคำที่ต้องการค้นหา");
        return;
    }

    // ลบช่องว่างทั้งหมดและเปลี่ยนเป็นตัวพิมพ์เล็ก
    final String searchKeyword = keyword.toLowerCase().trim().replaceAll("\\s+", "");
    if (searchKeyword.isEmpty()) {
        JOptionPane.showMessageDialog(this, "กรุณากรอกคำที่ต้องการค้นหา");
        return;
    }

    catalog.reload();
    List<Product> allProducts = catalog.getAllProducts();

    // รวม brand + name แล้วลบช่องว่างเพื่อค้นหา 
    List<Product> results = allProducts.stream()
            .filter(p -> {
                String brandNameCombo = (p.getBrand() + p.getName())
                        .toLowerCase()
                        .replaceAll("\\s+", "");
                return brandNameCombo.contains(searchKeyword);
            })
            .collect(Collectors.toList());

//  สร้างหน้า SearchResult
JPanel SearchResult = new JPanel(new BorderLayout());
SearchResult.setBackground(Color.WHITE);

// ===== แถบด้านบน (ผลลัพธ์ + ช่องเลือกไซส์) =====
JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
topPanel.setBackground(Color.WHITE);

// ป้ายข้อความผลลัพธ์
JLabel title = new JLabel("ผลลัพธ์การค้นหา: " + keyword);
title.setFont(new Font("Tahoma", Font.BOLD, 26));

// ช่องเลือกไซส์
sizeComboBox = new javax.swing.JComboBox<>(new String[]{
    "กรุณาเลือกขนาดรองเท้า", "36", "37", "38", "39", "40",
    "41", "42", "43", "44", "45"
});
sizeComboBox.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13));
sizeComboBox.setSelectedIndex(0);

JLabel sizeLabel = new JLabel("ขนาดรองเท้า:");
sizeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

// เพิ่มทั้งหมดในแถบด้านบน
topPanel.add(title);
topPanel.add(Box.createHorizontalStrut(30)); // เว้นช่องว่าง
topPanel.add(sizeLabel);
topPanel.add(sizeComboBox);

// เพิ่มแถบด้านบนเข้าไปใน SearchResult
SearchResult.add(topPanel, BorderLayout.NORTH);


    JPanel productPanel = new JPanel();
    productPanel.setBackground(Color.WHITE);
    ensureGridBag(productPanel);

    if (results.isEmpty()) {
        // ไม่พบสินค้า  แสดงข้อความแทน
        JLabel noResult = new JLabel("ไม่พบสินค้าที่ตรงกับ \"" + keyword + "\"", SwingConstants.CENTER);
        noResult.setFont(new Font("Tahoma", Font.PLAIN, 22));
        noResult.setForeground(Color.GRAY);
        productPanel.setLayout(new BorderLayout());
        productPanel.add(noResult, BorderLayout.CENTER);
    } else {
        // พบสินค้า  แสดงการ์ดสินค้าแบบเดิม
        populateGrid(productPanel, results);
    }

    JScrollPane scroll = new JScrollPane(productPanel);
    scroll.setBorder(null);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(24);
    scroll.getViewport().setBackground(Color.WHITE);
    scroll.setPreferredSize(new Dimension(0, 0));
    SearchResult.add(scroll, BorderLayout.CENTER);

    // ปุ่มกลับหน้าหลัก
    JPanel bottom = new JPanel();
    bottom.setBackground(Color.WHITE);
    JButton back = new JButton("← กลับหน้าหลัก");
    back.setFont(new Font("Tahoma", Font.BOLD, 16));
    back.setFocusPainted(false);
    back.addActionListener(e -> {
    motherpanel.removeAll();
    motherpanel.add(MainNew8);
    motherpanel.repaint();
    motherpanel.revalidate();
    showNew(); // โหลดสินค้าหน้า New ใหม่เพื่อให้ปุ่ม Add to Cart ทำงานได้ทันที
    });

    bottom.add(back);
    SearchResult.add(bottom, BorderLayout.SOUTH);
    motherpanel.removeAll();
    motherpanel.add(SearchResult);
    motherpanel.repaint();
    motherpanel.revalidate();
}



private static String nvl(String s) { return s == null ? "" : s.trim(); }
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setupNewArea(); setupMenArea(); setupWomenArea(); setupDealArea();
        setupCartPage(); setupPurchasePage(); setupConfirmPage();
        showNew();           // แสดงหน้า New ครั้งแรก (ดึงจากไฟล์)
        setLocationRelativeTo(null);           // จัดกึ่งกลาง 
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);   // เปิดมาแบบเต็มหน้าจอ
        refreshCartListPanel();   // ให้หน้า Cart (ฝั่งซ้าย) ว่างแบบ layout พร้อมใช้งาน และสรุปยอดเป็น 0

    }
    /**CART: ให้สรุปออเดอร์อยู่ฝั่งขวา */
private void setupCartPage() {
    Cart.removeAll();
    Cart.setLayout(new BorderLayout());

    // แถบหัวข้อ
    JPanel north = new JPanel(new BorderLayout());
    north.setOpaque(false);
    jLabel49.setBorder(javax.swing.BorderFactory.createEmptyBorder(12,16,8,16));
    north.add(jLabel49, BorderLayout.WEST);
    Cart.add(north, BorderLayout.NORTH);

    // ฝั่งซ้าย: รายการสินค้าในตะกร้า (ใส่ใน ScrollPane)
    jPanel1.setBackground(new java.awt.Color(250,253,255));
    JScrollPane itemsScroll = new JScrollPane(
            jPanel1,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    itemsScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(12,12,12,12));
    Cart.add(itemsScroll, BorderLayout.CENTER);

    // ฝั่งขวา: คอลัมน์สรุปออเดอร์ + กล่องคูปอง + ปุ่มซื้อ
    JPanel rightCol = new JPanel();
    rightCol.setOpaque(false);
    rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
    rightCol.setBorder(javax.swing.BorderFactory.createEmptyBorder(12,12,12,12));

    // จัดให้ชิดขวาและคุมความกว้างคงที่
    jPanel10.setAlignmentX(Component.RIGHT_ALIGNMENT);
    jTextField2.setAlignmentX(Component.RIGHT_ALIGNMENT);
    jButton13.setAlignmentX(Component.RIGHT_ALIGNMENT);
    jTextField2.setPreferredSize(new Dimension(295, 66));
    jTextField2.setMaximumSize(new Dimension(295, 66));
    jButton13.setPreferredSize(new Dimension(295, 66));
    jButton13.setMaximumSize(new Dimension(295, 66));

    rightCol.add(jPanel10);
    rightCol.add(Box.createVerticalStrut(10));
    rightCol.add(jTextField2);
    rightCol.add(Box.createVerticalStrut(10));
    rightCol.add(jButton13);
    rightCol.add(Box.createVerticalGlue());

    JPanel eastWrap = new JPanel(new BorderLayout());
    eastWrap.setOpaque(false);
    eastWrap.add(rightCol, BorderLayout.NORTH);       // ให้กลุ่มอยู่ชิดบน-ขวา
    eastWrap.setPreferredSize(new Dimension(360, 10)); // ล็อคความกว้างคอลัมน์ขวา
    Cart.add(eastWrap, BorderLayout.EAST);

    Cart.revalidate();
    Cart.repaint();
}
/** ลดสินค้าทีละ 1 ชิ้นสำหรับ sku ที่ระบุ แล้วรีเฟรชหน้าจอ */
private void removeOneFromCart(String sku) {
    try {
        cart.removeOne(sku);
        // อัปเดตป้ายจำนวนบนการ์ดสินค้า (ฝั่งหน้า New/Men/Women/Deal)
        int q = getQtyInCart(sku);
        JLabel lbl = skuToQtyLabel.get(sku);
        if (lbl != null) {
            lbl.setText("In cart: " + q);
            lbl.setVisible(q > 0);
        }
        if (q <= 0) skuToSize.remove(sku);   // เอาไซส์ออกเมื่อจำนวนเป็น 0
        refreshCartListPanel();


        // รีเฟรชฝั่งรายการ + สรุปยอด
        refreshCartListPanel();
    } catch (Exception ex) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Remove failed: " + ex.getMessage(),
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}

/** คืนจำนวนที่สินค้า sku นี้อยู่ในตะกร้า */
private int getQtyInCart(String sku) {
    for (CartItem it : cart.getItems()) {
        if (it.getProduct().getSku().equalsIgnoreCase(sku)) return it.getQuantity();
    }
    return 0;
}

/** วาดรายการฝั่งซ้ายของหน้า Cart (jPanel1) และอัปเดต Order Summary */
private void refreshCartListPanel() {
    jPanel1.removeAll();
    jPanel1.setLayout(new GridBagLayout());
    GridBagConstraints gc = new GridBagConstraints();
    gc.insets = new Insets(8, 8, 8, 8);
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.weightx = 1.0;

    int row = 0;
    for (CartItem it : cart.getItems()) {
    JPanel rowPanel = new JPanel(new BorderLayout());
    rowPanel.setBackground(Color.WHITE);
    rowPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(230, 230, 230)));

    String sku  = it.getProduct().getSku();
    String sizeTxt = skuToSize.get(sku);
    String name = it.getProduct().getBrand() + "  " + it.getProduct().getName()+ (sizeTxt != null ? "  (Size: " + sizeTxt + ")" : "");
    JLabel left = new JLabel(name);


    double sub = it.getProduct().getPriceAfterBuiltInDiscount() * it.getQuantity();
    javax.swing.JLabel rightLabel = new javax.swing.JLabel("x" + it.getQuantity() + "   ฿ " + THB.format(Math.round(sub)));
    rightLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    // === ปุ่มลดจำนวน –  ===
    javax.swing.JButton minus = new javax.swing.JButton(" - ");
    minus.setMargin(new Insets(1, 6, 1, 6));
    minus.setFocusable(false);
    minus.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(180,180,180)));
    minus.setBackground(Color.WHITE);
    minus.setToolTipText("Remove 1 Pair");
    final String skuFinal = sku;
    minus.addActionListener(new java.awt.event.ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        removeOneFromCart(skuFinal);
        updatePurchaseButton();
    }
});

    // กล่องขวา: label + ปุ่ม –
    JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
    rightBox.setOpaque(false);
    rightBox.add(rightLabel);
    rightBox.add(minus);

    rowPanel.add(left, BorderLayout.WEST);
    rowPanel.add(rightBox, BorderLayout.EAST);

    gc.gridx = 0; gc.gridy = row++;
    jPanel1.add(rowPanel, gc);
}


    // ดันช่องว่างด้านล่าง
    gc.weighty = 1; gc.gridy = row;
    jPanel1.add(Box.createVerticalGlue(), gc);

    jPanel1.revalidate();
    jPanel1.repaint();

    updateOrderSummary();
}

/** คำนวณยอดรวมแล้วอัปเดตป้ายในกล่อง Order Summary */
private void updateOrderSummary() {
    double subtotal = 0.0;
    for (CartItem it : cart.getItems()) {
        double unit = it.getProduct().getPriceAfterBuiltInDiscount();
        subtotal += unit * it.getQuantity();
    }

    // ยอดหลังหักโปรโมโค้ด (ถ้ามี) ที่ PricingService คิดไว้ในตะกร้า
    double totalAfterPromo = cart.getTotalPrice();

    // ส่วนลดจากโปรโมโค้ด = ก่อนลด - หลังลด (ไม่ติดลบ)
    double promoDiscount = subtotal - totalAfterPromo;
    if (promoDiscount < 0.0) {
        promoDiscount = 0.0;
    }

    double shipping = SHIPPING_FEE;
    double grand = totalAfterPromo + shipping;

    jLabel55.setText("+ " + THB.format(Math.round(subtotal)));      // Subtotal
    jLabel56.setText("+ " + THB.format(Math.round(shipping)));      // Shipping Fee = 50
    jLabel57.setText("- " + THB.format(Math.round(promoDiscount))); // Discount จากโค้ด
    jLabel58.setText(THB.format(Math.round(grand)));                // Grand Total
}


/**จัด Shipping Address ให้อยู่กลางจอ*/
private void setupPurchasePage() {
    Purchase.removeAll();
    Purchase.setLayout(new BorderLayout());

    // หัวข้อซ้ายบนคงเดิม
    JPanel north = new JPanel(new BorderLayout());
    north.setOpaque(false);
    north.add(jLabel59, BorderLayout.WEST);
    north.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 0, 20));
    Purchase.add(north, BorderLayout.NORTH);

    // กลางจอ
    JPanel centerWrap = new JPanel(new GridBagLayout());
    centerWrap.setOpaque(false);

    JPanel content = new JPanel();
    content.setOpaque(false);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

    jPanel11.setAlignmentX(Component.CENTER_ALIGNMENT); // ฟอร์ม
    jButton16.setAlignmentX(Component.CENTER_ALIGNMENT); // ปุ่ม Confirm
    jButton16.setPreferredSize(new Dimension(400, 35));
    jButton16.setMaximumSize(new Dimension(400, 35));

    content.add(jPanel11);
    content.add(Box.createVerticalStrut(24));
    content.add(jButton16);

    centerWrap.add(content, new GridBagConstraints());
    Purchase.add(centerWrap, BorderLayout.CENTER);

    Purchase.revalidate();
    Purchase.repaint();
}

/** CONFIRM: จัดการ์ด Order Completed */
private void setupConfirmPage() {
    Confirm.removeAll();
    Confirm.setLayout(new BorderLayout());

    // แถบหัวข้อด้านบนเหมือนเดิม
    JPanel north = new JPanel(new BorderLayout());
    north.setOpaque(false);
    north.add(jLabel60, BorderLayout.WEST);
    north.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 0, 20));
    Confirm.add(north, BorderLayout.NORTH);

    // เอาขนาดคงที่ออก ให้สูงได้เท่าที่ต้องการ
    jPanel15.setPreferredSize(null);
    jPanel15.setAlignmentX(Component.CENTER_ALIGNMENT);
    jButton14.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel content = new JPanel();
    content.setOpaque(false);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.add(Box.createVerticalStrut(10));
    content.add(jPanel15);
    content.add(Box.createVerticalStrut(20));
    content.add(jButton14);
    content.add(Box.createVerticalStrut(10));

    // จัดให้อยู่กลางแนวนอน แต่สามารถเลื่อนแนวตั้งได้
    JPanel centerWrap = new JPanel(new GridBagLayout());
    centerWrap.setOpaque(false);
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 0;
    gc.gridy = 0;
    gc.weightx = 1;
    gc.anchor = GridBagConstraints.NORTH;
    centerWrap.add(content, gc);
    gc.gridy = 1;
    gc.weighty = 1;
    centerWrap.add(Box.createVerticalGlue(), gc);
    // เพิ่มให้เลื่อนดูข้อมูลได้
    JScrollPane sp = new JScrollPane(
            centerWrap,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    sp.setBorder(null);
    sp.getVerticalScrollBar().setUnitIncrement(24);
    Confirm.add(sp, BorderLayout.CENTER);

    Confirm.revalidate();
    Confirm.repaint();
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jLabel61 = new javax.swing.JLabel();
        motherpanel = new javax.swing.JPanel();
        MainNew8 = new javax.swing.JPanel();
        ProductNew = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        NewShowProduct = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        FilterNew = new javax.swing.JPanel();
        NewB10 = new javax.swing.JButton();
        NewB11 = new javax.swing.JButton();
        jCheckBox41 = new javax.swing.JCheckBox();
        jCheckBox42 = new javax.swing.JCheckBox();
        jCheckBox44 = new javax.swing.JCheckBox();
        jCheckBox45 = new javax.swing.JCheckBox();
        NewB12 = new javax.swing.JButton();
        NewB13 = new javax.swing.JButton();
        jCheckBox56 = new javax.swing.JCheckBox();
        jCheckBox57 = new javax.swing.JCheckBox();
        jCheckBox58 = new javax.swing.JCheckBox();
        jCheckBox59 = new javax.swing.JCheckBox();
        jCheckBox60 = new javax.swing.JCheckBox();
        MainMen = new javax.swing.JPanel();
        ProductMen = new javax.swing.JPanel();
        NewShowProduct2 = new javax.swing.JPanel();
        NewShowProduct3 = new javax.swing.JPanel();
        NewShowProduct4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        FilterMen = new javax.swing.JPanel();
        NewB6 = new javax.swing.JButton();
        NewB7 = new javax.swing.JButton();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        NewB8 = new javax.swing.JButton();
        NewB9 = new javax.swing.JButton();
        jCheckBox36 = new javax.swing.JCheckBox();
        jCheckBox37 = new javax.swing.JCheckBox();
        jCheckBox38 = new javax.swing.JCheckBox();
        jCheckBox39 = new javax.swing.JCheckBox();
        jCheckBox40 = new javax.swing.JCheckBox();
        MainWomen = new javax.swing.JPanel();
        ProductWomen = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        FilterWomen = new javax.swing.JPanel();
        NewB14 = new javax.swing.JButton();
        NewB15 = new javax.swing.JButton();
        jCheckBox61 = new javax.swing.JCheckBox();
        jCheckBox62 = new javax.swing.JCheckBox();
        jCheckBox64 = new javax.swing.JCheckBox();
        jCheckBox65 = new javax.swing.JCheckBox();
        NewB16 = new javax.swing.JButton();
        NewB17 = new javax.swing.JButton();
        jCheckBox76 = new javax.swing.JCheckBox();
        jCheckBox77 = new javax.swing.JCheckBox();
        jCheckBox78 = new javax.swing.JCheckBox();
        jCheckBox79 = new javax.swing.JCheckBox();
        jCheckBox80 = new javax.swing.JCheckBox();
        MainSD = new javax.swing.JPanel();
        ProductSD = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        FilterSD = new javax.swing.JPanel();
        NewB18 = new javax.swing.JButton();
        NewB19 = new javax.swing.JButton();
        jCheckBox81 = new javax.swing.JCheckBox();
        jCheckBox82 = new javax.swing.JCheckBox();
        jCheckBox84 = new javax.swing.JCheckBox();
        jCheckBox85 = new javax.swing.JCheckBox();
        NewB20 = new javax.swing.JButton();
        NewB21 = new javax.swing.JButton();
        jCheckBox96 = new javax.swing.JCheckBox();
        jCheckBox97 = new javax.swing.JCheckBox();
        jCheckBox98 = new javax.swing.JCheckBox();
        jCheckBox99 = new javax.swing.JCheckBox();
        jCheckBox100 = new javax.swing.JCheckBox();
        Cart = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        Purchase = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jTextField12 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        Confirm = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        MenuNew11 = new javax.swing.JPanel();
        Title17 = new javax.swing.JLabel();
        NewBnew11 = new javax.swing.JButton();
        NewBmen11 = new javax.swing.JButton();
        NewBwomen11 = new javax.swing.JButton();
        NewBsp11 = new javax.swing.JButton();
        Newsearch11 = new javax.swing.JTextField();
        newBsearch11 = new javax.swing.JButton();
        NewBlogout = new javax.swing.JButton();
        NewBcart11 = new javax.swing.JButton();
        NewBorder11 = new javax.swing.JButton();
        sizeComboBox = new javax.swing.JComboBox<>();
        sizeComboBoxMen = new javax.swing.JComboBox<>();
        sizeComboBoxWomen = new javax.swing.JComboBox<>();
        sizeComboBoxSD = new javax.swing.JComboBox<>();

        jLabel61.setText("jLabel61");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MainPanel");
        setBackground(new java.awt.Color(0, 0, 0));
        // ขนาดหน้าต่าง
        setMinimumSize(new java.awt.Dimension(1100, 700));  
        setPreferredSize(new java.awt.Dimension(1100, 700));
        setResizable(true);

        motherpanel.setPreferredSize(new java.awt.Dimension(900, 530));
        motherpanel.setLayout(new java.awt.CardLayout());

        //หน้าNew
        MainNew8.setMaximumSize(new java.awt.Dimension(920, 573));
        MainNew8.setPreferredSize(new java.awt.Dimension(900, 530));

        ProductNew.setBackground(new java.awt.Color(249, 249, 249));
        ProductNew.setPreferredSize(new java.awt.Dimension(708, 490));
        
        NewShowProduct.setBackground(new java.awt.Color(234, 230, 223)); //พื้นหลังหน้าNew
        java.awt.GridBagLayout NewShowProductLayout = new java.awt.GridBagLayout();
        NewShowProductLayout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0};
        NewShowProductLayout.rowHeights = new int[] {0, 12, 0};
        NewShowProduct.setLayout(NewShowProductLayout);

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(0, 0, 0));
        jTextField4.setFont(new java.awt.Font("Segoe UI", 3, 62)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setText("New");
        jTextField4.setFocusable(false);
        jTextField4.setRequestFocusEnabled(false);
        jTextField4.setVerifyInputWhenFocusTarget(false);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });


        FilterNew.setBackground(new java.awt.Color(249, 249, 249));
        NewB10.setBackground(new java.awt.Color(249, 249, 249));
        NewB10.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        NewB10.setText("Filter");
        NewB10.setBorder(null);
        NewB10.setBorderPainted(false);
        NewB10.setContentAreaFilled(false);
        NewB10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB10ActionPerformed(evt);
            }
        });

        NewB11.setBackground(new java.awt.Color(249, 249, 249));
        NewB11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB11.setText("Brand");
        NewB11.setBorder(null);
        NewB11.setBorderPainted(false);
        NewB11.setContentAreaFilled(false);
        NewB11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB11ActionPerformed(evt);
            }
        });

        jCheckBox41.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox41.setText("Adidas");

        jCheckBox42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox42.setText("Converse");

        jCheckBox44.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox44.setText("Nike");

        jCheckBox45.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox45.setText("Puma");

        NewB12.setBackground(new java.awt.Color(249, 249, 249));
        NewB12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB12.setText("Size");
        NewB12.setBorder(null);
        NewB12.setBorderPainted(false);
        NewB12.setContentAreaFilled(false);
        NewB12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB12ActionPerformed(evt);
            }
        });
        
        sizeComboBox.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12)); // NOI18N
        sizeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "กรุณาเลือกขนาดรองเท้า","36", "37", "38", "39", "40", "41", "42", "43", "44", "45" }
        ));
        sizeComboBox.setSelectedIndex(0);

        NewB13.setBackground(new java.awt.Color(249, 249, 249));
        NewB13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB13.setText("Price");
        NewB13.setBorder(null);
        NewB13.setBorderPainted(false);
        NewB13.setContentAreaFilled(false);
        NewB13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB13ActionPerformed(evt);
            }
        });

        jCheckBox56.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox56.setText("1,000 - 1,999 ฿");

        jCheckBox57.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox57.setText("2,000 - 2,999 ฿");

        jCheckBox58.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox58.setText("3,000 - 3,999 ฿");

        jCheckBox59.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox59.setText("4,000 - 4,999 ฿");

        jCheckBox60.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox60.setText("5,000 - 5,999 ฿");

        
        javax.swing.GroupLayout FilterNewLayout = new javax.swing.GroupLayout(FilterNew);
        FilterNew.setLayout(FilterNewLayout);
        FilterNewLayout.setHorizontalGroup(
            FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterNewLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NewB10)
                    .addComponent(NewB11)
                    .addComponent(jCheckBox41)
                    .addComponent(jCheckBox42)
                    .addComponent(jCheckBox44)
                    .addComponent(jCheckBox45)
                    .addComponent(NewB12)
                    .addComponent(sizeComboBox)
                        .addGap(51, 51, 51)
                    .addComponent(NewB13)
                    .addComponent(jCheckBox56)
                    .addComponent(jCheckBox57)
                    .addComponent(jCheckBox58)
                    .addComponent(jCheckBox59)
                    .addComponent(jCheckBox60))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        FilterNewLayout.setVerticalGroup(
            FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterNewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewB10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox58)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox60)
                .addComponent(NewB12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // เพิ่มตรงนี้
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                
        );

        javax.swing.GroupLayout MainNew8Layout = new javax.swing.GroupLayout(MainNew8);
        MainNew8.setLayout(MainNew8Layout);
        MainNew8Layout.setHorizontalGroup(
            MainNew8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainNew8Layout.createSequentialGroup()
                .addComponent(FilterNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProductNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MainNew8Layout.setVerticalGroup(
        MainNew8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(MainNew8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(MainNew8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(FilterNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ProductNew, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)) 
            .addContainerGap()) 
        );
        //เรียกใช้filter
        for (Component comp : FilterNew.getComponents()) {
            if (comp instanceof JCheckBox cb) {
                cb.addActionListener(e -> applyFilterNew());
            }
        }
        motherpanel.add(MainNew8, "card3");

       
       
       
       
       //ของหน้าMen
        MainMen.setPreferredSize(new java.awt.Dimension(900, 530));

        ProductMen.setBackground(new java.awt.Color(249, 249, 249));
        ProductMen.setPreferredSize(new java.awt.Dimension(708, 490));

        NewShowProduct2.setBackground(new java.awt.Color(234, 230, 223)); //พื้นหลังหน้าMen
        java.awt.GridBagLayout NewShowProduct2Layout = new java.awt.GridBagLayout();
        NewShowProduct2Layout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0};
        NewShowProduct2Layout.rowHeights = new int[] {0, 12, 0};
        NewShowProduct2.setLayout(NewShowProduct2Layout);

       jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(0, 0, 0));
        jTextField3.setFont(new java.awt.Font("Segoe UI", 3, 62)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("Men");
        jTextField3.setFocusable(false);
        jTextField3.setRequestFocusEnabled(false);
        jTextField3.setVerifyInputWhenFocusTarget(false);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });



        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(12, 12, 12))
        );

        FilterMen.setBackground(new java.awt.Color(249, 249, 249));

        NewB6.setBackground(new java.awt.Color(249, 249, 249));
        NewB6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        NewB6.setText("Filter");
        NewB6.setBorder(null);
        NewB6.setBorderPainted(false);
        NewB6.setContentAreaFilled(false);
        NewB6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB6ActionPerformed(evt);
            }
        });

        NewB7.setBackground(new java.awt.Color(249, 249, 249));
        NewB7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB7.setText("Brand");
        NewB7.setBorder(null);
        NewB7.setBorderPainted(false);
        NewB7.setContentAreaFilled(false);
        NewB7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB7ActionPerformed(evt);
            }
        });

        jCheckBox21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox21.setText("Adidas");

        jCheckBox22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox22.setText("Converse");

        jCheckBox24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox24.setText("Nike");

        jCheckBox25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox25.setText("Puma");

        NewB8.setBackground(new java.awt.Color(249, 249, 249));
        NewB8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB8.setText("Size");
        NewB8.setBorder(null);
        NewB8.setBorderPainted(false);
        NewB8.setContentAreaFilled(false);
        NewB8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB8ActionPerformed(evt);
            }
        });
        
        sizeComboBoxMen.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
        sizeComboBoxMen.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "กรุณาเลือกขนาดรองเท้า","36", "37", "38", "39", "40", "41", "42", "43", "44", "45" }
        ));
        sizeComboBoxMen.setSelectedIndex(0);


        NewB9.setBackground(new java.awt.Color(249, 249, 249));
        NewB9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB9.setText("Price");
        NewB9.setBorder(null);
        NewB9.setBorderPainted(false);
        NewB9.setContentAreaFilled(false);
        NewB9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB9ActionPerformed(evt);
            }
        });

        jCheckBox36.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox36.setText("1,000 - 1,999 ฿");

        jCheckBox37.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox37.setText("2,000 - 2,999 ฿");

        jCheckBox38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox38.setText("3,000 - 3,999 ฿");

        jCheckBox39.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox39.setText("4,000 - 4,999 ฿");

        jCheckBox40.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox40.setText("5,000 - 5,999 ฿");


        javax.swing.GroupLayout FilterMenLayout = new javax.swing.GroupLayout(FilterMen);
        FilterMen.setLayout(FilterMenLayout);
        FilterMenLayout.setHorizontalGroup(
            FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterMenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NewB6)
                    .addComponent(NewB7)
                    .addComponent(jCheckBox21)
                    .addComponent(jCheckBox22)
                    .addComponent(jCheckBox24)
                    .addComponent(jCheckBox25)
                    .addComponent(NewB8)
                    .addComponent(sizeComboBoxMen)
                    .addComponent(NewB9)
                    .addComponent(jCheckBox36)
                    .addComponent(jCheckBox37)
                    .addComponent(jCheckBox38)
                    .addComponent(jCheckBox39)
                    .addComponent(jCheckBox40))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        FilterMenLayout.setVerticalGroup(
            FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterMenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewB6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox40)
                .addComponent(NewB8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sizeComboBoxMen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            
        );

        javax.swing.GroupLayout MainMenLayout = new javax.swing.GroupLayout(MainMen);
        MainMen.setLayout(MainMenLayout);
        MainMenLayout.setHorizontalGroup(
            MainMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainMenLayout.createSequentialGroup()
                .addComponent(FilterMen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProductMen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MainMenLayout.setVerticalGroup(
            MainMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainMenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FilterMen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ProductMen, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
                .addContainerGap())
        );
         //เรียกใช้filter
        for (Component comp : FilterMen.getComponents()) {
                if (comp instanceof JCheckBox cb)
                    cb.addActionListener(e -> applyFilterMen());
                }
        motherpanel.add(MainMen, "card2");

        
        
        
        //หน้าwomen
        MainWomen.setPreferredSize(new java.awt.Dimension(900, 515));

        ProductWomen.setBackground(new java.awt.Color(249, 249, 249));
        ProductWomen.setPreferredSize(new java.awt.Dimension(708, 490));


        NewShowProduct3.setBackground(new java.awt.Color(234, 230, 223)); //พิ้นหลังหน้าWomen
        java.awt.GridBagLayout NewShowProduct3Layout = new java.awt.GridBagLayout();
        NewShowProduct3Layout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0};
        NewShowProduct3Layout.rowHeights = new int[] {0, 12, 0};
        NewShowProduct3.setLayout(NewShowProduct3Layout);

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(0, 0, 0));
        jTextField5.setFont(new java.awt.Font("Segoe UI", 3, 62)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(255, 255, 255));
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("Women");
        jTextField5.setFocusable(false);
        jTextField5.setRequestFocusEnabled(false);
        jTextField5.setVerifyInputWhenFocusTarget(false);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });


        javax.swing.GroupLayout ProductWomenLayout = new javax.swing.GroupLayout(ProductWomen);
        ProductWomen.setLayout(ProductWomenLayout);
        ProductWomenLayout.setHorizontalGroup(
            ProductWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductWomenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                .addContainerGap())
        );
        ProductWomenLayout.setVerticalGroup(
            ProductWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductWomenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FilterWomen.setBackground(new java.awt.Color(249, 249, 249));

        NewB14.setBackground(new java.awt.Color(249, 249, 249));
        NewB14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        NewB14.setText("Filter");
        NewB14.setBorder(null);
        NewB14.setBorderPainted(false);
        NewB14.setContentAreaFilled(false);
        NewB14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB14ActionPerformed(evt);
            }
        });

        NewB15.setBackground(new java.awt.Color(249, 249, 249));
        NewB15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB15.setText("Brand");
        NewB15.setBorder(null);
        NewB15.setBorderPainted(false);
        NewB15.setContentAreaFilled(false);
        NewB15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB15ActionPerformed(evt);
            }
        });

        jCheckBox61.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox61.setText("Adidas");

        jCheckBox62.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox62.setText("Converse");

        jCheckBox64.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox64.setText("Nike");

        jCheckBox65.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox65.setText("Puma");

        NewB16.setBackground(new java.awt.Color(249, 249, 249));
        NewB16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB16.setText("Size");
        NewB16.setBorder(null);
        NewB16.setBorderPainted(false);
        NewB16.setContentAreaFilled(false);
        NewB16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB16ActionPerformed(evt);
            }
        });

        sizeComboBoxWomen.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
        sizeComboBoxWomen.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "กรุณาเลือกขนาดรองเท้า","36", "37", "38", "39", "40", "41", "42", "43", "44", "45" }
        ));
        sizeComboBoxWomen.setSelectedIndex(0);


        NewB17.setBackground(new java.awt.Color(249, 249, 249));
        NewB17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB17.setText("Price");
        NewB17.setBorder(null);
        NewB17.setBorderPainted(false);
        NewB17.setContentAreaFilled(false);
        NewB17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB17ActionPerformed(evt);
            }
        });

        jCheckBox76.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox76.setText("1,000 - 1,999 ฿");

        jCheckBox77.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox77.setText("2,000 - 2,999 ฿");

        jCheckBox78.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox78.setText("3,000 - 3,999 ฿");

        jCheckBox79.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox79.setText("4,000 - 4,999 ฿");

        jCheckBox80.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox80.setText("5,000 - 5,999 ฿");

        javax.swing.GroupLayout FilterWomenLayout = new javax.swing.GroupLayout(FilterWomen);
        FilterWomen.setLayout(FilterWomenLayout);
        FilterWomenLayout.setHorizontalGroup(
            FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterWomenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NewB14)
                    .addComponent(NewB15)
                    .addComponent(jCheckBox61)
                    .addComponent(jCheckBox62)
                    .addComponent(jCheckBox64)
                    .addComponent(jCheckBox65)
                    .addComponent(NewB16)
                    .addComponent(sizeComboBoxWomen)
                    .addComponent(NewB17)
                    .addComponent(jCheckBox76)
                    .addComponent(jCheckBox77)
                    .addComponent(jCheckBox78)
                    .addComponent(jCheckBox79)
                    .addComponent(jCheckBox80))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        FilterWomenLayout.setVerticalGroup(
            FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterWomenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewB14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox61)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox62)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox76)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox77)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox78)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox79)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox80)
                .addComponent(NewB16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sizeComboBoxWomen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MainWomenLayout = new javax.swing.GroupLayout(MainWomen);
        MainWomen.setLayout(MainWomenLayout);
        MainWomenLayout.setHorizontalGroup(
            MainWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainWomenLayout.createSequentialGroup()
                .addComponent(FilterWomen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProductWomen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MainWomenLayout.setVerticalGroup(
            MainWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainWomenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FilterWomen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ProductWomen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        //เรียกใช้filter
        for (Component comp : FilterWomen.getComponents()) {
            if (comp instanceof JCheckBox cb)
                cb.addActionListener(e -> applyFilterWomen());
        }
        motherpanel.add(MainWomen, "card4");



        //หน้าSpecial deal

        MainSD.setPreferredSize(new java.awt.Dimension(900, 515));

        ProductSD.setBackground(new java.awt.Color(249, 249, 249));

        NewShowProduct4.setBackground(new java.awt.Color(234, 230, 223)); //พิ้นหลังหน้าSpecial
        java.awt.GridBagLayout NewShowProduct4Layout = new java.awt.GridBagLayout();
        NewShowProduct4Layout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0};
        NewShowProduct4Layout.rowHeights = new int[] {0, 12, 0};
        NewShowProduct4.setLayout(NewShowProduct4Layout);


        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(0, 0, 0));
        jTextField6.setFont(new java.awt.Font("Segoe UI", 3, 62)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(255, 255, 255));
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setText("Special Deal");
        jTextField6.setFocusable(false);
        jTextField6.setRequestFocusEnabled(false);
        jTextField6.setVerifyInputWhenFocusTarget(false);
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ProductSDLayout = new javax.swing.GroupLayout(ProductSD);
        ProductSD.setLayout(ProductSDLayout);
        ProductSDLayout.setHorizontalGroup(
            ProductSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductSDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                .addContainerGap())
        );
        ProductSDLayout.setVerticalGroup(
            ProductSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductSDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FilterSD.setBackground(new java.awt.Color(249, 249, 249));

        NewB18.setBackground(new java.awt.Color(249, 249, 249));
        NewB18.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        NewB18.setText("Filter");
        NewB18.setBorder(null);
        NewB18.setBorderPainted(false);
        NewB18.setContentAreaFilled(false);
        NewB18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB18ActionPerformed(evt);
            }
        });

        NewB19.setBackground(new java.awt.Color(249, 249, 249));
        NewB19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB19.setText("Brand");
        NewB19.setBorder(null);
        NewB19.setBorderPainted(false);
        NewB19.setContentAreaFilled(false);
        NewB19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB19ActionPerformed(evt);
            }
        });

        jCheckBox81.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox81.setText("Adidas");

        jCheckBox82.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox82.setText("Converse");

        jCheckBox84.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox84.setText("Nike");

        jCheckBox85.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox85.setText("Puma");

        NewB20.setBackground(new java.awt.Color(249, 249, 249));
        NewB20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB20.setText("Size");
        NewB20.setBorder(null);
        NewB20.setBorderPainted(false);
        NewB20.setContentAreaFilled(false);
        NewB20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB20ActionPerformed(evt);
            }
        });

        sizeComboBoxSD.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
        sizeComboBoxSD.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "กรุณาเลือกขนาดรองเท้า","36", "37", "38", "39", "40", "41", "42", "43", "44", "45" }
        ));
        sizeComboBoxSD.setSelectedIndex(0);

        NewB21.setBackground(new java.awt.Color(249, 249, 249));
        NewB21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewB21.setText("Price");
        NewB21.setBorder(null);
        NewB21.setBorderPainted(false);
        NewB21.setContentAreaFilled(false);
        NewB21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewB21ActionPerformed(evt);
            }
        });

        jCheckBox96.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox96.setText("1,000 - 1,999 ฿");

        jCheckBox97.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox97.setText("2,000 - 2,999 ฿");

        jCheckBox98.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox98.setText("3,000 - 3,999 ฿");

        jCheckBox99.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox99.setText("4,000 - 4,999 ฿");

        jCheckBox100.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox100.setText("5,000 - 5,999 ฿");

        javax.swing.GroupLayout FilterSDLayout = new javax.swing.GroupLayout(FilterSD);
        FilterSD.setLayout(FilterSDLayout);
        FilterSDLayout.setHorizontalGroup(
            FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterSDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NewB18)
                    .addComponent(NewB19)
                    .addComponent(jCheckBox81)
                    .addComponent(jCheckBox82)
                    .addComponent(jCheckBox84)
                    .addComponent(jCheckBox85)
                    .addComponent(NewB20)
                    .addComponent(sizeComboBoxSD)
                    .addComponent(NewB21)
                    .addComponent(jCheckBox96)
                    .addComponent(jCheckBox97)
                    .addComponent(jCheckBox98)
                    .addComponent(jCheckBox99)
                    .addComponent(jCheckBox100))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        FilterSDLayout.setVerticalGroup(
            FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterSDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewB18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox81)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox82)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox84)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox85)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox96)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox97)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox98)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox99)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox100)
                .addComponent(NewB20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sizeComboBoxSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MainSDLayout = new javax.swing.GroupLayout(MainSD);
        MainSD.setLayout(MainSDLayout);
        MainSDLayout.setHorizontalGroup(
            MainSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainSDLayout.createSequentialGroup()
                .addComponent(FilterSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProductSD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MainSDLayout.setVerticalGroup(
            MainSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainSDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FilterSD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ProductSD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        //เรียกใช้filter
        for (Component comp : FilterSD.getComponents()) {
            if (comp instanceof JCheckBox cb)
                cb.addActionListener(e -> applyFilterDeal());
        }
        motherpanel.add(MainSD, "card4");



        Cart.setBackground(new java.awt.Color(255, 255, 255));
        Cart.setPreferredSize(new java.awt.Dimension(900, 515));

        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel49.setText("Cart");

        jPanel1.setBackground(new java.awt.Color(250, 253, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Order Summary");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Subtotal");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Shipping Fee");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Discount");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Grand Total");

        jLabel55.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("+ 0.00");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("+ 0.00");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("- 0.00");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("0.00");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel54)
                                .addGap(88, 88, 88)
                                .addComponent(jLabel58))
                            .addComponent(jLabel50))
                        .addGap(26, 39, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jLabel52)
                            .addComponent(jLabel53))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57)
                            .addComponent(jLabel56)
                            .addComponent(jLabel55))
                        .addGap(30, 30, 30))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel50)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jLabel55))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel56))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jLabel58))
                .addGap(19, 19, 19))
        );
        // สีเทาสำหรับ placeholder ถูกตั้งไว้แล้วด้านบน
jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
    @Override
    public void focusGained(java.awt.event.FocusEvent e) {
        if (PROMO_PLACEHOLDER.equals(jTextField2.getText())) {
            jTextField2.setText("");
            jTextField2.setForeground(java.awt.Color.BLACK); // พร้อมพิมพ์
        }
    }
    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        if (jTextField2.getText().trim().isEmpty()) {
            jTextField2.setText(PROMO_PLACEHOLDER);
            jTextField2.setForeground(new java.awt.Color(204,204,204)); // กลับเป็น placeholder
        }
    }
});

        jTextField2.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(204, 204, 204));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Enter code...");
        jTextField2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(0, 0, 0));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Purchase");
        // 🔒 ปิดปุ่ม Purchase ถ้ายังไม่มีสินค้าในตะกร้า
        if (cart.getItemCount() == 0) {
            jButton13.setEnabled(false);
        }

        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            
            }
        });

        javax.swing.GroupLayout CartLayout = new javax.swing.GroupLayout(Cart);
        Cart.setLayout(CartLayout);
        CartLayout.setHorizontalGroup(
            CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CartLayout.createSequentialGroup()
                .addGroup(CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CartLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                                .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CartLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel49)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CartLayout.setVerticalGroup(
            CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CartLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(CartLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        motherpanel.add(Cart, "card6");

        Purchase.setBackground(new java.awt.Color(255, 255, 255));
        Purchase.setPreferredSize(new java.awt.Dimension(900, 515));

        jLabel59.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel59.setText("Purchase");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jTextField12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField12.setText("Province");

        jTextField11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField11.setText("ZIP Code");

        jTextField9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField9.setText("City");

        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField8.setText("Sub-area");

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField7.setText("Street Address / House No.");

        jTextField13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField13.setText("Phone Number");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField1.setText("Full Name");

        jLabel69.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel69.setText("Shipping Address");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField7)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        jButton16.setBackground(new java.awt.Color(0, 0, 0));
        jButton16.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("Confirm ");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
                updatePurchaseButton();
            }
        });

        javax.swing.GroupLayout PurchaseLayout = new javax.swing.GroupLayout(Purchase);
        Purchase.setLayout(PurchaseLayout);
        PurchaseLayout.setHorizontalGroup(
            PurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PurchaseLayout.createSequentialGroup()
                .addGroup(PurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PurchaseLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel59))
                    .addGroup(PurchaseLayout.createSequentialGroup()
                        .addGap(238, 238, 238)
                        .addGroup(PurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(262, Short.MAX_VALUE))
        );
        PurchaseLayout.setVerticalGroup(
            PurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PurchaseLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel59)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        motherpanel.add(Purchase, "card6");

        Confirm.setBackground(new java.awt.Color(255, 255, 255));
        Confirm.setPreferredSize(new java.awt.Dimension(900, 515));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel60.setText("Confirm");

        jPanel15.setBackground(new java.awt.Color(248, 248, 248));
        jPanel15.setPreferredSize(new java.awt.Dimension(810, 350));

        jLabel72.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(0, 204, 51));
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("Order completed!");

        jLabel73.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(102, 102, 102));
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("Thank you for ordering from our store. We will prepare and ship the product as soon as possible.");

        jLabel74.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("Order information ");

        jLabel75.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("Total price ");

        jLabel76.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("Email");

        jLabel77.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel77.setText("Shipping Address");

        jLabel78.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel78.setText("Phone number");

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Home/1024px-Antu_task-complete.svg (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel72, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel75)
                            .addComponent(jLabel76)
                            .addComponent(jLabel77)
                            .addComponent(jLabel78))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel74)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel72)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel73)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel74)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel75)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel76)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel77)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel78)
                .addGap(53, 53, 53))
        );

        jButton14.setBackground(new java.awt.Color(0, 0, 0));
        jButton14.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("Go back to home page");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ConfirmLayout = new javax.swing.GroupLayout(Confirm);
        Confirm.setLayout(ConfirmLayout);
        ConfirmLayout.setHorizontalGroup(
            ConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConfirmLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addGroup(ConfirmLayout.createSequentialGroup()
                .addGroup(ConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ConfirmLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel60))
                    .addGroup(ConfirmLayout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ConfirmLayout.setVerticalGroup(
            ConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfirmLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel60)
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton14)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        motherpanel.add(Confirm, "card6");

        getContentPane().add(motherpanel, java.awt.BorderLayout.CENTER);

        MenuNew11.setBackground(new java.awt.Color(249, 249, 249));

        Title17.setFont(new java.awt.Font("Segoe UI Black", 0, 20)); // NOI18N
        Title17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Title17.setText("Nutthapong");

        NewBnew11.setBackground(new java.awt.Color(249, 249, 249));
        NewBnew11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NewBnew11.setText("New");
        NewBnew11.setBorder(null);
        NewBnew11.setBorderPainted(false);
        NewBnew11.setContentAreaFilled(false);
        NewBnew11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewBnew11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewBnew11ActionPerformed(evt);
            }
        });

        NewBmen11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NewBmen11.setText("Men");
        NewBmen11.setBorder(null);
        NewBmen11.setBorderPainted(false);
        NewBmen11.setContentAreaFilled(false);
        NewBmen11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewBmen11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NewBmen11NewBmenMenB1MouseClicked(evt);
            }
        });
        NewBmen11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewBmen11NewBmenMenB1ActionPerformed(evt);
            }
        });

        NewBwomen11.setBackground(new java.awt.Color(204, 204, 204));
        NewBwomen11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NewBwomen11.setText("Women");
        NewBwomen11.setBorder(null);
        NewBwomen11.setContentAreaFilled(false);
        NewBwomen11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewBwomen11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewBwomen11NewBwomenWomenB1ActionPerformed(evt);
            }
        });

        NewBsp11.setBackground(new java.awt.Color(204, 204, 204));
        NewBsp11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NewBsp11.setText("Special Deal");
        NewBsp11.setBorder(null);
        NewBsp11.setContentAreaFilled(false);
        NewBsp11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewBsp11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewBsp11NewBspSpecialB1ActionPerformed(evt);
            }
        });

        Newsearch11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Newsearch11.setToolTipText("");
        Newsearch11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Newsearch11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Newsearch11NewsearchSearchF1ActionPerformed(evt);
            }
        });

        newBsearch11.setText("🔎");
        newBsearch11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBsearch11ActionPerformed(evt); 
            }
        });


        NewBcart11.setBackground(new java.awt.Color(204, 204, 204));
        ///NewBcart11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ///NewBcart11.setText("Cart");
        NewBcart11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/shopping-cart.png")));
        NewBcart11.setBorder(null);
        NewBcart11.setContentAreaFilled(false);
        NewBcart11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewBcart11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewBcart11NewBcartCartB1ActionPerformed(evt);
            }
        });
        NewBorder11 = new javax.swing.JButton();
        NewBorder11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/order.png")));
        NewBorder11.setBorder(null);
        NewBorder11.setContentAreaFilled(false);
        NewBorder11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewBorder11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motherpanel.removeAll();
                motherpanel.add(new OrderPage());
                motherpanel.revalidate();
                motherpanel.repaint();
            }
        });
        NewBlogout.setBackground(new java.awt.Color(204, 204, 204));
        NewBlogout.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        NewBlogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/logout.png")));
        ///NewBlogout.setText("Log out");
        NewBlogout.setBorder(null);
        NewBlogout.setContentAreaFilled(false);
        NewBlogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewBlogout.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // ถ้ามีขั้นตอนบันทึกตะกร้า/ข้อมูลอื่นก่อนออก ใส่เพิ่มได้ที่นี่
            Session.clearSession();
            javax.swing.JOptionPane.showMessageDialog(MainFrame.this, "Log Out!");
            MainFrame.this.dispose();
            new ShoeStoreLogin().setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                MainFrame.this,
                "Logout error: " + ex.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
         }   

        }
    
        });
        javax.swing.GroupLayout MenuNew11Layout = new javax.swing.GroupLayout(MenuNew11);
        MenuNew11.setLayout(MenuNew11Layout);
        MenuNew11Layout.setHorizontalGroup(
    MenuNew11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(MenuNew11Layout.createSequentialGroup()
        .addGap(30, 30, 30)
        .addComponent(Title17, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
        // ดันกลุ่มเมนูทั้งหมดไปชิดขวาเสมอ
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        // กลุ่มเมนูด้านขวา (เรียงติดกัน)
        .addComponent(NewBnew11)
        .addGap(26, 26, 26)
        .addComponent(NewBmen11)
        .addGap(18, 18, 18)
        .addComponent(NewBwomen11)
        .addGap(18, 18, 18)
        .addComponent(NewBsp11)
        .addGap(28, 28, 28)
        .addComponent(Newsearch11, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(newBsearch11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(24, 24, 24)
        .addComponent(NewBcart11)
        .addGap(18, 18, 18)
        .addComponent(NewBorder11)
        .addGap(18, 18, 18)
        .addComponent(NewBlogout)  // <-- Log out ต่อท้าย Cart
        .addGap(20, 20, 20))
);

        MenuNew11Layout.setVerticalGroup(
    MenuNew11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuNew11Layout.createSequentialGroup()
        .addGap(16, 16, 16)
        .addGroup(MenuNew11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(NewBnew11)
            .addComponent(NewBmen11)
            .addComponent(NewBwomen11)
            .addComponent(NewBsp11)
            .addComponent(Newsearch11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(newBsearch11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(NewBcart11)
            .addComponent(NewBorder11)
            .addComponent(NewBlogout)    // อยู่ในแถวเดียวกัน
            .addComponent(Title17))
        .addContainerGap(14, Short.MAX_VALUE))
);


        getContentPane().add(MenuNew11, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Filter
    private void applyFilterNew() {
    List<Product> filtered = filterManager.filterProducts(FilterNew, "new");

    if (filtered == null) { // หมายถึงไม่ได้ติ๊กอะไรเลย → โหลดใหม่
        showNew();
        return;
    }

    ensureGridBag(NewShowProduct);
    populateGrid(NewShowProduct, filtered);
}
// 🔹 หน้า Men
private void applyFilterMen() {
    List<Product> filtered = filterManager.filterProducts(FilterMen, "men");

    if (filtered == null) { // ถ้าไม่ติ๊กอะไร → โหลดหน้าเดิม
        showMen();
        return;
    }

    ensureGridBag(NewShowProduct2);
    populateGrid(NewShowProduct2, filtered);
}

// 🔹 หน้า Women
private void applyFilterWomen() {
    List<Product> filtered = filterManager.filterProducts(FilterWomen, "women");

    if (filtered == null) {
        showWomen();
        return;
    }

    ensureGridBag(NewShowProduct3);
    populateGrid(NewShowProduct3, filtered);
}

// 🔹 หน้า Special Deal
private void applyFilterDeal() {
    List<Product> filtered = filterManager.filterProducts(FilterSD, "special");

    if (filtered == null) {
        showSpecial();
        return;
    }

    ensureGridBag(NewShowProduct4);
    populateGrid(NewShowProduct4, filtered);
}

    private void NewBmen11NewBmenMenB1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NewBmen11NewBmenMenB1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_NewBmen11NewBmenMenB1MouseClicked

    private void NewBmen11NewBmenMenB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewBmen11NewBmenMenB1ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(MainMen);
        motherpanel.repaint();
        motherpanel.revalidate();
        showMen();
    }//GEN-LAST:event_NewBmen11NewBmenMenB1ActionPerformed

    // ==================== ปุ่มค้นหา ====================
    private void newBsearch11ActionPerformed(java.awt.event.ActionEvent evt) {                                             
        String keyword = Newsearch11.getText();
        applySearch(keyword);
    }

    private void NewBwomen11NewBwomenWomenB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewBwomen11NewBwomenWomenB1ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(MainWomen);
        motherpanel.repaint();
        motherpanel.revalidate();
        showWomen();
    }//GEN-LAST:event_NewBwomen11NewBwomenWomenB1ActionPerformed

    private void NewBsp11NewBspSpecialB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewBsp11NewBspSpecialB1ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(MainSD);
        motherpanel.repaint();
        motherpanel.revalidate();
        showSpecial();
    }//GEN-LAST:event_NewBsp11NewBspSpecialB1ActionPerformed

    private void Newsearch11NewsearchSearchF1ActionPerformed(java.awt.event.ActionEvent evt) {                                                             
    String keyword = Newsearch11.getText();
    applySearch(keyword);
    }//GEN-LAST:event_Newsearch11NewsearchSearchF1ActionPerformed


    private void NewBcart11NewBcartCartB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewBcart11NewBcartCartB1ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(Cart);
        motherpanel.repaint();
        motherpanel.revalidate();
    }//GEN-LAST:event_NewBcart11NewBcartCartB1ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void NewB10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB10ActionPerformed

    private void NewB11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB11ActionPerformed

    private void NewB12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB12ActionPerformed

    private void jCheckBox54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox54ActionPerformed

    private void NewB13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB13ActionPerformed

    private void NewB6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB6ActionPerformed

    private void NewB7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB7ActionPerformed

    private void NewB8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB8ActionPerformed

    private void jCheckBox34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox34ActionPerformed

    private void NewB9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB9ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void NewB14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB14ActionPerformed

    private void NewB15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB15ActionPerformed

    private void NewB16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB16ActionPerformed

    private void jCheckBox74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox74ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox74ActionPerformed

    private void NewB17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB17ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void NewB18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB18ActionPerformed

    private void NewB19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB19ActionPerformed

    private void NewB20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB20ActionPerformed

    private void jCheckBox94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox94ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox94ActionPerformed

    private void NewB21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewB21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewB21ActionPerformed

    private void NewBnew11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewBnew11ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(MainNew8);
        motherpanel.repaint();
        motherpanel.revalidate();
        showNew();
    }//GEN-LAST:event_NewBnew11ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        applyPromoFromField();   // ลองใช้โค้ด + อัปเดตยอด
}//GEN-LAST:event_jTextField2ActionPerformed

    // ปุ่ม Purchase
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        String code = jTextField2.getText().trim();

            // ถ้าไม่กรอก หรือยังเป็น placeholder = ข้ามการตรวจ
        if (!code.isBlank() && !PROMO_PLACEHOLDER.equals(code)) {
            if (!pricing.applyPromoCode(code)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Invalid promo code!");
                jTextField2.requestFocus();
                jTextField2.selectAll();
            return;
        }
    }
    // ไปหน้าถัดไปตามปกติ
    motherpanel.removeAll();
    motherpanel.add(Purchase);
    motherpanel.repaint();
    motherpanel.revalidate();
    forceThai(Purchase);
    loadProfileToForm();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(MainNew8);
        motherpanel.repaint();
        motherpanel.revalidate();
    }//GEN-LAST:event_jButton14ActionPerformed

    // ปุ่ม Confirm
    // ปุ่ม Confirm

private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {
    List<CartItem> snapshot = new ArrayList<>(cart.getItems());
    Map<String, String> sizeSnap = getSkuToSizeSnapshot();  // ดึงไซส์ตอนนี้ทันที
    double grand = cart.getTotalPrice();

    saveProfileFromForm(); 

    // 3. สร้างออเดอร์
    String username = "Guest";
    String[] session = Session.loadSession();
    if (session != null && session.length > 0) {
        username = session[0];
    }

    Order currentOrder = new Order(username, java.time.LocalDateTime.now(), snapshot, grand, "กำลังจัดส่ง");

    // 4. บันทึกออเดอร์ + ไซส์ ลงไฟล์ก่อน clear cart
    Collectorder.saveOrder(currentOrder, sizeSnap);

    // 5. ค่อยล้างสต็อก + ตะกร้า หลังจากบันทึกเรียบร้อยแล้ว
    reduceStockByCartAndClear();

    // 6. ไปหน้า Confirm ตามปกติ
    motherpanel.removeAll();
    motherpanel.add(Confirm);
    motherpanel.repaint();
    motherpanel.revalidate();

    fillConfirmSummary(snapshot, sizeSnap, grand);
    forceThai(Confirm);
}



//GEN-LAST:event_jButton16ActionPerformed

private static void installThaiFriendlyFont() {
    String[] candidates = {"Segoe UI", "Tahoma", "Noto Sans Thai", "Sarabun"};
    Set<String> installed = new HashSet<>(Arrays.asList(
        GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()
    ));
    String pick = Arrays.stream(candidates).filter(installed::contains).findFirst().orElse("Dialog");

    Font base = new Font(pick, Font.PLAIN, 16); 
    UIManager.put("Label.font", base);
    UIManager.put("TextField.font", base);
    UIManager.put("Button.font", base);
    UIManager.put("CheckBox.font", base);
    UIManager.put("ComboBox.font", base);
    UIManager.put("TextArea.font", base);
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        MainFrame.installThaiUIFont(16f);
      java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
                public void run() {
                new MainFrame().setVisible(true);
            }
});

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Cart;
    private javax.swing.JPanel Confirm;
    private javax.swing.JPanel FilterMen;
    private javax.swing.JPanel FilterNew;
    private javax.swing.JPanel FilterSD;
    private javax.swing.JPanel FilterWomen;
    private javax.swing.JPanel MainMen;
    private javax.swing.JPanel MainNew8;
    private javax.swing.JPanel MainSD;
    private javax.swing.JPanel MainWomen;
    private javax.swing.JPanel MenuNew11;
    private javax.swing.JButton NewB10;
    private javax.swing.JButton NewB11;
    private javax.swing.JButton NewB12;
    private javax.swing.JButton NewB13;
    private javax.swing.JButton NewB14;
    private javax.swing.JButton NewB15;
    private javax.swing.JButton NewB16;
    private javax.swing.JButton NewB17;
    private javax.swing.JButton NewB18;
    private javax.swing.JButton NewB19;
    private javax.swing.JButton NewB20;
    private javax.swing.JButton NewB21;
    private javax.swing.JButton NewB6;
    private javax.swing.JButton NewB7;
    private javax.swing.JButton NewB8;
    private javax.swing.JButton NewB9;
    private javax.swing.JButton NewBcart11;
    private javax.swing.JButton NewBlogout;
    private javax.swing.JButton NewBmen11;
    private javax.swing.JButton NewBnew11;
    private javax.swing.JButton NewBorder11;
    private javax.swing.JButton NewBsp11;
    private javax.swing.JButton NewBwomen11;
    private javax.swing.JPanel NewShowProduct;
    private javax.swing.JPanel NewShowProduct2;
    private javax.swing.JPanel NewShowProduct3;
    private javax.swing.JPanel NewShowProduct4;
    private javax.swing.JTextField Newsearch11;
    private javax.swing.JPanel ProductMen;
    private javax.swing.JPanel ProductNew;
    private javax.swing.JPanel ProductSD;
    private javax.swing.JPanel ProductWomen;
    private javax.swing.JPanel Purchase;
    private javax.swing.JLabel Title17;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox100;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox36;
    private javax.swing.JCheckBox jCheckBox37;
    private javax.swing.JCheckBox jCheckBox38;
    private javax.swing.JCheckBox jCheckBox39;
    private javax.swing.JCheckBox jCheckBox40;
    private javax.swing.JCheckBox jCheckBox41;
    private javax.swing.JCheckBox jCheckBox42;
    private javax.swing.JCheckBox jCheckBox44;
    private javax.swing.JCheckBox jCheckBox45;
    private javax.swing.JCheckBox jCheckBox56;
    private javax.swing.JCheckBox jCheckBox57;
    private javax.swing.JCheckBox jCheckBox58;
    private javax.swing.JCheckBox jCheckBox59;
    private javax.swing.JCheckBox jCheckBox60;
    private javax.swing.JCheckBox jCheckBox61;
    private javax.swing.JCheckBox jCheckBox62;
    private javax.swing.JCheckBox jCheckBox64;
    private javax.swing.JCheckBox jCheckBox65;
    private javax.swing.JCheckBox jCheckBox76;
    private javax.swing.JCheckBox jCheckBox77;
    private javax.swing.JCheckBox jCheckBox78;
    private javax.swing.JCheckBox jCheckBox79;
    private javax.swing.JCheckBox jCheckBox80;
    private javax.swing.JCheckBox jCheckBox81;
    private javax.swing.JCheckBox jCheckBox82;
    private javax.swing.JCheckBox jCheckBox84;
    private javax.swing.JCheckBox jCheckBox85;
    private javax.swing.JCheckBox jCheckBox96;
    private javax.swing.JCheckBox jCheckBox97;
    private javax.swing.JCheckBox jCheckBox98;
    private javax.swing.JCheckBox jCheckBox99;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JPanel motherpanel;
    private javax.swing.JButton newBsearch11;
    private javax.swing.JComboBox<String> sizeComboBox;
    private javax.swing.JComboBox<String> sizeComboBoxMen;
    private javax.swing.JComboBox<String> sizeComboBoxWomen;
    private javax.swing.JComboBox<String> sizeComboBoxSD;
    // End of variables declaration//GEN-END:variables
}
