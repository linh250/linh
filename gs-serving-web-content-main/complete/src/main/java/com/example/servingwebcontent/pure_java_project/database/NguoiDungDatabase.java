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

    //  Kiểm tra tài khoản đã tồn tại chưa
    public boolean tonTaiTaiKhoan(String taiKhoan) {
        String sql = "SELECT COUNT(*) FROM nguoi_dung WHERE tai_khoan = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, taiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean themNguoiDung(NguoiDung nd) {
        
        if (tonTaiTaiKhoan(nd.getTaiKhoan())) {
            return false;
        }

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
                    nd.setId(rs.getLong(1)); // gán id lại
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    // 🔍 Tìm người dùng theo họ tên
    public NguoiDung timTheoTen(String ten) {
        String sql = "SELECT * FROM nguoi_dung WHERE ho_ten = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ten);
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

    // 6. Tìm kiếm người dùng
    public List<NguoiDung> timKiemNguoiDung(String keyword) {
        List<NguoiDung> ketQua = new ArrayList<>();
        String sql = """
            SELECT * FROM nguoi_dung
            WHERE ho_ten LIKE ? OR tai_khoan LIKE ? OR lien_he LIKE ?
            """;

        try (Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            String tuKhoa = "%" + keyword + "%";
            ps.setString(1, tuKhoa);
            ps.setString(2, tuKhoa);
            ps.setString(3, tuKhoa);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ketQua.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    // map row → NguoiDung
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