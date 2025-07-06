package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.PhieuMuonDatabase;
import com.example.servingwebcontent.pure_java_project.database.SachDatabase;
import com.example.servingwebcontent.pure_java_project.database.NguoiDungDatabase;
import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;
import com.example.servingwebcontent.pure_java_project.model.Sach;
import com.example.servingwebcontent.pure_java_project.model.NguoiDung;

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
    private final NguoiDungDatabase nguoiDungDatabase;

    public PhieuMuonController(
            PhieuMuonDatabase database,
            SachDatabase sachDatabase,
            NguoiDungDatabase nguoiDungDatabase) {
        this.database = database;
        this.sachDatabase = sachDatabase;
        this.nguoiDungDatabase = nguoiDungDatabase;
    }

    @GetMapping("/tao")
    public String hienThiFormTao(Model model,
                                 @ModelAttribute("tenNguoiMoi") String tenNguoiMoi,
                                 @ModelAttribute("idNguoiMoi") String idNguoiMoiStr) {

        Long idNguoiMoi = null;
        try {
            if (idNguoiMoiStr != null && !idNguoiMoiStr.isEmpty()) {
                idNguoiMoi = Long.parseLong(idNguoiMoiStr);
            }
        } catch (NumberFormatException ignored) {}

        if (!model.containsAttribute("phieuMuonMoi")) {
            PhieuMuon phieu = new PhieuMuon();
            if (tenNguoiMoi != null && !tenNguoiMoi.isEmpty() && idNguoiMoi != null) {
                phieu.setTenNguoiMuon(tenNguoiMoi);
                phieu.setNguoiDungId(idNguoiMoi);
            }
            model.addAttribute("phieuMuonMoi", phieu);
        }

        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        model.addAttribute("danhSachNguoiDung", nguoiDungDatabase.layTatCaNguoiDung()); 

        return "tao_phieu_muon";
    }

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
        } else if (phieu.getNgayTra() != null && phieu.getNgayTra().isBefore(phieu.getNgayMuon())) {
            redirect.addFlashAttribute("thongBao", "❌ Ngày trả không được trước ngày mượn.");
            redirect.addFlashAttribute("thanhCong", false);
            redirect.addFlashAttribute("phieuMuonMoi", phieu);
        } else {
            if (phieu.getNguoiDungId() == null) {
                redirect.addFlashAttribute("thongBao", "❌ Bạn chưa chọn người mượn.");
                redirect.addFlashAttribute("thanhCong", false);
                redirect.addFlashAttribute("phieuMuonMoi", phieu);
                return "redirect:/phieu-muon/tao";
            }

            NguoiDung nguoiDung = nguoiDungDatabase.layNguoiDungTheoId(phieu.getNguoiDungId());
            if (nguoiDung != null) {
                phieu.setTenNguoiMuon(nguoiDung.getHoTen());
                phieu.setNguoiDungId(nguoiDung.getId());
            }

            if (!dangSua) {
                database.themPhieuMuon(phieu);
                redirect.addFlashAttribute("thongBao", "✅ Tạo phiếu mượn thành công!");
            } else {
                database.capNhatPhieuMuon(phieu);
                redirect.addFlashAttribute("thongBao", "✅ Cập nhật phiếu thành công!");
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
        }

        return "redirect:/phieu-muon/tao";
    }

    @GetMapping("/sua/{id}")
    public String suaPhieu(@PathVariable int id, Model model) {
        PhieuMuon phieu = database.layPhieuMuonTheoId(id);
        if (phieu == null) {
            return "redirect:/phieu-muon/tao";
        }

        model.addAttribute("phieuMuonMoi", phieu);
        model.addAttribute("phieuMoiTao", phieu);
        model.addAttribute("danhSachChuaMuon", sachDatabase.laySachChuaMuon());
        model.addAttribute("danhSachNguoiDung", nguoiDungDatabase.layTatCaNguoiDung()); 
        model.addAttribute("thongBao", "Sẵn sàng sửa phiếu.");
        model.addAttribute("thanhCong", true);

        return "tao_phieu_muon";
    }

    @GetMapping("/xoa/{id}")
    public String xoaPhieu(@PathVariable int id, RedirectAttributes redirect) {
        PhieuMuon phieu = database.layPhieuMuonTheoId(id);

        if (phieu != null) {
            Long idNguoi = phieu.getNguoiDungId();
            String tenNguoi = phieu.getTenNguoiMuon();

            database.xoaPhieuMuon(id);

            redirect.addFlashAttribute("tenNguoiMoi", tenNguoi);
            redirect.addFlashAttribute("idNguoiMoi", String.valueOf(idNguoi));
            redirect.addFlashAttribute("thongBao", "✅ Đã xoá phiếu mượn!");
            redirect.addFlashAttribute("thanhCong", true);
        }

        return "redirect:/phieu-muon/tao";
    }

    @GetMapping("/nguoi-dung/{id}")
    public String hienThiPhieuTheoNguoiDung(@PathVariable("id") long id, Model model) {
        NguoiDung nguoiDung = nguoiDungDatabase.layNguoiDungTheoId(id);
        List<PhieuMuon> danhSachPhieu = database.layPhieuMuonTheoNguoiDung(id);

        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("danhSachPhieu", danhSachPhieu);

        return "thongtin_nguoidung";
    }
}
