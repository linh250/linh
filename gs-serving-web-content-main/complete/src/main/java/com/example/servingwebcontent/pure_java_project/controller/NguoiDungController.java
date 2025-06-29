package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quan-ly/nguoi-dung")  // ✅ Đường dẫn đã sửa theo yêu cầu
public class NguoiDungController {

    private final List<NguoiDung> danhSachNguoiDung = new ArrayList<>();

    public NguoiDungController() {
        danhSachNguoiDung.add(new NguoiDung(1, "Nguyễn Văn A"));
        danhSachNguoiDung.add(new NguoiDung(2, "Trần Thị B"));
    }

    // ✅ Hiển thị danh sách người dùng chưa bị xoá
    @GetMapping
    public String hienThiDanhSach(Model model) {
        List<NguoiDung> hienThi = danhSachNguoiDung.stream()
                .filter(nd -> !nd.isDaXoa())
                .collect(Collectors.toList());
        model.addAttribute("dsNguoiDung", hienThi);
        return "nguoi-dung"; // Tên file HTML: templates/nguoi-dung.html
    }

    // ✅ Xoá mềm hoặc xoá cứng
    @GetMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable int id, @RequestParam(defaultValue = "false") boolean xoaCung) {
        for (NguoiDung nd : danhSachNguoiDung) {
            if (nd.getId() == id) {
                if (xoaCung) {
                    danhSachNguoiDung.remove(nd); // Xoá cứng
                } else {
                    nd.setDaXoa(true); // Xoá mềm
                }
                break;
            }
        }
        return "redirect:/quan-ly/nguoi-dung";
    }
}