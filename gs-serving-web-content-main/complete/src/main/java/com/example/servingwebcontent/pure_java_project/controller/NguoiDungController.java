package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.NguoiDungDatabase;
import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nguoidung")
public class NguoiDungController {

    private final NguoiDungDatabase db;

    public NguoiDungController(NguoiDungDatabase db) {
        this.db = db;
    }

    /* 1. Hiển thị form đăng ký */
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
}