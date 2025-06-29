package com.example.servingwebcontent.pure_java_project.controller;

import com.example.servingwebcontent.pure_java_project.model.PhieuMuon;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/phieu-muon")
public class PhieuMuonController {

    private final List<PhieuMuon> danhSachPhieuMuon = new ArrayList<>();

    @GetMapping("/tao")
    public String hienThiFormTao(Model model) {
        model.addAttribute("phieuMuonMoi", new PhieuMuon());
        return "tao_phieu_muon";
    }

    @PostMapping("/tao")
    public String taoPhieu(@ModelAttribute("phieuMuonMoi") PhieuMuon phieu) {
        phieu.setId(danhSachPhieuMuon.size() + 1);

        // Kiểm tra ngày trả không vượt quá 15 ngày từ ngày mượn
        if (phieu.getNgayTra() != null && phieu.getNgayMuon() != null) {
            if (phieu.getNgayTra().isAfter(phieu.getNgayMuon().plusDays(15))) {
                return "redirect:/phieu-muon/tao?loi=ngaytra";
            }
        }

        danhSachPhieuMuon.add(phieu);
        return "redirect:/?phieu=ok";
    }

    @GetMapping("/api/all")
    @ResponseBody
    public List<PhieuMuon> tatCaPhieuMuon() {
        return danhSachPhieuMuon;
    }
}