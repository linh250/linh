package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.NguoiDungDatabase;
import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DangKyController {

    private final NguoiDungDatabase db;

    public DangKyController(NguoiDungDatabase db) {
        this.db = db;
    }

    // Hiển thị form đăng ký người dùng mới
    @GetMapping("/nguoidung/dangky")
    public String hienFormDangKy(Model model) {
        model.addAttribute("nguoiDungMoi", new NguoiDung());
        return "dangky_nguoidung"; // ← View ở templates/dangky_nguoidung.html
    }

    // Xử lý form đăng ký
    @PostMapping("/nguoidung/dangky")
    public String xuLyDangKy(@ModelAttribute("nguoiDungMoi") NguoiDung nd,
                             RedirectAttributes redirect) {
        db.themNguoiDung(nd);
        redirect.addFlashAttribute("tenNguoiMoi", nd.getHoTen());
        return "redirect:/phieu-muon/tao";
    }
}