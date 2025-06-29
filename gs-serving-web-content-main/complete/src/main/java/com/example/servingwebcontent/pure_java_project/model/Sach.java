package com.example.servingwebcontent.pure_java_project.model;

public class Sach {

    private int id;
    private String ma;         // ✅ Mã sách để binding trong form
    private String tenSach;
    private String tacGia;
    private boolean daMuon = false;

    // ✅ Constructor rỗng – BẮT BUỘC CHO Thymeleaf
    public Sach() {}

    // ✅ Constructor đầy đủ 4 tham số
    public Sach(int id, String ma, String tenSach, String tacGia) {
        this.id = id;
        this.ma = ma;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
    }

    // ✅ Constructor cũ (giữ lại để không lỗi chỗ cũ)
    public Sach(int id, String tenSach, String tacGia) {
        this.id = id;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }
    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenSach() {
        return tenSach;
    }
    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }
    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public boolean isDaMuon() {
        return daMuon;
    }
    public void setDaMuon(boolean daMuon) {
        this.daMuon = daMuon;
    }
}