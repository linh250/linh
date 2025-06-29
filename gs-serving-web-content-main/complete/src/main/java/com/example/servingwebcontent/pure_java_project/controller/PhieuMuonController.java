package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.PhieuMuonDatabase;
import com.example.servingwebcontent.pure_java_project.database.SachDatabase;
import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;
import com.example.servingwebcontent.pure_java_project.model.Sach;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/phieu-muon")
public class PhieuMuonController {

    private final PhieuMuonDatabase database;
    private final SachDatabase sachDatabase;

    public PhieuMuonController(PhieuMuonDatabase database, SachDatabase sachDatabase) {
        this.database = database;
        this.sachDatabase = sachDatabase;
    }

    @GetMapping("/tao")
    public String hienThiFormTao(Model model) {
        if (!model.containsAttribute("phieuMuonMoi")) {
            model.addAttribute("phieuMuonMoi", new PhieuMuon());
        }

        List<Sach> danhSachChuaMuon = sachDatabase.laySachChuaMuon();
        model.addAttribute("danhSachChuaMuon", danhSachChuaMuon);

        System.out.println(">> Số sách chưa mượn: " + danhSachChuaMuon.size());

        return "tao_phieu_muon";
    }

    @PostMapping("/tao")
    public String taoPhieu(@ModelAttribute("phieuMuonMoi") PhieuMuon phieu,
                           RedirectAttributes redirectAttributes) {
        if (phieu.getNgayTra() != null && phieu.getNgayMuon() != null) {
            if (phieu.getNgayTra().isAfter(phieu.getNgayMuon().plusDays(15))) {
                redirectAttributes.addFlashAttribute("thongBao", "Ngày trả không được quá 15 ngày sau ngày mượn.");
                redirectAttributes.addFlashAttribute("thanhCong", false);
                redirectAttributes.addFlashAttribute("phieuMuonMoi", phieu);
                return "redirect:/phieu-muon/tao";
            }
        }

        try {
            database.themPhieuMuon(phieu);

            List<Sach> danhSach = sachDatabase.layDanhSachSach();
            for (Sach s : danhSach) {
                if (s.getTen().equalsIgnoreCase(phieu.getTenSach()) &&
                    s.getTacGia().equalsIgnoreCase(phieu.getTacGia())) {
                    s.setDaMuon(true);
                    sachDatabase.capNhatSach(s);
                    break;
                }
            }

            redirectAttributes.addFlashAttribute("thongBao", "Tạo phiếu mượn thành công!");
            redirectAttributes.addFlashAttribute("thanhCong", true);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("thongBao", "Có lỗi xảy ra khi tạo phiếu mượn. Vui lòng thử lại.");
            redirectAttributes.addFlashAttribute("thanhCong", false);
            redirectAttributes.addFlashAttribute("phieuMuonMoi", phieu);
        }

        return "redirect:/phieu-muon/tao";
    }

    @GetMapping("/quan-ly")
    public String danhSachQuanLy(Model model) {
        List<PhieuMuon> danhSach = database.layTatCaPhieuMuon();
        model.addAttribute("danhSach", danhSach);
        return "nguoi_quan_ly";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable int id, Model model) {
        PhieuMuon phieu = database.layPhieuMuonTheoId(id);
        model.addAttribute("phieu", phieu);
        return "sua_phieu_muon";
    }

    @PostMapping("/sua")
    public String capNhatPhieu(@ModelAttribute("phieu") PhieuMuon phieu) {
        database.capNhatPhieuMuon(phieu);
        return "redirect:/phieu-muon/quan-ly";
    }

    @PostMapping("/cap-nhat-tra/{id}")
    public String capNhatTrangThaiDaTra(@PathVariable int id) {
        PhieuMuon phieu = database.layPhieuMuonTheoId(id);
        if (phieu != null) {
            phieu.setDaTra(!phieu.isDaTra()); // Toggle trạng thái
            database.capNhatPhieuMuon(phieu);
        }
        return "redirect:/phieu-muon/quan-ly";
    }

    @GetMapping("/xoa/{id}")
    public String xoaPhieu(@PathVariable int id) {
        database.xoaPhieuMuon(id);
        return "redirect:/phieu-muon/quan-ly";
    }
}

