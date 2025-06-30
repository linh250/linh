package com.example.servingwebcontent.pure_java_project.model;

import java.io.Serializable;

public class NguoiDung implements Serializable {

    /* ===== Thuộc tính ===== */
    private Long id;
    private String hoTen;
    private Integer tuoi;
    private String gioiTinh;   // Nam / Nữ / Khác
    private String lienHe;     // SĐT hoặc địa chỉ
    private String taiKhoan;   // username
    private String matKhau;    // password (đã hash)

    /* ===== Constructors ===== */

    // Constructor rỗng – Spring / Jackson / Thymeleaf cần
    public NguoiDung() {
    }

    // Constructor đủ tham số (không có id – id được DB tự tăng)
    public NguoiDung(String hoTen, Integer tuoi, String gioiTinh,
                     String lienHe, String taiKhoan, String matKhau) {
        this.hoTen = hoTen;
        this.tuoi = tuoi;
        this.gioiTinh = gioiTinh;
        this.lienHe = lienHe;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
    }

    /* ===== Getters & Setters ===== */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Integer getTuoi() {
        return tuoi;
    }

    public void setTuoi(Integer tuoi) {
        this.tuoi = tuoi;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getLienHe() {
        return lienHe;
    }

    public void setLienHe(String lienHe) {
        this.lienHe = lienHe;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    /* ===== toString() – tiện debug ===== */
    @Override
    public String toString() {
        return "NguoiDung{" +
                "id=" + id +
                ", hoTen='" + hoTen + '\'' +
                ", tuoi=" + tuoi +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", lienHe='" + lienHe + '\'' +
                ", taiKhoan='" + taiKhoan + '\'' +
                '}';
    }
}