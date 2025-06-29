package com.example.servingwebcontent.pure_java_project.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Bean Spring quản lý kết nối Aiven MySQL.
 * Lấy cấu hình từ spring.datasource.* (đã ánh xạ tới biến .env).
 */
@Component
public class AivenConnection {

    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPass;

    // Spring inject qua constructor
    public AivenConnection(@Value("${spring.datasource.url}")      String jdbcUrl,
                           @Value("${spring.datasource.username}") String jdbcUser,
                           @Value("${spring.datasource.password}") String jdbcPass) {
        this.jdbcUrl  = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPass = jdbcPass;
    }

    /** Phương thức test auto khi ứng dụng khởi động */
    @PostConstruct
    public void testConnection() {
        String sql = "SELECT NOW() AS server_time";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             Statement st  = conn.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("✅ Đã kết nối Aiven MySQL – Server time: "
                                   + rs.getString("server_time"));
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối Aiven:");
            e.printStackTrace();
        }
    }

    /** Trả về một Connection mới khi cần */
    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
    }
}
