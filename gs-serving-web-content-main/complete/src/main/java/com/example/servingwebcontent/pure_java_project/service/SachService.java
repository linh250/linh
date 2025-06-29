package com.example.servingwebcontent.pure_java_project.service;

import com.example.servingwebcontent.pure_java_project.model.Sach;

import java.util.List;

public interface SachService {
    List<Sach> getAll();
    void addSach(Sach sach);
    List<Sach> searchByKeyword(String keyword);
}