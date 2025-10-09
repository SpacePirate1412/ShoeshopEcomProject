/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package page;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.text.NumberFormat;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

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
        // —————————— Shopping cart ——————————
        private final PricingService pricing = new PricingService();
        private final ShoppingCart cart = new ShoppingCart(pricing, catalog);
        // ป้ายจำนวนบนการ์ดสินค้า (key = SKU)
        private final Map<String, javax.swing.JLabel> skuToQtyLabel = new HashMap<>();

        private void setupWomenArea() {
            ProductWomen.removeAll();
            ProductWomen.setLayout(new BorderLayout());
            ProductWomen.add(jTextField5, BorderLayout.NORTH);   
         configureSpacedGrid(womenGrid);                      
            ProductWomen.add(womenGrid, BorderLayout.CENTER);
            ProductWomen.revalidate();ProductWomen.repaint();
}

        private void setupDealArea() {
            ProductSD.removeAll();
            ProductSD.setLayout(new BorderLayout());
            ProductSD.add(jTextField6, BorderLayout.NORTH);      
        configureSpacedGrid(dealGrid);                       
            ProductSD.add(dealGrid, BorderLayout.CENTER);
            ProductSD.revalidate();ProductSD.repaint();
}
        private void setupNewArea() {
            ProductNew.removeAll();
            ProductNew.setLayout(new BorderLayout());
        // แถบหัวเรื่อง
            ProductNew.add(jTextField4, BorderLayout.NORTH);
        // โชว์สินค้าในกริดผ่านสกรอลล์พาเนล
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
        JScrollPane sp = new JScrollPane(NewShowProduct7);
            sp.setBorder(null);
            sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            sp.getVerticalScrollBar().setUnitIncrement(24);
            ProductMen.add(sp, BorderLayout.CENTER);
}

        private void showNew() {
            catalog.reload();
            ArrayList<Product> all = new ArrayList<Product>(catalog.getAllProducts());
            ArrayList<Product> onlyNoDiscount = new ArrayList<Product>();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
        if (p.getDiscountPercent() == 0.0) {
            onlyNoDiscount.add(p);
        }
    }
    int from = Math.max(onlyNoDiscount.size() - 4, 0); //เอาสินค้า 4 อันล่าสุดที่ไม่ได้มีการลดราคามา
    ArrayList<Product> last5 = new ArrayList<Product>();
    for (int i = from; i < onlyNoDiscount.size(); i++) {
        last5.add(onlyNoDiscount.get(i));
    }
    ensureGridBag(NewShowProduct);
    populateGrid(NewShowProduct, last5);
}

        private void showMen() {
            catalog.reload();
            ArrayList<Product> men = new ArrayList<Product>();
            List<Product> all = catalog.getAllProducts();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
        if ("Men".equalsIgnoreCase(p.getGender()) && p.getDiscountPercent() == 0.0) {
            men.add(p);
        }
    }
    ensureGridBag(NewShowProduct7);
    populateGrid(NewShowProduct7, men);
}

        private void showWomen() {
            catalog.reload();
            ArrayList<Product> women = new ArrayList<Product>();
            List<Product> all = catalog.getAllProducts();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
        if ("Women".equalsIgnoreCase(p.getGender()) && p.getDiscountPercent() == 0.0) {
            women.add(p);
        }
    }
    populateGrid(womenGrid, women);
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
    populateGrid(dealGrid, deals);
}
private void updateCartSummary() {
    double total = cart.getTotalPrice();                 // จะคิดส่วนลด/โปรโมโค้ดให้เอง
    jLabel58.setText("฿ " + THB.format(Math.round(total)));  // Grand Total
    // ถ้าจะโชว์ subtotal/discount แยก ค่อยคำนวณเพิ่มทีหลังได้
}


private void ensureGridBag(JPanel panel) {
    if (!(panel.getLayout() instanceof GridBagLayout)) {
        panel.setLayout(new GridBagLayout());
    }
}
/** ใช้คอลัมน์เว้นช่องแบบเดียวกับ New/Men: 0,15,0,15,0,15,0 */
private void configureSpacedGrid(JPanel grid) {
    GridBagLayout gbl = new GridBagLayout();
    gbl.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0}; // 4 คอลัมน์จริง + ช่องว่าง
    gbl.rowHeights  = new int[] {0, 12, 0};                // 2 แถวจริง + ช่องว่าง
    grid.setLayout(gbl);
    grid.setBackground(Color.WHITE);
}

private void populateGrid(JPanel grid, List<Product> items) {
    grid.removeAll();

    GridBagConstraints gc = new GridBagConstraints();
    gc.insets = new Insets(0, 0, 0, 0);    // ระยะเว้นใช้จาก columnWidths/rowHeights แล้ว
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.weightx = 1.0;
    int col = 0, row = 0;
    final int COLS = 4; // 4 ช่องต่อแถวเหมือนหน้า New/Men
    for (int i = 0; i < items.size(); i++) {
        Product p = items.get(i);
        gc.gridx = col * 2;    // ข้ามคอลัมน์เว้นช่อง (0,2,4,6)
        gc.gridy = row * 2;    // ข้ามแถวเว้นช่อง (0,2,4,...)
        grid.add(createProductCard(p), gc);
        col++;
        if (col == COLS) { col = 0; row++; }
    }
    grid.revalidate();
    grid.repaint();
}

private static final int CARD_W  = 200;
private static final int CARD_H  = 240;
private static final int IMG_H   = 100;
private static final int BRAND_H = 16;
private static final int NAME_H  = 28;  // เผื่อ 2 บรรทัด
private static final int PRICE_H = 18;
private static final int BTN_H   = 30;

private static String htmlWrap(String text, int widthPx) {
    if (text == null) text = "";
    // ทำให้ JLabel แสดงหลายบรรทัดตามความกว้างที่กำหนด
    return "<html><div style='width:" + widthPx + "px'>" + text + "</div></html>";
}

private JPanel createProductCard(final Product p) {
    JPanel card = new JPanel();
    card.setPreferredSize(new Dimension(160, 195));
    card.setBackground(Color.WHITE);
    card.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(230,230,230)));

    // รูป (placeholder)
    javax.swing.JLabel img = new javax.swing.JLabel("IMAGE", javax.swing.SwingConstants.CENTER);
    img.setPreferredSize(new Dimension(143, 88));
    img.setOpaque(true);
    img.setBackground(new Color(245,245,245));

    javax.swing.JLabel brand = new javax.swing.JLabel(p.getBrand());
    brand.setFont(brand.getFont().deriveFont(Font.BOLD, 12f));
    javax.swing.JLabel name = new javax.swing.JLabel(p.getName());
    name.setFont(name.getFont().deriveFont(Font.BOLD, 10f));

    javax.swing.JPanel pricePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
    pricePanel.setOpaque(false);
    if (p.getDiscountPercent() != 0.0) {
        javax.swing.JLabel orig = new javax.swing.JLabel("฿ " + THB.format(Math.round(p.getPrice())));
        orig.setFont(orig.getFont().deriveFont(Font.PLAIN, 10f));
        orig.setForeground(Color.GRAY);
        orig.setText("<html><strike>" + orig.getText() + "</strike></html>");

        double after = p.getPriceAfterBuiltInDiscount();
        javax.swing.JLabel now = new javax.swing.JLabel("฿ " + THB.format(Math.round(after)));
        now.setFont(now.getFont().deriveFont(Font.BOLD, 11f));
        pricePanel.add(orig);
        pricePanel.add(now);
    } else {
        javax.swing.JLabel now = new javax.swing.JLabel("฿ " + THB.format(Math.round(p.getPrice())));
        now.setFont(now.getFont().deriveFont(Font.BOLD, 11f));
        pricePanel.add(now);
    }

    // ปุ่มและป้ายจำนวนในตะกร้า
    javax.swing.JButton add = new javax.swing.JButton("Add to Cart");
    add.setBackground(Color.BLACK);
    add.setForeground(Color.WHITE);

    final javax.swing.JLabel qtyLabel = new javax.swing.JLabel();
    qtyLabel.setFont(qtyLabel.getFont().deriveFont(Font.BOLD, 10f));
    qtyLabel.setForeground(new Color(60,60,60));
    qtyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // เซ็ตจำนวนเริ่มต้นตามตะกร้า
    int startQty = getQtyInCart(p.getSku());
    if (startQty > 0) {
        qtyLabel.setText("In cart: " + startQty);
        qtyLabel.setVisible(true);
    } else {
        qtyLabel.setVisible(false);
    }
    skuToQtyLabel.put(p.getSku(), qtyLabel);

    add.addActionListener(new ActionListener() {
        @Override public void actionPerformed(ActionEvent e) {
            try {
                cart.addItem(p.getSku(), 1);                 // เพิ่มลงตะกร้า
                int q = getQtyInCart(p.getSku());            // จำนวนล่าสุด
                javax.swing.JLabel lbl = skuToQtyLabel.get(p.getSku());
                if (lbl != null) { 
                    lbl.setText("In cart: " + q);
                    lbl.setVisible(q > 0);
                }
                refreshCartListPanel();                      // รีเฟรชหน้า Cart + สรุปยอด
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

    javax.swing.Box box = javax.swing.Box.createVerticalBox();
    box.add(javax.swing.Box.createVerticalStrut(6));
    box.add(img);
    box.add(javax.swing.Box.createVerticalStrut(6));
    box.add(brand);
    box.add(name);
    box.add(javax.swing.Box.createVerticalStrut(4));
    box.add(pricePanel);
    box.add(javax.swing.Box.createVerticalStrut(6));
    box.add(add);
    box.add(javax.swing.Box.createVerticalStrut(4));
    box.add(qtyLabel);                      // <<— ป้ายจำนวนในตะกร้า
    box.add(javax.swing.Box.createVerticalStrut(6));

    card.setLayout(new BorderLayout());
    card.add(box, BorderLayout.CENTER);
    return card;
}



    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setupNewArea(); setupMenArea(); setupWomenArea(); setupDealArea();
        setupCartPage(); setupPurchasePage(); setupConfirmPage();
        showNew();           // แสดงหน้า New ครั้งแรก (ดึงจากไฟล์)
        setLocationRelativeTo(null);           // จัดกึ่งกลาง (ก่อนจะ maximize ก็ได้)
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);   // เปิดมาแบบเต็มหน้าจอ
        refreshCartListPanel();   // ให้หน้า Cart (ฝั่งซ้าย) ว่างแบบ layout พร้อมใช้งาน และสรุปยอดเป็น 0

    }
    /** ===== CART: ให้สรุปออเดอร์อยู่ฝั่งขวา ===== */
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

        String name = it.getProduct().getBrand() + "  " + it.getProduct().getName();
        javax.swing.JLabel left = new javax.swing.JLabel(name);
        left.setFont(left.getFont().deriveFont(Font.BOLD, 14f));

        double sub = it.getProduct().getPriceAfterBuiltInDiscount() * it.getQuantity();
        javax.swing.JLabel right = new javax.swing.JLabel("x" + it.getQuantity() + "   ฿ " + THB.format(Math.round(sub)));
        right.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        rowPanel.add(left, BorderLayout.WEST);
        rowPanel.add(right, BorderLayout.EAST);

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

/** คำนวณยอดรวมแล้วอัปเดตป้ายในกล่อง Order Summary (jLabel55..58) */
private void updateOrderSummary() {
    double subtotal = 0.0;
    for (CartItem it : cart.getItems()) {
        subtotal += it.getProduct().getPriceAfterBuiltInDiscount() * it.getQuantity();
    }
    jLabel55.setText("+ " + THB.format(Math.round(subtotal))); // Subtotal
    jLabel56.setText("+ 0.00");                                 // Shipping (ปรับได้ภายหลัง)
    jLabel57.setText("- 0.00");                                 // Discount (คูปอง/โปรเพิ่มได้ภายหลัง)
    jLabel58.setText(THB.format(Math.round(subtotal)));         // Grand total ตอนนี้ = subtotal
}

/** ===== PURCHASE: จัด Shipping Address ให้อยู่กลางจอ ===== */
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

/** ===== CONFIRM: จัดการ์ด Order Completed ให้อยู่กลางจอ ===== */
private void setupConfirmPage() {
    Confirm.removeAll();
    Confirm.setLayout(new BorderLayout());

    // หัวข้อซ้ายบนคงเดิม
    JPanel north = new JPanel(new BorderLayout());
    north.setOpaque(false);
    north.add(jLabel60, BorderLayout.WEST);
    north.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 0, 20));
    Confirm.add(north, BorderLayout.NORTH);

    // กลางจอ
    JPanel centerWrap = new JPanel(new GridBagLayout());
    centerWrap.setOpaque(false);

    JPanel content = new JPanel();
    content.setOpaque(false);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

    jPanel15.setAlignmentX(Component.CENTER_ALIGNMENT); // การ์ดสีเทา
    jButton14.setAlignmentX(Component.CENTER_ALIGNMENT); // ปุ่มกลับบ้าน
    content.add(jPanel15);
    content.add(Box.createVerticalStrut(20));
    content.add(jButton14);

    centerWrap.add(content, new GridBagConstraints());
    Confirm.add(centerWrap, BorderLayout.CENTER);

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
        Newshowproduct1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Newshowproduct2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Newshowproduct3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        Newshowproduct4 = new javax.swing.JPanel();
        Newshowproduct5 = new javax.swing.JPanel();
        Newshowproduct6 = new javax.swing.JPanel();
        Newshowproduct7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        Newshowproduct8 = new javax.swing.JPanel();
        FilterNew = new javax.swing.JPanel();
        NewB10 = new javax.swing.JButton();
        NewB11 = new javax.swing.JButton();
        jCheckBox41 = new javax.swing.JCheckBox();
        jCheckBox42 = new javax.swing.JCheckBox();
        jCheckBox43 = new javax.swing.JCheckBox();
        jCheckBox44 = new javax.swing.JCheckBox();
        jCheckBox45 = new javax.swing.JCheckBox();
        NewB12 = new javax.swing.JButton();
        jCheckBox46 = new javax.swing.JCheckBox();
        jCheckBox47 = new javax.swing.JCheckBox();
        jCheckBox48 = new javax.swing.JCheckBox();
        jCheckBox49 = new javax.swing.JCheckBox();
        jCheckBox50 = new javax.swing.JCheckBox();
        jCheckBox51 = new javax.swing.JCheckBox();
        jCheckBox52 = new javax.swing.JCheckBox();
        jCheckBox53 = new javax.swing.JCheckBox();
        jCheckBox54 = new javax.swing.JCheckBox();
        jCheckBox55 = new javax.swing.JCheckBox();
        NewB13 = new javax.swing.JButton();
        jCheckBox56 = new javax.swing.JCheckBox();
        jCheckBox57 = new javax.swing.JCheckBox();
        jCheckBox58 = new javax.swing.JCheckBox();
        jCheckBox59 = new javax.swing.JCheckBox();
        jCheckBox60 = new javax.swing.JCheckBox();
        MainMen = new javax.swing.JPanel();
        ProductMen = new javax.swing.JPanel();
        NewShowProduct7 = new javax.swing.JPanel();
        Newshowproduct15 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        Newshowproduct16 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        Newshowproduct17 = new javax.swing.JPanel();
        Newshowproduct18 = new javax.swing.JPanel();
        Newshowproduct19 = new javax.swing.JPanel();
        Newshowproduct20 = new javax.swing.JPanel();
        Newshowproduct21 = new javax.swing.JPanel();
        Newshowproduct22 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        FilterMen = new javax.swing.JPanel();
        NewB6 = new javax.swing.JButton();
        NewB7 = new javax.swing.JButton();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jCheckBox23 = new javax.swing.JCheckBox();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        NewB8 = new javax.swing.JButton();
        jCheckBox26 = new javax.swing.JCheckBox();
        jCheckBox27 = new javax.swing.JCheckBox();
        jCheckBox28 = new javax.swing.JCheckBox();
        jCheckBox29 = new javax.swing.JCheckBox();
        jCheckBox30 = new javax.swing.JCheckBox();
        jCheckBox31 = new javax.swing.JCheckBox();
        jCheckBox32 = new javax.swing.JCheckBox();
        jCheckBox33 = new javax.swing.JCheckBox();
        jCheckBox34 = new javax.swing.JCheckBox();
        jCheckBox35 = new javax.swing.JCheckBox();
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
        jCheckBox63 = new javax.swing.JCheckBox();
        jCheckBox64 = new javax.swing.JCheckBox();
        jCheckBox65 = new javax.swing.JCheckBox();
        NewB16 = new javax.swing.JButton();
        jCheckBox66 = new javax.swing.JCheckBox();
        jCheckBox67 = new javax.swing.JCheckBox();
        jCheckBox68 = new javax.swing.JCheckBox();
        jCheckBox69 = new javax.swing.JCheckBox();
        jCheckBox70 = new javax.swing.JCheckBox();
        jCheckBox71 = new javax.swing.JCheckBox();
        jCheckBox72 = new javax.swing.JCheckBox();
        jCheckBox73 = new javax.swing.JCheckBox();
        jCheckBox74 = new javax.swing.JCheckBox();
        jCheckBox75 = new javax.swing.JCheckBox();
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
        jCheckBox83 = new javax.swing.JCheckBox();
        jCheckBox84 = new javax.swing.JCheckBox();
        jCheckBox85 = new javax.swing.JCheckBox();
        NewB20 = new javax.swing.JButton();
        jCheckBox86 = new javax.swing.JCheckBox();
        jCheckBox87 = new javax.swing.JCheckBox();
        jCheckBox88 = new javax.swing.JCheckBox();
        jCheckBox89 = new javax.swing.JCheckBox();
        jCheckBox90 = new javax.swing.JCheckBox();
        jCheckBox91 = new javax.swing.JCheckBox();
        jCheckBox92 = new javax.swing.JCheckBox();
        jCheckBox93 = new javax.swing.JCheckBox();
        jCheckBox94 = new javax.swing.JCheckBox();
        jCheckBox95 = new javax.swing.JCheckBox();
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

        MainNew8.setMaximumSize(new java.awt.Dimension(920, 573));
        MainNew8.setPreferredSize(new java.awt.Dimension(900, 530));

        ProductNew.setBackground(new java.awt.Color(255, 255, 255));
        ProductNew.setPreferredSize(new java.awt.Dimension(708, 490));

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

        NewShowProduct.setBackground(new java.awt.Color(255, 255, 255));
        java.awt.GridBagLayout NewShowProductLayout = new java.awt.GridBagLayout();
        NewShowProductLayout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0};
        NewShowProductLayout.rowHeights = new int[] {0, 12, 0};
        NewShowProduct.setLayout(NewShowProductLayout);

        Newshowproduct1.setPreferredSize(new java.awt.Dimension(159, 195));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Home/forMen (1).png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Adidas");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 9)); // NOI18N
        jLabel3.setText("FECHTEN INDOOR CONSORTIUM");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel4.setText("฿ 4,500");

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add to Cart");

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

        javax.swing.GroupLayout Newshowproduct1Layout = new javax.swing.GroupLayout(Newshowproduct1);
        Newshowproduct1.setLayout(Newshowproduct1Layout);
        Newshowproduct1Layout.setHorizontalGroup(
            Newshowproduct1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Newshowproduct1Layout.setVerticalGroup(
            Newshowproduct1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        NewShowProduct.add(Newshowproduct1, gridBagConstraints);

        Newshowproduct2.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct2.setPreferredSize(new java.awt.Dimension(159, 195));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("New Balance");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 9)); // NOI18N
        jLabel7.setText("New Balance 740");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel8.setText("฿ 4,300");

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Add to Cart");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Home/image.png"))); // NOI18N

        javax.swing.GroupLayout Newshowproduct2Layout = new javax.swing.GroupLayout(Newshowproduct2);
        Newshowproduct2.setLayout(Newshowproduct2Layout);
        Newshowproduct2Layout.setHorizontalGroup(
            Newshowproduct2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Newshowproduct2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Newshowproduct2Layout.createSequentialGroup()
                        .addGroup(Newshowproduct2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Newshowproduct2Layout.setVerticalGroup(
            Newshowproduct2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        NewShowProduct.add(Newshowproduct2, gridBagConstraints);

        Newshowproduct3.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct3.setPreferredSize(new java.awt.Dimension(159, 195));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Home/httpsimages.stockx.comimagesNike.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Nike");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 9)); // NOI18N
        jLabel11.setText("Air Force 1 Low Retro Premium ");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel12.setText("฿ 5,400");

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Add to Cart");

        javax.swing.GroupLayout Newshowproduct3Layout = new javax.swing.GroupLayout(Newshowproduct3);
        Newshowproduct3.setLayout(Newshowproduct3Layout);
        Newshowproduct3Layout.setHorizontalGroup(
            Newshowproduct3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Newshowproduct3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Newshowproduct3Layout.createSequentialGroup()
                        .addGroup(Newshowproduct3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Newshowproduct3Layout.setVerticalGroup(
            Newshowproduct3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        NewShowProduct.add(Newshowproduct3, gridBagConstraints);

        Newshowproduct4.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct4.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct4Layout = new javax.swing.GroupLayout(Newshowproduct4);
        Newshowproduct4.setLayout(Newshowproduct4Layout);
        Newshowproduct4Layout.setHorizontalGroup(
            Newshowproduct4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct4Layout.setVerticalGroup(
            Newshowproduct4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        NewShowProduct.add(Newshowproduct4, gridBagConstraints);

        Newshowproduct5.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct5.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct5Layout = new javax.swing.GroupLayout(Newshowproduct5);
        Newshowproduct5.setLayout(Newshowproduct5Layout);
        Newshowproduct5Layout.setHorizontalGroup(
            Newshowproduct5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct5Layout.setVerticalGroup(
            Newshowproduct5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        NewShowProduct.add(Newshowproduct5, gridBagConstraints);

        Newshowproduct6.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct6.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct6Layout = new javax.swing.GroupLayout(Newshowproduct6);
        Newshowproduct6.setLayout(Newshowproduct6Layout);
        Newshowproduct6Layout.setHorizontalGroup(
            Newshowproduct6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct6Layout.setVerticalGroup(
            Newshowproduct6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        NewShowProduct.add(Newshowproduct6, gridBagConstraints);

        Newshowproduct7.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct7.setPreferredSize(new java.awt.Dimension(159, 195));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Home/043519_3.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Puma");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 9)); // NOI18N
        jLabel15.setText("Speedcat Metallic");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel16.setText("฿ 3,800");

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Add to Cart");

        javax.swing.GroupLayout Newshowproduct7Layout = new javax.swing.GroupLayout(Newshowproduct7);
        Newshowproduct7.setLayout(Newshowproduct7Layout);
        Newshowproduct7Layout.setHorizontalGroup(
            Newshowproduct7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Newshowproduct7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Newshowproduct7Layout.createSequentialGroup()
                        .addGroup(Newshowproduct7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Newshowproduct7Layout.setVerticalGroup(
            Newshowproduct7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        NewShowProduct.add(Newshowproduct7, gridBagConstraints);

        Newshowproduct8.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct8.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct8Layout = new javax.swing.GroupLayout(Newshowproduct8);
        Newshowproduct8.setLayout(Newshowproduct8Layout);
        Newshowproduct8Layout.setHorizontalGroup(
            Newshowproduct8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct8Layout.setVerticalGroup(
            Newshowproduct8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        NewShowProduct.add(Newshowproduct8, gridBagConstraints);

        javax.swing.GroupLayout ProductNewLayout = new javax.swing.GroupLayout(ProductNew);
        ProductNew.setLayout(ProductNewLayout);
        ProductNewLayout.setHorizontalGroup(
            ProductNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductNewLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ProductNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(NewShowProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE))
                .addContainerGap())
        );
        ProductNewLayout.setVerticalGroup(
            ProductNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductNewLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewShowProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

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
        jCheckBox42.setText("ASUCS");

        jCheckBox43.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox43.setText("New Balance");

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

        jCheckBox46.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox46.setText("36");

        jCheckBox47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox47.setText("41");

        jCheckBox48.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox48.setText("37");

        jCheckBox49.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox49.setText("38");

        jCheckBox50.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox50.setText("39");

        jCheckBox51.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox51.setText("40");

        jCheckBox52.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox52.setText("42");

        jCheckBox53.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox53.setText("44");

        jCheckBox54.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox54.setText("43");
        jCheckBox54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox54ActionPerformed(evt);
            }
        });

        jCheckBox55.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox55.setText("45");

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
                    .addComponent(jCheckBox43)
                    .addComponent(jCheckBox44)
                    .addComponent(jCheckBox45)
                    .addComponent(NewB12)
                    .addGroup(FilterNewLayout.createSequentialGroup()
                        .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox46)
                            .addComponent(jCheckBox48)
                            .addComponent(jCheckBox49)
                            .addComponent(jCheckBox50)
                            .addComponent(jCheckBox51))
                        .addGap(51, 51, 51)
                        .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox55)
                            .addComponent(jCheckBox53)
                            .addComponent(jCheckBox52)
                            .addComponent(jCheckBox47)
                            .addComponent(jCheckBox54)))
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
                .addComponent(jCheckBox43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox46)
                    .addComponent(jCheckBox47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox48)
                    .addComponent(jCheckBox52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox49)
                    .addComponent(jCheckBox54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox50)
                    .addComponent(jCheckBox53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox51)
                    .addComponent(jCheckBox55))
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
                .addGroup(MainNew8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(FilterNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ProductNew, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        motherpanel.add(MainNew8, "card3");

        MainMen.setPreferredSize(new java.awt.Dimension(900, 530));

        ProductMen.setBackground(new java.awt.Color(255, 255, 255));
        ProductMen.setPreferredSize(new java.awt.Dimension(708, 490));

        NewShowProduct7.setBackground(new java.awt.Color(255, 255, 255));
        java.awt.GridBagLayout NewShowProduct7Layout = new java.awt.GridBagLayout();
        NewShowProduct7Layout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0};
        NewShowProduct7Layout.rowHeights = new int[] {0, 12, 0};
        NewShowProduct7.setLayout(NewShowProduct7Layout);

        Newshowproduct15.setPreferredSize(new java.awt.Dimension(159, 195));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Home/shoe1.png"))); // NOI18N

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel42.setText("Adidas");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 9)); // NOI18N
        jLabel43.setText("Super Star II");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel44.setText("฿ 4,500");

        jButton11.setBackground(new java.awt.Color(0, 0, 0));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Add to Cart");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Newshowproduct15Layout = new javax.swing.GroupLayout(Newshowproduct15);
        Newshowproduct15.setLayout(Newshowproduct15Layout);
        Newshowproduct15Layout.setHorizontalGroup(
            Newshowproduct15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Newshowproduct15Layout.setVerticalGroup(
            Newshowproduct15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct15Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        NewShowProduct7.add(Newshowproduct15, gridBagConstraints);

        Newshowproduct16.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct16.setPreferredSize(new java.awt.Dimension(159, 195));

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel45.setText("New Balance");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 9)); // NOI18N
        jLabel46.setText("New Balance 740");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel47.setText("฿ 4,300");

        jButton12.setBackground(new java.awt.Color(0, 0, 0));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Add to Cart");

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Home/new-balance-mr530-beige - w - 1.png"))); // NOI18N
        jLabel48.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout Newshowproduct16Layout = new javax.swing.GroupLayout(Newshowproduct16);
        Newshowproduct16.setLayout(Newshowproduct16Layout);
        Newshowproduct16Layout.setHorizontalGroup(
            Newshowproduct16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Newshowproduct16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Newshowproduct16Layout.createSequentialGroup()
                        .addGroup(Newshowproduct16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Newshowproduct16Layout.setVerticalGroup(
            Newshowproduct16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Newshowproduct16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        NewShowProduct7.add(Newshowproduct16, gridBagConstraints);

        Newshowproduct17.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct17.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct17Layout = new javax.swing.GroupLayout(Newshowproduct17);
        Newshowproduct17.setLayout(Newshowproduct17Layout);
        Newshowproduct17Layout.setHorizontalGroup(
            Newshowproduct17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 159, Short.MAX_VALUE)
        );
        Newshowproduct17Layout.setVerticalGroup(
            Newshowproduct17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        NewShowProduct7.add(Newshowproduct17, gridBagConstraints);

        Newshowproduct18.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct18.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct18Layout = new javax.swing.GroupLayout(Newshowproduct18);
        Newshowproduct18.setLayout(Newshowproduct18Layout);
        Newshowproduct18Layout.setHorizontalGroup(
            Newshowproduct18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct18Layout.setVerticalGroup(
            Newshowproduct18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        NewShowProduct7.add(Newshowproduct18, gridBagConstraints);

        Newshowproduct19.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct19.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct19Layout = new javax.swing.GroupLayout(Newshowproduct19);
        Newshowproduct19.setLayout(Newshowproduct19Layout);
        Newshowproduct19Layout.setHorizontalGroup(
            Newshowproduct19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct19Layout.setVerticalGroup(
            Newshowproduct19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        NewShowProduct7.add(Newshowproduct19, gridBagConstraints);

        Newshowproduct20.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct20.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct20Layout = new javax.swing.GroupLayout(Newshowproduct20);
        Newshowproduct20.setLayout(Newshowproduct20Layout);
        Newshowproduct20Layout.setHorizontalGroup(
            Newshowproduct20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct20Layout.setVerticalGroup(
            Newshowproduct20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        NewShowProduct7.add(Newshowproduct20, gridBagConstraints);

        Newshowproduct21.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct21.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct21Layout = new javax.swing.GroupLayout(Newshowproduct21);
        Newshowproduct21.setLayout(Newshowproduct21Layout);
        Newshowproduct21Layout.setHorizontalGroup(
            Newshowproduct21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 159, Short.MAX_VALUE)
        );
        Newshowproduct21Layout.setVerticalGroup(
            Newshowproduct21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        NewShowProduct7.add(Newshowproduct21, gridBagConstraints);

        Newshowproduct22.setBackground(new java.awt.Color(255, 255, 255));
        Newshowproduct22.setPreferredSize(new java.awt.Dimension(159, 195));

        javax.swing.GroupLayout Newshowproduct22Layout = new javax.swing.GroupLayout(Newshowproduct22);
        Newshowproduct22.setLayout(Newshowproduct22Layout);
        Newshowproduct22Layout.setHorizontalGroup(
            Newshowproduct22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Newshowproduct22Layout.setVerticalGroup(
            Newshowproduct22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        NewShowProduct7.add(Newshowproduct22, gridBagConstraints);

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

        javax.swing.GroupLayout ProductMenLayout = new javax.swing.GroupLayout(ProductMen);
        ProductMen.setLayout(ProductMenLayout);
        ProductMenLayout.setHorizontalGroup(
            ProductMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductMenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ProductMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NewShowProduct7, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                    .addComponent(jTextField3))
                .addContainerGap())
        );
        ProductMenLayout.setVerticalGroup(
            ProductMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductMenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewShowProduct7, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jCheckBox22.setText("ASUCS");

        jCheckBox23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox23.setText("New Balance");

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

        jCheckBox26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox26.setText("36");

        jCheckBox27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox27.setText("41");

        jCheckBox28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox28.setText("37");

        jCheckBox29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox29.setText("38");

        jCheckBox30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox30.setText("39");

        jCheckBox31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox31.setText("40");

        jCheckBox32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox32.setText("42");

        jCheckBox33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox33.setText("44");

        jCheckBox34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox34.setText("43");
        jCheckBox34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox34ActionPerformed(evt);
            }
        });

        jCheckBox35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox35.setText("45");

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
                    .addComponent(jCheckBox23)
                    .addComponent(jCheckBox24)
                    .addComponent(jCheckBox25)
                    .addComponent(NewB8)
                    .addGroup(FilterMenLayout.createSequentialGroup()
                        .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox26)
                            .addComponent(jCheckBox28)
                            .addComponent(jCheckBox29)
                            .addComponent(jCheckBox30)
                            .addComponent(jCheckBox31))
                        .addGap(51, 51, 51)
                        .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox35)
                            .addComponent(jCheckBox33)
                            .addComponent(jCheckBox32)
                            .addComponent(jCheckBox27)
                            .addComponent(jCheckBox34)))
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
                .addComponent(jCheckBox23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox26)
                    .addComponent(jCheckBox27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox28)
                    .addComponent(jCheckBox32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox29)
                    .addComponent(jCheckBox34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox30)
                    .addComponent(jCheckBox33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox31)
                    .addComponent(jCheckBox35))
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

        motherpanel.add(MainMen, "card2");

        MainWomen.setPreferredSize(new java.awt.Dimension(900, 515));

        ProductWomen.setBackground(new java.awt.Color(255, 255, 255));

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
        jCheckBox62.setText("ASUCS");

        jCheckBox63.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox63.setText("New Balance");

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

        jCheckBox66.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox66.setText("36");

        jCheckBox67.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox67.setText("41");

        jCheckBox68.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox68.setText("37");

        jCheckBox69.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox69.setText("38");

        jCheckBox70.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox70.setText("39");

        jCheckBox71.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox71.setText("40");

        jCheckBox72.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox72.setText("42");

        jCheckBox73.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox73.setText("44");

        jCheckBox74.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox74.setText("43");
        jCheckBox74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox74ActionPerformed(evt);
            }
        });

        jCheckBox75.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox75.setText("45");

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
                    .addComponent(jCheckBox63)
                    .addComponent(jCheckBox64)
                    .addComponent(jCheckBox65)
                    .addComponent(NewB16)
                    .addGroup(FilterWomenLayout.createSequentialGroup()
                        .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox66)
                            .addComponent(jCheckBox68)
                            .addComponent(jCheckBox69)
                            .addComponent(jCheckBox70)
                            .addComponent(jCheckBox71))
                        .addGap(51, 51, 51)
                        .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox75)
                            .addComponent(jCheckBox73)
                            .addComponent(jCheckBox72)
                            .addComponent(jCheckBox67)
                            .addComponent(jCheckBox74)))
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
                .addComponent(jCheckBox63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox66)
                    .addComponent(jCheckBox67))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox68)
                    .addComponent(jCheckBox72))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox69)
                    .addComponent(jCheckBox74))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox70)
                    .addComponent(jCheckBox73))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterWomenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox71)
                    .addComponent(jCheckBox75))
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
                .addContainerGap(37, Short.MAX_VALUE))
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

        motherpanel.add(MainWomen, "card4");

        MainSD.setPreferredSize(new java.awt.Dimension(900, 515));

        ProductSD.setBackground(new java.awt.Color(255, 255, 255));

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
        jCheckBox82.setText("ASUCS");

        jCheckBox83.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox83.setText("New Balance");

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

        jCheckBox86.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox86.setText("36");

        jCheckBox87.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox87.setText("41");

        jCheckBox88.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox88.setText("37");

        jCheckBox89.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox89.setText("38");

        jCheckBox90.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox90.setText("39");

        jCheckBox91.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox91.setText("40");

        jCheckBox92.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox92.setText("42");

        jCheckBox93.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox93.setText("44");

        jCheckBox94.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox94.setText("43");
        jCheckBox94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox94ActionPerformed(evt);
            }
        });

        jCheckBox95.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox95.setText("45");

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
                    .addComponent(jCheckBox83)
                    .addComponent(jCheckBox84)
                    .addComponent(jCheckBox85)
                    .addComponent(NewB20)
                    .addGroup(FilterSDLayout.createSequentialGroup()
                        .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox86)
                            .addComponent(jCheckBox88)
                            .addComponent(jCheckBox89)
                            .addComponent(jCheckBox90)
                            .addComponent(jCheckBox91))
                        .addGap(51, 51, 51)
                        .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox95)
                            .addComponent(jCheckBox93)
                            .addComponent(jCheckBox92)
                            .addComponent(jCheckBox87)
                            .addComponent(jCheckBox94)))
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
                .addComponent(jCheckBox83)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox84)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox85)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewB20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox86)
                    .addComponent(jCheckBox87))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox88)
                    .addComponent(jCheckBox92))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox89)
                    .addComponent(jCheckBox94))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox90)
                    .addComponent(jCheckBox93))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FilterSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox91)
                    .addComponent(jCheckBox95))
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
            .addComponent(NewBlogout)    // อยู่ในแถวเดียวกัน
            .addComponent(Title17))
        .addContainerGap(14, Short.MAX_VALUE))
);


        getContentPane().add(MenuNew11, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void Newsearch11NewsearchSearchF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Newsearch11NewsearchSearchF1ActionPerformed
        // TODO add your handling code here:
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
    String code = jTextField2.getText();
    boolean ok = pricing.applyPromoCode(code);  // คืน true ถ้าโค้ดถูกต้อง

    if (!ok) {
        javax.swing.JOptionPane.showMessageDialog(this, "Promo code ไม่ถูกต้อง");
    }
    updateCartSummary(); // รีเฟรชยอดรวมหลังใช้โค้ด
}//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(Purchase);
        motherpanel.repaint();
        motherpanel.revalidate();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(MainNew8);
        motherpanel.repaint();
        motherpanel.revalidate();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        motherpanel.removeAll();
        motherpanel.add(Confirm);
        motherpanel.repaint();
        motherpanel.revalidate();
    }//GEN-LAST:event_jButton16ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
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
    private javax.swing.JButton NewBsp11;
    private javax.swing.JButton NewBwomen11;
    private javax.swing.JPanel NewShowProduct;
    private javax.swing.JPanel NewShowProduct7;
    private javax.swing.JTextField Newsearch11;
    private javax.swing.JPanel Newshowproduct1;
    private javax.swing.JPanel Newshowproduct15;
    private javax.swing.JPanel Newshowproduct16;
    private javax.swing.JPanel Newshowproduct17;
    private javax.swing.JPanel Newshowproduct18;
    private javax.swing.JPanel Newshowproduct19;
    private javax.swing.JPanel Newshowproduct2;
    private javax.swing.JPanel Newshowproduct20;
    private javax.swing.JPanel Newshowproduct21;
    private javax.swing.JPanel Newshowproduct22;
    private javax.swing.JPanel Newshowproduct3;
    private javax.swing.JPanel Newshowproduct4;
    private javax.swing.JPanel Newshowproduct5;
    private javax.swing.JPanel Newshowproduct6;
    private javax.swing.JPanel Newshowproduct7;
    private javax.swing.JPanel Newshowproduct8;
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
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox28;
    private javax.swing.JCheckBox jCheckBox29;
    private javax.swing.JCheckBox jCheckBox30;
    private javax.swing.JCheckBox jCheckBox31;
    private javax.swing.JCheckBox jCheckBox32;
    private javax.swing.JCheckBox jCheckBox33;
    private javax.swing.JCheckBox jCheckBox34;
    private javax.swing.JCheckBox jCheckBox35;
    private javax.swing.JCheckBox jCheckBox36;
    private javax.swing.JCheckBox jCheckBox37;
    private javax.swing.JCheckBox jCheckBox38;
    private javax.swing.JCheckBox jCheckBox39;
    private javax.swing.JCheckBox jCheckBox40;
    private javax.swing.JCheckBox jCheckBox41;
    private javax.swing.JCheckBox jCheckBox42;
    private javax.swing.JCheckBox jCheckBox43;
    private javax.swing.JCheckBox jCheckBox44;
    private javax.swing.JCheckBox jCheckBox45;
    private javax.swing.JCheckBox jCheckBox46;
    private javax.swing.JCheckBox jCheckBox47;
    private javax.swing.JCheckBox jCheckBox48;
    private javax.swing.JCheckBox jCheckBox49;
    private javax.swing.JCheckBox jCheckBox50;
    private javax.swing.JCheckBox jCheckBox51;
    private javax.swing.JCheckBox jCheckBox52;
    private javax.swing.JCheckBox jCheckBox53;
    private javax.swing.JCheckBox jCheckBox54;
    private javax.swing.JCheckBox jCheckBox55;
    private javax.swing.JCheckBox jCheckBox56;
    private javax.swing.JCheckBox jCheckBox57;
    private javax.swing.JCheckBox jCheckBox58;
    private javax.swing.JCheckBox jCheckBox59;
    private javax.swing.JCheckBox jCheckBox60;
    private javax.swing.JCheckBox jCheckBox61;
    private javax.swing.JCheckBox jCheckBox62;
    private javax.swing.JCheckBox jCheckBox63;
    private javax.swing.JCheckBox jCheckBox64;
    private javax.swing.JCheckBox jCheckBox65;
    private javax.swing.JCheckBox jCheckBox66;
    private javax.swing.JCheckBox jCheckBox67;
    private javax.swing.JCheckBox jCheckBox68;
    private javax.swing.JCheckBox jCheckBox69;
    private javax.swing.JCheckBox jCheckBox70;
    private javax.swing.JCheckBox jCheckBox71;
    private javax.swing.JCheckBox jCheckBox72;
    private javax.swing.JCheckBox jCheckBox73;
    private javax.swing.JCheckBox jCheckBox74;
    private javax.swing.JCheckBox jCheckBox75;
    private javax.swing.JCheckBox jCheckBox76;
    private javax.swing.JCheckBox jCheckBox77;
    private javax.swing.JCheckBox jCheckBox78;
    private javax.swing.JCheckBox jCheckBox79;
    private javax.swing.JCheckBox jCheckBox80;
    private javax.swing.JCheckBox jCheckBox81;
    private javax.swing.JCheckBox jCheckBox82;
    private javax.swing.JCheckBox jCheckBox83;
    private javax.swing.JCheckBox jCheckBox84;
    private javax.swing.JCheckBox jCheckBox85;
    private javax.swing.JCheckBox jCheckBox86;
    private javax.swing.JCheckBox jCheckBox87;
    private javax.swing.JCheckBox jCheckBox88;
    private javax.swing.JCheckBox jCheckBox89;
    private javax.swing.JCheckBox jCheckBox90;
    private javax.swing.JCheckBox jCheckBox91;
    private javax.swing.JCheckBox jCheckBox92;
    private javax.swing.JCheckBox jCheckBox93;
    private javax.swing.JCheckBox jCheckBox94;
    private javax.swing.JCheckBox jCheckBox95;
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
    // End of variables declaration//GEN-END:variables
}
