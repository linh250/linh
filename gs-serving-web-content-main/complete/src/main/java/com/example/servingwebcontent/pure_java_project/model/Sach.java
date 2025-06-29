package com.example.servingwebcontent.pure_java_project.model;

public class Sach {
    private int id;
    private String ten;       // Sửa từ 'tenSach' thành 'ten' cho trùng với cột trong DB
    private String tacGia;
    private boolean daMuon = false;

    public Sach() {}

    public Sach(int id, String ten, String tacGia) {
        this.id = id;
        this.ten = ten;
        this.tacGia = tacGia;
    }

    // Getter + Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getTacGia() { return tacGia; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }

    public boolean isDaMuon() { return daMuon; }
    public void setDaMuon(boolean daMuon) { this.daMuon = daMuon; }
}