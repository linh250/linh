package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.model.Sach;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sach")
public class SachController {

    private final List<Sach> danhSachSach = new ArrayList<>();

    /* ===== Constructor ===== */
    public SachController() {
        danhSachSach.add(new Sach(1, "Lập Trình Java",     "Nguyễn Văn A"));
        danhSachSach.add(new Sach(2, "Cấu Trúc Dữ Liệu",   "Trần Thị B"));
    }

    /* ===== Trang hiển thị danh sách và form thêm ===== */
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("dsSach", danhSachSach);
        model.addAttribute("sachMoi", new Sach());     // form thêm
        return "sach";                                 // (templates/sach.html)
    }

    /* ===== Thêm sách mới (giữ nguyên trên cùng trang) ===== */
    @PostMapping("/them")
    public String themSach(@ModelAttribute("sachMoi") Sach sach, Model model) {
        sach.setId(danhSachSach.size() + 1);           // gán id tự tăng
        danhSachSach.add(sach);

        model.addAttribute("dsSach", danhSachSach);
        model.addAttribute("sachMoi", new Sach());     // reset form
        return "sach";
    }

    /* ===== Hiển thị form sửa ===== */
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable int id, Model model) {
        Sach sach = danhSachSach.stream()
                                .filter(s -> s.getId() == id)
                                .findFirst()
                                .orElse(null);

        if (sach == null) return "redirect:/sach";

        model.addAttribute("sach", sach);
        return "sua-sach";                             // (templates/sua-sach.html)
    }

    /* ===== Xử lý cập nhật ===== */
    @PostMapping("/sua")
    public String suaSach(@ModelAttribute Sach sachDaSua) {
        for (int i = 0; i < danhSachSach.size(); i++) {
            if (danhSachSach.get(i).getId() == sachDaSua.getId()) {
                danhSachSach.set(i, sachDaSua);
                break;
            }
        }
        return "redirect:/sach";
    }

    /* ===== Xoá sách ===== */
    @GetMapping("/xoa/{id}")
    public String xoaSach(@PathVariable int id) {
        danhSachSach.removeIf(s -> s.getId() == id);
        return "redirect:/sach";
    }
}