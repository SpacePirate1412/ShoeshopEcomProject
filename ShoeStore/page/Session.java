package page;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Session {
    private static final String SESSION_FILE = "session.csv";

    // บันทึก session (username + role)
    public static void saveSession(String username, String role) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(SESSION_FILE), StandardCharsets.UTF_8))) {

            // เขียน header
            bw.write("username,role");
            bw.newLine();

            // เขียนข้อมูล
            bw.write(username + "," + role);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // โหลด session ถ้ามี → return [username, role]
    public static String[] loadSession() {
        File file = new File(SESSION_FILE);
        if (!file.exists() || file.length() == 0) {
            return null; // ไม่มี session
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line = br.readLine(); // header
            if (line == null) return null;

            line = br.readLine(); // ข้อมูลจริง
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    return new String[]{parts[0].trim(), parts[1].trim()};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ลบ session (ตอน logout)
    public static void clearSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete(); // ลบไฟล์ทิ้ง
        }
    }
}
