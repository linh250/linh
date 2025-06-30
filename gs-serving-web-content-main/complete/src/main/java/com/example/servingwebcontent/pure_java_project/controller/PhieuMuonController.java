package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.PhieuMuonDatabase;
import com.example.servingwebcontent.pure_java_project.database.SachDatabase;
import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;
import com.example.servingwebcontent.pure_java_project.model.Sach;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/phieu-muon")
public class PhieuMuonController {

    private final PhieuMuonDatabase database;
    private final SachDatabase sachDatabase;

    public PhieuMuonController(PhieuMuonDatabase database, SachDatabase sachDatabase) {
        this.database = database;
        this.sachDatabase = sachDatabase;
    }

    // GET: Hiển thị form tạo phiếu
    @GetMapping("/tao")
    public String hienThiFormTao(Model model) {
        model.addAttribute("phieuMuonMoi", new PhieuMuon());
        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        model.addAttribute("danhSachDaMuon", database.layTatCaPhieuMuon());
        model.addAttribute("dangSua", false);
        return "tao_phieu_muon";
    }

    // GET: Sửa phiếu (đổ dữ liệu vào form)
    @GetMapping("/sua/{id}")
    public String suaPhieu(@PathVariable int id, Model model) {
        PhieuMuon phieu = database.layPhieuMuonTheoId(id);
        if (phieu == null) return "redirect:/phieu-muon/tao";

        model.addAttribute("phieuMuonMoi", phieu);
        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        model.addAttribute("danhSachDaMuon", database.layTatCaPhieuMuon());
        model.addAttribute("dangSua", true);
        return "tao_phieu_muon";
    }

    // POST: Tạo hoặc sửa phiếu mượn
    @PostMapping("/tao")
    public String xuLyPhieu(@ModelAttribute("phieuMuonMoi") PhieuMuon phieu, Model model) {
        boolean dangSua = phieu.getId() != 0;

        if (phieu.getNgayTra() != null && phieu.getNgayMuon() != null &&
            phieu.getNgayTra().isAfter(phieu.getNgayMuon().plusDays(15))) {
            model.addAttribute("thongBao", "Ngày trả không được quá 15 ngày sau ngày mượn.");
            model.addAttribute("thanhCong", false);
            model.addAttribute("dangSua", dangSua);
            model.addAttribute("phieuMoiTao", null);
        } else {
            if (dangSua) {
                database.capNhatPhieuMuon(phieu);
                model.addAttribute("thongBao", "Cập nhật phiếu thành công!");
            } else {
                database.themPhieuMuon(phieu);
                model.addAttribute("thongBao", "Tạo phiếu mượn thành công!");

                for (Sach s : sachDatabase.layDanhSachSach()) {
                    if (s.getTen().equalsIgnoreCase(phieu.getTenSach()) &&
                        s.getTacGia().equalsIgnoreCase(phieu.getTacGia())) {
                        s.setDaMuon(true);
                        sachDatabase.capNhatSach(s);
                        break;
                    }
                }
            }

            model.addAttribute("thanhCong", true);
            model.addAttribute("phieuMoiTao", phieu);  // ✅ Hiển thị lại phiếu vừa tạo/sửa
            model.addAttribute("dangSua", false);      // reset flag
        }

        // Dữ liệu lại cho form
        model.addAttribute("phieuMuonMoi", new PhieuMuon());
        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        model.addAttribute("danhSachDaMuon", database.layTatCaPhieuMuon());

        return "tao_phieu_muon";
    }

    // GET: Xoá phiếu
    @GetMapping("/xoa/{id}")
    public String xoaPhieu(@PathVariable int id) {
        database.xoaPhieuMuon(id);
        return "redirect:/phieu-muon/tao";
    }
}