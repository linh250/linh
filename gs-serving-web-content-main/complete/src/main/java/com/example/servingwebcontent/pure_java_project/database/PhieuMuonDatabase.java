package com.example.servingwebcontent.pure_java_project.database;

import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuMuonDatabase {

    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPassword;

    public PhieuMuonDatabase() {
        jdbcUrl = System.getenv("DB_URL");
        jdbcUser = System.getenv("DB_USER");
        jdbcPassword = System.getenv("DB_PASS");
    }

    // Thêm phiếu mượn
    public void themPhieuMuon(PhieuMuon phieu) {
        String sql = "INSERT INTO phieu_muon (ten_nguoi_muon, ten_sach, tac_gia, ngay_muon, ngay_tra, da_tra) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phieu.getTenNguoiMuon());
            stmt.setString(2, phieu.getTenSach());
            stmt.setString(3, phieu.getTacGia());
            stmt.setDate(4, Date.valueOf(phieu.getNgayMuon()));
            stmt.setDate(5, Date.valueOf(phieu.getNgayTra()));
            stmt.setBoolean(6, phieu.isDaTra());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy toàn bộ danh sách phiếu mượn
    public List<PhieuMuon> layTatCaPhieuMuon() {
        List<PhieuMuon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieu_muon ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PhieuMuon p = new PhieuMuon();
                p.setId(rs.getInt("id"));
                p.setTenNguoiMuon(rs.getString("ten_nguoi_muon"));
                p.setTenSach(rs.getString("ten_sach"));
                p.setTacGia(rs.getString("tac_gia"));
                p.setNgayMuon(rs.getDate("ngay_muon").toLocalDate());
                p.setNgayTra(rs.getDate("ngay_tra").toLocalDate());
                p.setDaTra(rs.getBoolean("da_tra"));
                danhSach.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    // Lấy phiếu mượn theo ID (cho trang sửa)
    public PhieuMuon layPhieuMuonTheoId(int id) {
        String sql = "SELECT * FROM phieu_muon WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PhieuMuon p = new PhieuMuon();
                    p.setId(rs.getInt("id"));
                    p.setTenNguoiMuon(rs.getString("ten_nguoi_muon"));
                    p.setTenSach(rs.getString("ten_sach"));
                    p.setTacGia(rs.getString("tac_gia"));
                    p.setNgayMuon(rs.getDate("ngay_muon").toLocalDate());
                    p.setNgayTra(rs.getDate("ngay_tra").toLocalDate());
                    p.setDaTra(rs.getBoolean("da_tra"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật phiếu mượn (cho chức năng sửa)
    public void capNhatPhieuMuon(PhieuMuon phieu) {
        String sql = "UPDATE phieu_muon SET ten_nguoi_muon=?, ten_sach=?, tac_gia=?, ngay_muon=?, ngay_tra=?, da_tra=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phieu.getTenNguoiMuon());
            stmt.setString(2, phieu.getTenSach());
            stmt.setString(3, phieu.getTacGia());
            stmt.setDate(4, Date.valueOf(phieu.getNgayMuon()));
            stmt.setDate(5, Date.valueOf(phieu.getNgayTra()));
            stmt.setBoolean(6, phieu.isDaTra());
            stmt.setInt(7, phieu.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xoá phiếu mượn theo ID
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
}


