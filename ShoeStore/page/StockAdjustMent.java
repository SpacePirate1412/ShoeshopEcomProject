/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package page;

import java.io.FileInputStream; // ProductCatalog, Session, etc.
import java.io.ObjectInputStream;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lib.*;

public class StockAdjustMent extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(StockAdjustMent.class.getName());

    // ===== NEW: ผูกกับแคตตาล็อกสินค้า =====
    private final ProductCatalog catalog;
    // --- Promo code CSV ---
    private static final java.nio.file.Path PROMO_CSV =
        java.nio.file.Paths.get("lib", "discount", "promocodes.csv");
        private javax.swing.JTextField tfPromoCode;
        private javax.swing.JTextField tfPromoPercent;
        private javax.swing.JButton btnPromoAdd;
        private javax.swing.JButton btnPromoClear;


    // สร้างด้วยแคตตาล็อกใหม่ (อ่านจาก stock.csv อัตโนมัติ)
    public StockAdjustMent() {
        this(new ProductCatalog());
    }

    // รับ catalog จากภายนอก (เผื่อหน้าอื่นส่งมา)
    public StockAdjustMent(ProductCatalog catalog) {
    this.catalog = (catalog != null) ? catalog : new ProductCatalog();
    initComponents();

    // เปิดมาแบบเต็มจอ เหมือนหน้าร้าน
    setLocationRelativeTo(null);
    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    setResizable(true);
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        tfSku = new javax.swing.JTextField();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        tfBrand = new javax.swing.JTextField();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        tfPrice = new javax.swing.JTextField();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        cbGender = new javax.swing.JComboBox<>();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        tfDiscount = new javax.swing.JTextField();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        tfQuantity = new javax.swing.JTextField();
        javax.swing.JButton btnAdd = new javax.swing.JButton();
        javax.swing.JButton btnClear = new javax.swing.JButton();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        tableStock = new javax.swing.JTable();
        javax.swing.JButton btnDelete = new javax.swing.JButton();
        javax.swing.JButton btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StockManagement");
        setMinimumSize(new java.awt.Dimension(801, 467));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(170, 167, 173));

        jLabel1.setBackground(new java.awt.Color(170, 167, 173));
        jLabel1.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/box.png"))); // NOI18N
        jLabel1.setText("Stock Management");

        jPanel2.setOpaque(false);

        jLabel2.setText("SKU");

        tfSku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfSkuActionPerformed(evt);
            }
        });

        jLabel3.setText("Product Name");

        tfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfNameActionPerformed(evt);
            }
        });

        jLabel4.setText("Brand");

        tfBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfBrandActionPerformed(evt);
            }
        });

        jLabel5.setText("Price");

        tfPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPriceActionPerformed(evt);
            }
        });

        jLabel6.setText("Gender");

        cbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Men", "Women" }));
        cbGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGenderActionPerformed(evt);
            }
        });

        jLabel7.setText("Discount");

        tfDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfDiscountActionPerformed(evt);
            }
        });

        jLabel8.setText("Quantity");

        tfQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfQuantityActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/more.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/broom.png"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        // ====== เพิ่มชุดคอมโพเนนต์สำหรับ Promo code ======
javax.swing.JLabel lblPromoTitle = new javax.swing.JLabel();
lblPromoTitle.setFont(new java.awt.Font("Cambria", 1, 14));
lblPromoTitle.setText("Promo code");

javax.swing.JLabel lblCode = new javax.swing.JLabel();
lblCode.setText("Code");

tfPromoCode = new javax.swing.JTextField();

javax.swing.JLabel lblPercent = new javax.swing.JLabel();
lblPercent.setText("Percent (%)");

tfPromoPercent = new javax.swing.JTextField();

btnPromoAdd = new javax.swing.JButton();
btnPromoAdd.setText("Add Promo");
btnPromoAdd.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnPromoAddActionPerformed(evt);
    }
});

btnPromoClear = new javax.swing.JButton();
btnPromoClear.setText("Clear");
btnPromoClear.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        tfPromoCode.setText("");
        tfPromoPercent.setText("");
    }
});

// ====== Layout ใหม่ของ jPanel2: เพิ่มกลุ่ม Promo code ไว้ล่างสุด ======
javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
jPanel2.setLayout(jPanel2Layout);
jPanel2Layout.setHorizontalGroup(
    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tfBrand)
                .addComponent(tfSku)
                .addComponent(tfName)
                .addComponent(tfPrice)
                .addComponent(cbGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tfDiscount)
                .addComponent(tfQuantity)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                // ----- Promo code block -----
                .addComponent(lblPromoTitle)
                .addComponent(lblCode)
                .addComponent(tfPromoCode)
                .addComponent(lblPercent)
                .addComponent(tfPromoPercent)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(btnPromoAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(btnPromoClear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            )
            .addContainerGap())
);
jPanel2Layout.setVerticalGroup(
    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfSku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel5)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel6)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel7)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel8)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnAdd)
                .addComponent(btnClear))
            .addGap(12, 12, 12)
            // ----- Promo code block -----
            .addComponent(lblPromoTitle)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(lblCode)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfPromoCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(lblPercent)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tfPromoPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnPromoAdd)
                .addComponent(btnPromoClear))
            .addContainerGap(11, Short.MAX_VALUE))
);

        tableStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SKU", "Name", "Brand", "Price", "Gender", "Discount", "Quantity"
            }
        ));
        jScrollPane1.setViewportView(tableStock);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/delete.png")));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page/Picture/logout.png")));
        btnLogout.setText("Log out");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    saveAndReload(); // NEW: บันทึก CSV + reload catalog
                    Session.clearSession();
                    JOptionPane.showMessageDialog(StockAdjustMent.this, "Log Out!");
                    StockAdjustMent.this.dispose();
                    new ShoeStoreLogin().setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        StockAdjustMent.this,
                        "Stock save ERROR: " + ex.getMessage(),
                        "Save error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogout)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnDelete)
                    .addComponent(btnLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfNameActionPerformed(java.awt.event.ActionEvent evt) { }
    private void tfBrandActionPerformed(java.awt.event.ActionEvent evt) { }
    private void tfPriceActionPerformed(java.awt.event.ActionEvent evt) { }
    private void tfDiscountActionPerformed(java.awt.event.ActionEvent evt) { }
    private void tfQuantityActionPerformed(java.awt.event.ActionEvent evt) { }
    private void tfSkuActionPerformed(java.awt.event.ActionEvent evt) { }
    private void cbGenderActionPerformed(java.awt.event.ActionEvent evt) { }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
    String sku = tfSku.getText().trim();
    String name = tfName.getText().trim();
    String brand = tfBrand.getText().trim();
    String price = tfPrice.getText().trim();
    String gender = (String) cbGender.getSelectedItem();
    String discount = tfDiscount.getText().trim();
    String quantity = tfQuantity.getText().trim();

    if (sku.isEmpty() || name.isEmpty() || brand.isEmpty() ||
        price.isEmpty() || discount.isEmpty() || quantity.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter all fields", "Try again",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    // === เช็คให้เป็นตัวเลข ===
    if (!isDouble(price)) {
        JOptionPane.showMessageDialog(this, "Price must be number.", "Invalid number",
                JOptionPane.ERROR_MESSAGE);
        tfPrice.requestFocus(); return;
    }
    if (!isDouble(discount)) {
        JOptionPane.showMessageDialog(this, "Discount must be number.", "Invalid number",
                JOptionPane.ERROR_MESSAGE);
        tfDiscount.requestFocus(); return;
    }
    if (!isInteger(quantity)) {
        JOptionPane.showMessageDialog(this, "Quantity must be integer.", "Invalid number",
                JOptionPane.ERROR_MESSAGE);
        tfQuantity.requestFocus(); return;
    }

    DefaultTableModel model = (DefaultTableModel) tableStock.getModel();
    model.addRow(new Object[]{ sku, name, brand, price, gender, discount, quantity });

    tfSku.setText(""); tfName.setText(""); tfBrand.setText("");
    tfPrice.setText(""); tfDiscount.setText(""); tfQuantity.setText("");

    try { saveAndReload(); } catch (Exception ex) { ex.printStackTrace(); }
}

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        tfSku.setText(""); tfName.setText(""); tfBrand.setText("");
        tfPrice.setText(""); tfDiscount.setText(""); tfQuantity.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try { saveAndReload(); } catch (Exception ex) { ex.printStackTrace(); }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            java.io.File csv = new java.io.File("stock.csv");
            if (csv.exists()) {
                loadFromCSV(csv);
            } else {
                // fallback legacy (ถ้ามี)
                try {
                    FileInputStream stock = new FileInputStream("stock.csv");
                    ObjectInputStream input = new ObjectInputStream(stock);
                    Vector<Vector> tableData = (Vector<Vector>) input.readObject();
                    input.close(); stock.close();
                    DefaultTableModel model = (DefaultTableModel) tableStock.getModel();
                    for (int i = 0; i < tableData.size(); i++) {
                        Vector row = tableData.get(i);
                        model.addRow(new Object[]{row.get(0), row.get(1), row.get(2),
                                row.get(3), row.get(4), row.get(5), row.get(6)});
                    }
                    saveToCSV(csv);
                } catch (Exception ignoreBin) { /* no data */ }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load stock: " + ex.getMessage());
        }
    }//GEN-LAST:event_formWindowOpened

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tableStock.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "No row is selected! Please select one row",
                    "Select row",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultTableModel model = (DefaultTableModel) tableStock.getModel();
            model.removeRow(row);
            // NEW: save + reload หลังลบ
            try { saveAndReload(); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
//promocode
    private void btnPromoAddActionPerformed(java.awt.event.ActionEvent evt) {
    String code = (tfPromoCode.getText() == null) ? "" : tfPromoCode.getText().trim();
    String pct  = (tfPromoPercent.getText() == null) ? "" : tfPromoPercent.getText().trim();

    if (code.isEmpty() || pct.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter promo code and percent.");
        return;
    }
    if (!isDouble(pct)) {
        JOptionPane.showMessageDialog(this, "Percent must be number.");
        tfPromoPercent.requestFocus();
        return;
    }
    double percent = Double.parseDouble(pct);
    if (percent <= 0 || percent > 100) {
        JOptionPane.showMessageDialog(this, "Percent must be 0-100.");
        tfPromoPercent.requestFocus();
        return;
    }
    try {
        upsertPromoCodeToCsv(code.toUpperCase(java.util.Locale.ROOT), percent);
        JOptionPane.showMessageDialog(this, "Saved promo code.");
        tfPromoCode.setText("");
        tfPromoPercent.setText("");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Save promo failed: " + ex.getMessage());
    }
}

private static void ensurePromoCsvExists() throws java.io.IOException {
    java.nio.file.Files.createDirectories(PROMO_CSV.getParent());
    if (!java.nio.file.Files.exists(PROMO_CSV)) {
        try (java.io.BufferedWriter w = java.nio.file.Files.newBufferedWriter(
                PROMO_CSV, java.nio.charset.StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.CREATE)) {
            w.write('\uFEFF');                 // BOM เพื่อกันภาษาไทยเพี้ยน
            w.write("code,percent");
            w.newLine();
        }
    }
}

private void upsertPromoCodeToCsv(String code, double percent) throws java.io.IOException {
    ensurePromoCsvExists();
    java.util.List<String> lines = java.nio.file.Files.readAllLines(
            PROMO_CSV, java.nio.charset.StandardCharsets.UTF_8);

    if (!lines.isEmpty()) {
        String h = lines.get(0).replace("\uFEFF","").trim().toLowerCase();
        if (!h.startsWith("code")) {
            lines.add(0, "code,percent");
        }
    } else {
        lines.add("code,percent");
    }

    boolean updated = false;
    for (int i = 1; i < lines.size(); i++) {
        String ln = lines.get(i).trim();
        if (ln.isEmpty()) continue;
        String[] cols = parseCSVLine(ln);
        if (cols.length > 0 && cols[0].trim().equalsIgnoreCase(code)) {
            lines.set(i, code + "," + percent);
            updated = true;
            break;
        }
    }
    if (!updated) {
        lines.add(code + "," + percent);
    }

    try (java.io.BufferedWriter w = java.nio.file.Files.newBufferedWriter(
            PROMO_CSV, java.nio.charset.StandardCharsets.UTF_8,
            java.nio.file.StandardOpenOption.CREATE,
            java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)) {
        w.write('\uFEFF'); // BOM
        for (String ln : lines) {
            w.write(ln);
            w.newLine();
        }
    }
}

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new StockAdjustMent().setVisible(true));
    }

    // ==== CSV helpers ====
    private static String csvEscape(String s) {
        if (s == null) return "";
        String t = s.replace("\"", "\"\"");
        return "\"" + t + "\"";
    }

    private static String[] parseCSVLine(String line) {
        java.util.List<String> out = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuote) {
                if (c == '\"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        cur.append('\"'); i++;
                    } else inQuote = false;
                } else cur.append(c);
            } else {
                if (c == '\"') inQuote = true;
                else if (c == ',') { out.add(cur.toString()); cur.setLength(0); }
                else cur.append(c);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    private void saveToCSV(java.io.File file) throws Exception {
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) tableStock.getModel();
        try (java.io.PrintWriter pw = new java.io.PrintWriter(
                new java.io.OutputStreamWriter(
                        new java.io.FileOutputStream(file),
                        java.nio.charset.StandardCharsets.UTF_8))) {
            pw.println("SKU,Name,Brand,Price,Gender,Discount,Quantity");
            for (int r = 0; r < model.getRowCount(); r++) {
                String sku = String.valueOf(model.getValueAt(r,0));
                String name = String.valueOf(model.getValueAt(r,1));
                String brand = String.valueOf(model.getValueAt(r,2));
                String price = String.valueOf(model.getValueAt(r,3));
                String gender = String.valueOf(model.getValueAt(r,4));
                String discount = String.valueOf(model.getValueAt(r,5));
                String quantity = String.valueOf(model.getValueAt(r,6));
                pw.println(String.join(",",
                        csvEscape(sku), csvEscape(name), csvEscape(brand),
                        csvEscape(price), csvEscape(gender),
                        csvEscape(discount), csvEscape(quantity)
                ));
            }
        }
    }

    private void loadFromCSV(java.io.File file) throws Exception {
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) tableStock.getModel();
        model.setRowCount(0);
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(
                        new java.io.FileInputStream(file),
                        java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    String l = line.trim().toLowerCase();
                    if (l.startsWith("sku,") || l.equals("sku")) continue;
                }
                if (line.trim().isEmpty()) continue;
                String[] cols = parseCSVLine(line);
                String[] row = new String[7];
                for (int i = 0; i < 7; i++) row[i] = i < cols.length ? cols[i] : "";
                model.addRow(row);
            }
        }
    }
    // ==== end CSV helpers ====

    // ===== NEW: helper บันทึกและรีโหลดแคตตาล็อก =====
    private void saveAndReload() throws Exception {
        if (tableStock.isEditing()) {
            tableStock.getCellEditor().stopCellEditing();
        }
        saveToCSV(new java.io.File("stock.csv")); // บันทึก
        catalog.reload();                         // รีโหลดจากไฟล์
    }
    // ===== numeric helpers =====
    private static boolean isDouble(String s) {
        if (s == null) return false;
        try { Double.parseDouble(s.trim()); return true; }
        catch (NumberFormatException e) { return false; }
    }

    private static boolean isInteger(String s) {
        if (s == null) return false;
        try { Integer.parseInt(s.trim()); return true; }
        catch (NumberFormatException e) { return false; }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbGender;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTable tableStock;
    private javax.swing.JTextField tfBrand;
    private javax.swing.JTextField tfDiscount;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfPrice;
    private javax.swing.JTextField tfQuantity;
    private javax.swing.JTextField tfSku;
    // End of variables declaration//GEN-END:variables
}
