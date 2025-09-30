package page;

import javax.swing.*;

public class SessionChecker {
    public static void start() {
        SwingUtilities.invokeLater(() -> {
            String[] session = Session.loadSession();

            if (session != null) {
                // ถ้ามี session
                if ("admin".equalsIgnoreCase(session[1])) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new AllProducts().setVisible(true);
                }
            } else {
                //ไม่มี session
                new ShoeStoreLogin().setVisible(true);
            }
        });
    }
}
