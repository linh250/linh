package com.example.servingwebcontent.pure_java_project.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

/** Kết nối MySQL Aiven và in dữ liệu bảng sach. */
@Component
public class AivenConnection {

    // Lấy cấu hình từ application.properties (hoặc biến môi trường)
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUser;

    @Value("${aiven.db.password}")
    private String jdbcPass;

    /** Kết nối DB, đọc bảng sach và in ra console. */
    public void testConnection() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery("SELECT * FROM sach")) {

            System.out.println("===> Dữ liệu bảng sach:");
            while (rs.next()) {
                int     id     = rs.getInt("id");
                String  ten    = rs.getString("ten_sach");
                String  tacGia = rs.getString("tac_gia");
                boolean daMuon = rs.getBoolean("da_muon");
                System.out.printf("%d | %s | %s | %s%n",
                        id, ten, tacGia, daMuon ? "Đã mượn" : "Chưa mượn");
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối hoặc truy vấn:");
            e.printStackTrace();
        }
    }
}

