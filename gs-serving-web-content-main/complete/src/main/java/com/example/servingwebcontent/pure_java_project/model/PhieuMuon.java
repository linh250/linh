package com.example.servingwebcontent.pure_java_project.model;

import java.time.LocalDate;

public class PhieuMuon {

    private int id;
    private String tenNguoiMuon;
    private String tenSach;
    private LocalDate ngayMuon;
    private LocalDate ngayTra;

    public PhieuMuon() {
    }

    public PhieuMuon(int id, String tenNguoiMuon, String tenSach, LocalDate ngayMuon, LocalDate ngayTra) {
        this.id = id;
        this.tenNguoiMuon = tenNguoiMuon;
        this.tenSach = tenSach;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    // Getter & Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenNguoiMuon() {
        return tenNguoiMuon;
    }

    public void setTenNguoiMuon(String tenNguoiMuon) {
        this.tenNguoiMuon = tenNguoiMuon;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
    }
}