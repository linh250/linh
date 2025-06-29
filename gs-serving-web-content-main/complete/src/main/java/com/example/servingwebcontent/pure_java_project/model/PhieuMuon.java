package com.example.servingwebcontent.pure_java_project.model;

import java.time.LocalDate;

public class PhieuMuon {

    private int id;               // ⭐ ID tự tăng/khóa chính
    private NguoiDung nguoiDung;
    private Sach sach;
    private LocalDate ngayMuon;
    private LocalDate ngayTra;
    private boolean daTra;        // true = đã trả

    /* ====== Constructors ====== */
    public PhieuMuon() {
        this.nguoiDung = new NguoiDung();
        this.sach = new Sach();
        this.ngayMuon = LocalDate.now();  // mượn hôm nay
        this.daTra = false;
    }

    /* ====== Getters & Setters ====== */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }
    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public Sach getSach() {
        return sach;
    }
    public void setSach(Sach sach) {
        this.sach = sach;
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

    public boolean isDaTra() {
        return daTra;
    }
    public void setDaTra(boolean daTra) {
        this.daTra = daTra;
    }
}