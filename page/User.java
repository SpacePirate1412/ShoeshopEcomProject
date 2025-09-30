package page;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class User {
    private static final String FILE_PATH = "users.csv";

    // สมัครสมาชิก (Register)
    public static boolean register(String username, String password, String email,
                                   String fullname, String house, String subdistrict,
                                   String district, String province, String zipcode,
                                   String phone, String role) throws Exception {
        // ตรวจสอบ user ซ้ำ
        if (userExists(username)) {
            throw new IllegalArgumentException("ชื่อผู้ใช้นี้ถูกใช้แล้ว!");
        }

        // ตรวจสอบ email ซ้ำ
        if (emailExists(email)) {
            throw new IllegalArgumentException("อีเมลนี้ถูกใช้แล้ว!");
        }

        // ตรวจสอบ phone ซ้ำ
        if (phoneExists(phone)) {
            throw new IllegalArgumentException("เบอร์โทรศัพท์นี้ถูกใช้แล้ว!");
        }

        File file = new File(FILE_PATH);
        boolean newFile = !file.exists() || file.length() == 0;

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(FILE_PATH, true), StandardCharsets.UTF_8))) {

            // เขียน header ถ้าไฟล์ยังไม่มี
            if (newFile) {
                bw.write("username,password,email,fullname,house,subdistrict,district,province,zipcode,phone,role");
                bw.newLine();
            }

            // เขียนข้อมูล 1 บรรทัด คั่นด้วย ,
            bw.write(String.join(",", username, password, email, fullname, house,
                    subdistrict, district, province, zipcode, phone, role));
            bw.newLine();
        }
        return true;
    }

    // เข้าสู่ระบบ (Login) → คืนค่า role (user/admin) หรือ null ถ้า fail
    public static String login(String username, String password) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line = br.readLine(); // ข้าม header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 11) { // ตอนนี้มี 11 field
                    String fileUser = parts[0].trim();
                    String filePass = parts[1].trim();
                    String fileRole = parts[10].trim(); // role อยู่ index 10

                    if (username.equals(fileUser) && password.equals(filePass)) {
                        return fileRole; // คืน role กลับมา เช่น "user" หรือ "admin"
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // login ไม่สำเร็จ
    }

    // ตรวจสอบ user ซ้ำ
    private static boolean userExists(String username) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            br.readLine(); // ข้าม header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equalsIgnoreCase(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ตรวจสอบ email ซ้ำ
    private static boolean emailExists(String email) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            br.readLine(); // ข้าม header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].equalsIgnoreCase(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ตรวจสอบ phone ซ้ำ
    private static boolean phoneExists(String phone) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            br.readLine(); // ข้าม header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10 && parts[9].equals(phone)) {
                    return true;
                }
            }
        }
        return false;
    }
}
