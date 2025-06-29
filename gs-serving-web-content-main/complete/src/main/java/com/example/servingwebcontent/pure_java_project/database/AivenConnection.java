package com.example.servingwebcontent.pure_java_project.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AivenConnection {

    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPass;

    // Spring sẽ inject qua constructor
    public AivenConnection(@Value("${DB_URL}")  String jdbcUrl,
                           @Value("${DB_USER}") String jdbcUser,
                           @Value("${DB_PASS}") String jdbcPass) {
        this.jdbcUrl  = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPass = jdbcPass;
    }

    @PostConstruct
    public void testConnection() {
        String sql = "SELECT NOW() AS server_time";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("✅ Kết nối Aiven thành công – Server time: "
                                   + rs.getString("server_time"));
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối Aiven:");
            e.printStackTrace();
        }
    }
}