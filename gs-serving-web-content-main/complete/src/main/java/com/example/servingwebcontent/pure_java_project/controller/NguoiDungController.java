package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.NguoiDungDatabase;
import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/quan-ly/nguoi-dung")
public class NguoiDungController {

    private final NguoiDungDatabase db;

    public NguoiDungController(NguoiDungDatabase db) {
        this.db = db;
    }

    // 1. Form đăng ký
    @GetMapping("/dangky")
    public String hienFormDangKy(Model model) {
        model.addAttribute("nguoiDungMoi", new NguoiDung());
        return "dangky_nguoidung";
    }

    // 2. Xử lý đăng ký
    @PostMapping("/dangky")
    public String xuLyDangKy(@ModelAttribute("nguoiDungMoi") NguoiDung nd,
                             RedirectAttributes redirect) {
        db.themNguoiDung(nd);
        redirect.addFlashAttribute("tenNguoiMoi", nd.getHoTen());
        return "redirect:/phieu-muon/tao";
    }

    // 3. Danh sách người dùng – mặc định
    @GetMapping("")
    public String hienThiDanhSachNguoiDung(Model model) {
        List<NguoiDung> list = db.layTatCaNguoiDung();
        model.addAttribute("nguoiDungList", list);
        return "quanly_nguoidung";
    }

    // ✅ 3.1. Tìm kiếm người dùng
    @GetMapping("/tim-kiem")
    public String timKiemNguoiDung(@RequestParam("keyword") String keyword, Model model) {
        List<NguoiDung> ketQua = db.timKiemNguoiDung(keyword);
        model.addAttribute("nguoiDungList", ketQua);
        model.addAttribute("keyword", keyword);
        return "quanly_nguoidung";
    }

    // 4. Xoá người dùng
    @GetMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable("id") long id) {
        db.xoaNguoiDung(id);
        return "redirect:/quan-ly/nguoi-dung";
    }

    // 5. Hiển thị form sửa người dùng
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

    // 6. Xử lý sửa
    @PostMapping("/sua")
    public String xuLySua(@ModelAttribute("nguoiDungSua") NguoiDung nd) {
        db.capNhatNguoiDung(nd);
        return "redirect:/quan-ly/nguoi-dung";
    }
}