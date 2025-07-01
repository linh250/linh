// package com.example.servingwebcontent.pure_java_project.controller;

// import com.example.servingwebcontent.pure_java_project.database.NguoiDungDatabase;
// import com.example.servingwebcontent.pure_java_project.model.NguoiDung;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

// import java.util.List;

// @Controller
// @RequestMapping("/quan-ly")                // gốc URL: /quan-ly/...
// public class QuanLyNguoiDungController {

//     private final NguoiDungDatabase db;

//     // Spring sẽ tự tiêm NguoiDungDatabase
//     public QuanLyNguoiDungController(NguoiDungDatabase db) {
//         this.db = db;
//     }

//     /* /quan-ly/nguoi-dung  → hiển thị bảng người dùng */
//     @GetMapping("/nguoi-dung")
//     public String danhSachNguoiDung(Model model) {
//         List<NguoiDung> list = db.layTatCaNguoiDung();
//         model.addAttribute("danhSachNguoiDung", list);
//         return "quanly_nguoidung";          // templates/quanly_nguoidung.html
//     }
// }
