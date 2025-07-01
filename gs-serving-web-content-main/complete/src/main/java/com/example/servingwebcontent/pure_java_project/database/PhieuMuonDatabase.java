package com.example.servingwebcontent.pure_java_project.database;

import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PhieuMuonDatabase {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUser;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    public PhieuMuonDatabase() {}

    // ✅ Thêm phiếu mượn (có nguoi_dung_id)
    public void themPhieuMuon(PhieuMuon phieu) {
        String sql = "INSERT INTO phieu_muon (ten_nguoi_muon, ten_sach, tac_gia, ngay_muon, ngay_tra, da_tra, nguoi_dung_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, phieu.getTenNguoiMuon());
            stmt.setString(2, phieu.getTenSach());
            stmt.setString(3, phieu.getTacGia());
            stmt.setDate(4, Date.valueOf(phieu.getNgayMuon()));
            stmt.setDate(5, Date.valueOf(phieu.getNgayTra()));
            stmt.setBoolean(6, phieu.isDaTra());
            stmt.setLong(7, phieu.getNguoiDungId());

            stmt.executeUpdate();

            // ✅ Gán lại ID được sinh tự động từ DB
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    phieu.setId(id);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Lấy tất cả phiếu mượn của một người dùng
    public List<PhieuMuon> layPhieuMuonTheoNguoiDung(long nguoiDungId) {
        List<PhieuMuon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieu_muon WHERE nguoi_dung_id = ? ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, nguoiDungId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    // ✅ Lấy 1 phiếu theo id
    public PhieuMuon layPhieuMuonTheoId(int id) {
        String sql = "SELECT * FROM phieu_muon WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Cập nhật phiếu mượn
    public void capNhatPhieuMuon(PhieuMuon phieu) {
        String sql = "UPDATE phieu_muon SET ten_nguoi_muon=?, ten_sach=?, tac_gia=?, ngay_muon=?, ngay_tra=?, da_tra=?, nguoi_dung_id=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phieu.getTenNguoiMuon());
            stmt.setString(2, phieu.getTenSach());
            stmt.setString(3, phieu.getTacGia());
            stmt.setDate(4, Date.valueOf(phieu.getNgayMuon()));
            stmt.setDate(5, Date.valueOf(phieu.getNgayTra()));
            stmt.setBoolean(6, phieu.isDaTra());
            stmt.setLong(7, phieu.getNguoiDungId());
            stmt.setInt(8, phieu.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Xoá phiếu mượn
    public void xoaPhieuMuon(int id) {
        String sql = "DELETE FROM phieu_muon WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Hàm dùng chung để chuyển ResultSet thành đối tượng PhieuMuon
    private PhieuMuon mapRow(ResultSet rs) throws SQLException {
        PhieuMuon p = new PhieuMuon();
        p.setId(rs.getInt("id"));
        p.setTenNguoiMuon(rs.getString("ten_nguoi_muon"));
        p.setTenSach(rs.getString("ten_sach"));
        p.setTacGia(rs.getString("tac_gia"));
        p.setNgayMuon(rs.getDate("ngay_muon").toLocalDate());
        p.setNgayTra(rs.getDate("ngay_tra").toLocalDate());
        p.setDaTra(rs.getBoolean("da_tra"));
        p.setNguoiDungId(rs.getLong("nguoi_dung_id"));
        return p;
    }
}