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
    private String jdbcUser;

    @Value("${spring.datasource.password}")
    private String jdbcPass;

    // 1. Thêm sách
    public void themSach(Sach sachMoi) {
        String sql = "INSERT INTO sach (ten_sach, tac_gia, da_muon) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sachMoi.getTenSach());
            ps.setString(2, sachMoi.getTacGia());
            ps.setBoolean(3, sachMoi.isDaMuon());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. Cập nhật sách
    public void capNhatSach(Sach s) {
        String sql = "UPDATE sach SET ten_sach = ?, tac_gia = ?, da_muon = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getTenSach());
            ps.setString(2, s.getTacGia());
            ps.setBoolean(3, s.isDaMuon());
            ps.setInt(4, s.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. Xoá sách
    public void xoaSach(int id) {
        String sql = "DELETE FROM sach WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. Lấy toàn bộ danh sách sách
    public List<Sach> layDanhSachSach() {
        List<Sach> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sach";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sach s = new Sach();
                s.setId(rs.getInt("id"));
                s.setTenSach(rs.getString("ten_sach"));
                s.setTacGia(rs.getString("tac_gia"));
                s.setDaMuon(rs.getBoolean("da_muon"));
                danhSach.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    // 5. Lấy 1 sách theo ID
    public Sach laySachTheoId(int id) {
        String sql = "SELECT * FROM sach WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Sach s = new Sach();
                s.setId(rs.getInt("id"));
                s.setTenSach(rs.getString("ten_sach"));
                s.setTacGia(rs.getString("tac_gia"));
                s.setDaMuon(rs.getBoolean("da_muon"));
                return s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
