package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.NguoiDungDatabase;
import com.example.servingwebcontent.pure_java_project.database.PhieuMuonDatabase;
import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/quan-ly/nguoi-dung")
public class NguoiDungController {

    private final NguoiDungDatabase db;
    private final PhieuMuonDatabase phieuMuonDB;

    public NguoiDungController(NguoiDungDatabase db, PhieuMuonDatabase phieuMuonDB) {
        this.db = db;
        this.phieuMuonDB = phieuMuonDB;
    }

    // 1. Hiển thị form đăng ký người dùng
    @GetMapping("/dangky")
    public String hienFormDangKy(Model model) {
        if (!model.containsAttribute("nguoiDungMoi")) {
            model.addAttribute("nguoiDungMoi", new NguoiDung());
        }
        return "dangky_nguoidung";
    }

    // 2. Xử lý đăng ký (có kiểm tra trùng tài khoản)
    @PostMapping("/dangky")
    public String xuLyDangKy(@ModelAttribute("nguoiDungMoi") NguoiDung nd,
                             RedirectAttributes redirect) {

        boolean thanhCong = db.themNguoiDung(nd); // sẽ trả false nếu trùng tài khoản

        if (!thanhCong) {
            redirect.addFlashAttribute("thongBao", " Tài khoản đã tồn tại. Vui lòng chọn tài khoản khác.");
            redirect.addFlashAttribute("thanhCong", false);
            redirect.addFlashAttribute("nguoiDungMoi", nd); // giữ lại thông tin đã nhập
            return "redirect:/quan-ly/nguoi-dung/dangky";
        }

        
        PhieuMuon phieu = new PhieuMuon();
        phieu.setNguoiDungId(nd.getId());
        phieu.setTenNguoiMuon(nd.getHoTen());
        phieu.setNgayMuon(LocalDate.now());
        phieu.setNgayTra(LocalDate.now().plusDays(7));

        redirect.addFlashAttribute("thongBao", "✅ Đăng ký thành công cho " + nd.getHoTen());
        redirect.addFlashAttribute("thanhCong", true);
        redirect.addFlashAttribute("phieuMuonMoi", phieu); 

        return "redirect:/phieu-muon/tao";
    }

    // 3. Danh sách người dùng
    @GetMapping("")
    public String hienThiDanhSachNguoiDung(Model model,
                                           @ModelAttribute("thongBao") String thongBao) {
        List<NguoiDung> list = db.layTatCaNguoiDung();
        model.addAttribute("nguoiDungList", list);
        model.addAttribute("thongBao", thongBao);
        return "quanly_nguoidung";
    }

    // 3.1 Tìm kiếm người dùng
    @GetMapping("/tim-kiem")
    public String timKiemNguoiDung(@RequestParam("keyword") String keyword, Model model) {
        List<NguoiDung> ketQua = db.timKiemNguoiDung(keyword);
        model.addAttribute("nguoiDungList", ketQua);
        model.addAttribute("keyword", keyword);
        return "quanly_nguoidung";
    }

    // 4. Xoá người dùng
    @GetMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable("id") long id, RedirectAttributes redirect) {
        db.xoaNguoiDung(id);
        redirect.addFlashAttribute("thongBao", "✅ Đã xoá người dùng thành công.");
        return "redirect:/quan-ly/nguoi-dung";
    }

    // 5. Hiển thị form sửa
    @GetMapping("/sua/{id}")
    public String hienFormSua(@PathVariable("id") long id, Model model) {
        NguoiDung nd = db.layNguoiDungTheoId(id);
        if (nd != null) {
            model.addAttribute("nguoiDungSua", nd);
            return "sua_nguoidung";
        } else {
            return "redirect:/quan-ly/nguoi-dung";
        }
    }

    // 6. Xử lý cập nhật người dùng
    @PostMapping("/sua")
    public String xuLySua(@ModelAttribute("nguoiDungSua") NguoiDung nd, RedirectAttributes redirect) {
        db.capNhatNguoiDung(nd);
        redirect.addFlashAttribute("thongBao", "✅ Đã cập nhật thông tin người dùng.");
        return "redirect:/quan-ly/nguoi-dung";
    }

    // 7. Thông tin người dùng + phiếu mượn
    @GetMapping("/thong-tin/{id}")
    public String xemThongTinNguoiDung(@PathVariable long id, Model model) {
        NguoiDung nd = db.layNguoiDungTheoId(id);
        if (nd == null) {
            return "redirect:/quan-ly/nguoi-dung";
        }

        List<PhieuMuon> danhSach = phieuMuonDB.layPhieuMuonTheoNguoiDung(id);
        model.addAttribute("nguoiDung", nd);
        model.addAttribute("danhSachPhieu", danhSach);
        return "thongtin_nguoidung";
    }
}