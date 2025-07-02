package com.example.servingwebcontent.pure_java_project.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PhieuMuon {
    private int id;
    private String tenNguoiMuon;
    private String tenSach;
    private String tacGia;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayMuon;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayTra;

    private boolean daTra;

 
    private Long nguoiDungId;

    public PhieuMuon() {
        this.ngayMuon = LocalDate.now();
        this.daTra = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenNguoiMuon() { return tenNguoiMuon; }
    public void setTenNguoiMuon(String tenNguoiMuon) { this.tenNguoiMuon = tenNguoiMuon; }

    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }

    public String getTacGia() { return tacGia; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }

    public LocalDate getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(LocalDate ngayMuon) { this.ngayMuon = ngayMuon; }

    public LocalDate getNgayTra() { return ngayTra; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }

    public boolean isDaTra() { return daTra; }
    public void setDaTra(boolean daTra) { this.daTra = daTra; }

    public Long getNguoiDungId() { return nguoiDungId; }
    public void setNguoiDungId(Long nguoiDungId) { this.nguoiDungId = nguoiDungId; }
}