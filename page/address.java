package page;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class address {
    public static Map<String, Map<String, Map<String, String>>> loadAddresses(String path) {
        Map<String, Map<String, Map<String, String>>> data = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), "UTF-8"))) { // <<< บังคับ UTF-8

            String line;
            br.readLine(); // ข้าม header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                String province = parts[0].trim();
                String district = parts[1].trim();
                String subdistrict = parts[2].trim();
                String zipcode = parts[3].trim();

                data.computeIfAbsent(province, k -> new HashMap<>())
                    .computeIfAbsent(district, k -> new HashMap<>())
                    .put(subdistrict, zipcode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
