package page;

import java.io.*;

public class User {
    private static final String FILE_PATH = "users.txt";

    // สมัครสมาชิก
    public static boolean register(String username, String password, String email,
                                   String fullname, String house, String subdistrict,
                                   String district, String province, String zipcode,
                                   String phone) {
        try {
            if (userExists(username)) {
                throw new IllegalArgumentException("ชื่อผู้ใช้นี้ถูกใช้แล้ว!");
            }

            // ✅ เช็ครหัสผ่านขั้นต่ำ 8 ตัว
            if (password.length() < 8) {
                throw new IllegalArgumentException("รหัสผ่านต้องมีอย่างน้อย 8 ตัวอักษร!");
            }

            FileWriter fw = new FileWriter(FILE_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // เขียนข้อมูลเป็น 1 บรรทัด คั่นด้วย ,
            bw.write(String.join(",", username, password, email, fullname, house,
                    subdistrict, district, province, zipcode, phone));
            bw.newLine();
            bw.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // เข้าสู่ระบบ
    public static boolean login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String fileUser = parts[0].trim();
                    String filePass = parts[1].trim();
                    if (username.equals(fileUser) && password.equals(filePass)) {
                        return true; // login ผ่าน
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // login ไม่สำเร็จ
    }

    // ตรวจสอบ user ซ้ำ
    private static boolean userExists(String username) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }
}
