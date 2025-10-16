package page;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class address {
    public static Map<String, Map<String, Map<String, String>>> loadAddresses(String path) {
        Map<String, Map<String, Map<String, String>>> data = new HashMap<>();

        try {
            //  ลองหาใน classpath ก่อน
            InputStream in = address.class.getResourceAsStream("/page/thai_addresses.csv");
            if (in == null) in = address.class.getResourceAsStream("/thai_addresses.csv");
            //  ถ้าไม่เจอจริง ๆ ค่อยอ่านจากpathที่ส่งมา
            if (in == null) in = new FileInputStream(path);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String header = br.readLine(); // ข้าม header
                if (header == null) return data;

                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",", -1); // เก็บค่าว่างท้ายบรรทัดด้วย
                    if (parts.length < 4) continue;

                    String province    = parts[0].trim();
                    String district    = parts[1].trim();
                    String subdistrict = parts[2].trim();
                    String zipcode     = parts[3].trim();
                    if (province.isEmpty() || district.isEmpty() || subdistrict.isEmpty() || zipcode.isEmpty()) continue;

                    data.computeIfAbsent(province, k -> new HashMap<>())
                        .computeIfAbsent(district, k -> new HashMap<>())
                        .put(subdistrict, zipcode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
