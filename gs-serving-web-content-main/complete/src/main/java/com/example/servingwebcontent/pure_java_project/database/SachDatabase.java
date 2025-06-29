package com.example.servingwebcontent.pure_java_project.database;

import com.example.servingwebcontent.pure_java_project.model.Sach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SachDatabase {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public List<Sach> layDanhSachSach() {
        List<Sach> danhSach = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sach")) {

            while (rs.next()) {
                Sach s = new Sach();
                s.setId(rs.getInt("id"));
                s.setTen(rs.getString("ten"));
                s.setTacGia(rs.getString("tac_gia"));
                s.setDaMuon(rs.getBoolean("da_muon"));
                danhSach.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    // ✅ Thêm hàm lấy sách chưa mượn
    public List<Sach> laySachChuaMuon() {
        List<Sach> danhSach = new ArrayList<>();

        String sql = "SELECT * FROM sach WHERE da_muon = false";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sach s = new Sach();
                s.setId(rs.getInt("id"));
                s.setTen(rs.getString("ten"));
                s.setTacGia(rs.getString("tac_gia"));
                s.setDaMuon(rs.getBoolean("da_muon"));
                danhSach.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    public void themSach(Sach sach) {
        String sql = "INSERT INTO sach (ten, tac_gia, da_muon) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sach.getTen());
            stmt.setString(2, sach.getTacGia());
            stmt.setBoolean(3, sach.isDaMuon());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void xoaSach(int id) {
        String sql = "DELETE FROM sach WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Sach laySachTheoId(int id) {
        String sql = "SELECT * FROM sach WHERE id = ?";
        Sach sach = null;

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sach = new Sach();
                sach.setId(rs.getInt("id"));
                sach.setTen(rs.getString("ten"));
                sach.setTacGia(rs.getString("tac_gia"));
                sach.setDaMuon(rs.getBoolean("da_muon"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sach;
    }

    public void capNhatSach(Sach sachDaSua) {
        String sql = "UPDATE sach SET ten = ?, tac_gia = ?, da_muon = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sachDaSua.getTen());
            stmt.setString(2, sachDaSua.getTacGia());
            stmt.setBoolean(3, sachDaSua.isDaMuon());
            stmt.setInt(4, sachDaSua.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
