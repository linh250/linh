package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.database.SachDatabase;
import com.example.servingwebcontent.pure_java_project.model.Sach;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sach")
public class SachController {

    private final SachDatabase sachDb;

    // Spring sẽ tự inject bean SachDatabase qua constructor
    public SachController(SachDatabase sachDb) {
        this.sachDb = sachDb;
    }

    /** GET /sach – hiển thị danh sách + form thêm */
    @GetMapping
    public String hienThiDanhSach(Model model) {
        List<Sach> danhSach = sachDb.layDanhSachSach();
        model.addAttribute("dsSach", danhSach);
        model.addAttribute("sachMoi", new Sach());
        return "danh-sach"; // ✅ view trùng với file danh-sach.html
    }

    /** POST /sach/them – thêm sách mới */
    @PostMapping("/them")
    public String themSach(@ModelAttribute("sachMoi") Sach sach) {
        sachDb.themSach(sach);
        return "redirect:/sach"; // ✅ quay lại trang danh sách
    }

    /** GET /sach/xoa/{id} – xoá sách */
    @GetMapping("/xoa/{id}")
    public String xoaSach(@PathVariable int id) {
        sachDb.xoaSach(id);
        return "redirect:/sach";
    }

    /** GET /sach/sua/{id} – hiện form sửa */
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable int id, Model model) {
        Sach sach = sachDb.laySachTheoId(id);
        if (sach == null) return "redirect:/sach";

        model.addAttribute("sach", sach);
        return "sua-sach";
    }

    /** POST /sach/sua – lưu cập nhật */
    @PostMapping("/sua")
    public String suaSach(@ModelAttribute Sach sachDaSua) {
        sachDb.capNhatSach(sachDaSua);
        return "redirect:/sach";
    }
}
