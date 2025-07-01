package com.example.servingwebcontent.pure_java_project.database;

import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class NguoiDungDatabase {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUser;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    // 1. Thêm người dùng mới
    public long themNguoiDung(NguoiDung nd) {
        String sql = """
            INSERT INTO nguoi_dung
              (ho_ten, tuoi, gioi_tinh, lien_he, tai_khoan, mat_khau)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nd.getHoTen());
            ps.setInt   (2, nd.getTuoi());
            ps.setString(3, nd.getGioiTinh());
            ps.setString(4, nd.getLienHe());
            ps.setString(5, nd.getTaiKhoan());
            ps.setString(6, nd.getMatKhau());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    nd.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 2. Lấy người dùng theo id
    public NguoiDung layNguoiDungTheoId(long id) {
        String sql = "SELECT * FROM nguoi_dung WHERE id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Lấy toàn bộ người dùng
    public List<NguoiDung> layTatCaNguoiDung() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoi_dung";
        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. Xoá người dùng theo id
    public void xoaNguoiDung(long id) {
        String sql = "DELETE FROM nguoi_dung WHERE id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Cập nhật người dùng
    public void capNhatNguoiDung(NguoiDung nd) {
        String sql = """
            UPDATE nguoi_dung
            SET ho_ten = ?, tuoi = ?, gioi_tinh = ?, lien_he = ?, tai_khoan = ?, mat_khau = ?
            WHERE id = ?
            """;
        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nd.getHoTen());
            ps.setInt(2, nd.getTuoi());
            ps.setString(3, nd.getGioiTinh());
            ps.setString(4, nd.getLienHe());
            ps.setString(5, nd.getTaiKhoan());
            ps.setString(6, nd.getMatKhau());
            ps.setLong(7, nd.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // helper: map ResultSet -> NguoiDung
    private NguoiDung mapRow(ResultSet rs) throws SQLException {
        NguoiDung nd = new NguoiDung();
        nd.setId       (rs.getLong("id"));
        nd.setHoTen    (rs.getString("ho_ten"));
        nd.setTuoi     (rs.getInt("tuoi"));
        nd.setGioiTinh (rs.getString("gioi_tinh"));
        nd.setLienHe   (rs.getString("lien_he"));
        nd.setTaiKhoan (rs.getString("tai_khoan"));
        nd.setMatKhau  (rs.getString("mat_khau"));
        return nd;
    }
}