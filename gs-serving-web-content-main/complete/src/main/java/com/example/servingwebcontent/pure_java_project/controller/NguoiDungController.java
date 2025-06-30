package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.NguoiDungDatabase;
import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/nguoidung")
public class NguoiDungController {

    private final NguoiDungDatabase db;

    public NguoiDungController(NguoiDungDatabase db) {
        this.db = db;
    }

    /* 1. Form đăng ký người dùng mới */
    @GetMapping("/dangky")
    public String hienFormDangKy(Model model) {
        model.addAttribute("nguoiDungMoi", new NguoiDung());
        return "dangky_nguoidung";          // templates/dangky_nguoidung.html
    }

    /* 2. Xử lý đăng ký, rồi chuyển sang trang tạo phiếu */
    @PostMapping("/dangky")
    public String xuLyDangKy(@ModelAttribute("nguoiDungMoi") NguoiDung nd,
                             RedirectAttributes ra) {
        db.themNguoiDung(nd);               // lưu DB – trả về id tự tăng
        return "redirect:/phieu-muon/tao?userId=" + nd.getId();
    }

    /* 3. Danh sách người dùng cho Quản lý */
    @GetMapping("/danh-sach")
    public String hienThiDanhSachNguoiDung(Model model) {
        List<NguoiDung> list = db.layTatCaNguoiDung();
        model.addAttribute("nguoiDungList", list);
        return "quanly_nguoidung";          // templates/quanly_nguoidung.html
    }
}