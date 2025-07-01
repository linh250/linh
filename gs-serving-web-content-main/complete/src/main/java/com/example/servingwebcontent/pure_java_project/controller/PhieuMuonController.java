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
        return "tao_phieu_muon";
    }

    // GET: Sửa phiếu (đổ dữ liệu vào form + hiển thị phiếu bên dưới)
    @GetMapping("/sua/{id}")
    public String suaPhieu(@PathVariable int id, Model model) {
        PhieuMuon phieu = database.layPhieuMuonTheoId(id);
        if (phieu == null) return "redirect:/phieu-muon/tao";

        model.addAttribute("phieuMuonMoi", phieu);         // đổ lên form
        model.addAttribute("phieuMoiTao", phieu);          // hiển thị dưới bảng
        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        model.addAttribute("thongBao", "Sẵn sàng sửa phiếu.");
        model.addAttribute("thanhCong", true);
        return "tao_phieu_muon";
    }

    // POST: Tạo hoặc sửa phiếu mượn
    @PostMapping("/tao")
    public String xuLyPhieu(@ModelAttribute("phieuMuonMoi") PhieuMuon phieu, Model model) {
        boolean dangSua = phieu.getId() != 0;

        if (phieu.getNgayTra() != null && phieu.getNgayMuon() != null &&
            phieu.getNgayTra().isAfter(phieu.getNgayMuon().plusDays(15))) {

            model.addAttribute("thongBao", "❌ Ngày trả không được quá 15 ngày sau ngày mượn.");
            model.addAttribute("thanhCong", false);
            model.addAttribute("phieuMoiTao", null);
            model.addAttribute("phieuMuonMoi", phieu); // giữ lại dữ liệu cũ để user sửa tiếp

        } else {
            if (dangSua) {
                database.capNhatPhieuMuon(phieu);
                model.addAttribute("thongBao", "✅ Cập nhật phiếu thành công!");
            } else {
                database.themPhieuMuon(phieu);
                model.addAttribute("thongBao", "✅ Tạo phiếu mượn thành công!");

                // Cập nhật trạng thái đã mượn cho sách
                for (Sach s : sachDatabase.layDanhSachSach()) {
                    if (s.getTen().equalsIgnoreCase(phieu.getTenSach()) &&
                        s.getTacGia().equalsIgnoreCase(phieu.getTacGia())) {
                        s.setDaMuon(true);
                        sachDatabase.capNhatSach(s);
                        break;
                    }
                }
            }

            model.addAttribute("phieuMoiTao", phieu); // hiển thị lại bên dưới
            model.addAttribute("phieuMuonMoi", new PhieuMuon()); // làm trống form
            model.addAttribute("thanhCong", true);
        }

        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        return "tao_phieu_muon";
    }

    // GET: Xoá phiếu
    @GetMapping("/xoa/{id}")
    public String xoaPhieu(@PathVariable int id) {
        database.xoaPhieuMuon(id);
        return "redirect:/phieu-muon/tao";
    }
}