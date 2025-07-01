package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.PhieuMuonDatabase;
import com.example.servingwebcontent.pure_java_project.database.SachDatabase;
import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;
import com.example.servingwebcontent.pure_java_project.model.Sach;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/phieu-muon")
public class PhieuMuonController {

    private final PhieuMuonDatabase database;
    private final SachDatabase sachDatabase;

    public PhieuMuonController(PhieuMuonDatabase database, SachDatabase sachDatabase) {
        this.database = database;
        this.sachDatabase = sachDatabase;
    }

    // ✅ GET form tạo phiếu
    @GetMapping("/tao")
    public String hienThiFormTao(
            Model model,
            @ModelAttribute("tenNguoiMoi") String tenNguoiMoi
    ) {
        if (!model.containsAttribute("phieuMuonMoi")) {
            PhieuMuon phieu = new PhieuMuon();
            phieu.setTenNguoiMuon(tenNguoiMoi); // Gán tên từ đăng ký nếu có
            model.addAttribute("phieuMuonMoi", phieu);
        }

        model.addAttribute("tenNguoiMoi", tenNguoiMoi); // dùng trong form readonly
        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());

        return "tao_phieu_muon";
    }

    // ✅ POST xử lý tạo hoặc sửa phiếu
    @PostMapping("/tao")
    public String xuLyPhieu(
            @ModelAttribute("phieuMuonMoi") PhieuMuon phieu,
            RedirectAttributes redirect
    ) {
        boolean dangSua = phieu.getId() != 0;

        if (phieu.getNgayTra() != null && phieu.getNgayMuon() != null &&
            phieu.getNgayTra().isAfter(phieu.getNgayMuon().plusDays(15))) {

            redirect.addFlashAttribute("thongBao", "❌ Ngày trả không được quá 15 ngày sau ngày mượn.");
            redirect.addFlashAttribute("thanhCong", false);
            redirect.addFlashAttribute("phieuMuonMoi", phieu);
            redirect.addFlashAttribute("tenNguoiMoi", phieu.getTenNguoiMuon());

        } else if (phieu.getNgayTra() != null && phieu.getNgayTra().isBefore(phieu.getNgayMuon())) {

            redirect.addFlashAttribute("thongBao", "❌ Ngày trả không được trước ngày mượn.");
            redirect.addFlashAttribute("thanhCong", false);
            redirect.addFlashAttribute("phieuMuonMoi", phieu);
            redirect.addFlashAttribute("tenNguoiMoi", phieu.getTenNguoiMuon());

        } else {
            if (dangSua) {
                database.capNhatPhieuMuon(phieu);
                redirect.addFlashAttribute("thongBao", "✅ Cập nhật phiếu thành công!");
            } else {
                database.themPhieuMuon(phieu);
                redirect.addFlashAttribute("thongBao", "✅ Tạo phiếu mượn thành công!");
            }

            for (Sach s : sachDatabase.layDanhSachSach()) {
                if (s.getTen().equalsIgnoreCase(phieu.getTenSach()) &&
                    s.getTacGia().equalsIgnoreCase(phieu.getTacGia())) {
                    s.setDaMuon(true);
                    sachDatabase.capNhatSach(s);
                    break;
                }
            }

            redirect.addFlashAttribute("phieuMoiTao", phieu);
            redirect.addFlashAttribute("thanhCong", true);
            redirect.addFlashAttribute("tenNguoiMoi", phieu.getTenNguoiMuon());
        }

        return "redirect:/phieu-muon/tao";
    }

    // ✅ GET sửa phiếu
    @GetMapping("/sua/{id}")
    public String suaPhieu(@PathVariable int id, Model model) {
        PhieuMuon phieu = database.layPhieuMuonTheoId(id);
        if (phieu == null) {
            return "redirect:/phieu-muon/tao";
        }

        model.addAttribute("phieuMuonMoi", phieu);
        model.addAttribute("phieuMoiTao", phieu);
        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        model.addAttribute("thongBao", "Sẵn sàng sửa phiếu.");
        model.addAttribute("thanhCong", true);
        model.addAttribute("tenNguoiMoi", phieu.getTenNguoiMuon());

        return "tao_phieu_muon";
    }

    // ✅ GET xoá phiếu
    @GetMapping("/xoa/{id}")
    public String xoaPhieu(@PathVariable int id) {
        database.xoaPhieuMuon(id);
        return "redirect:/phieu-muon/tao";
    }
}
