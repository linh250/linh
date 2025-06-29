package com.example.servingwebcontent.pure_java_project.database;

import com.example.servingwebcontent.pure_java_project.model.Sach;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SachDatabase {

    private final List<Sach> danhSach = new ArrayList<>();
        private final AtomicInteger idGenerator = new AtomicInteger(1);

            public List<Sach> layDanhSachSach() {
                    return new ArrayList<>(danhSach);
                        }

                            public void themSach(Sach sach) {
                                    sach.setId(idGenerator.getAndIncrement()); // gán ID tự động
                                            danhSach.add(sach);
                                                }

                                                    public void xoaSach(int id) {
                                                            danhSach.removeIf(s -> s.getId() == id);
                                                                }

                                                                    public Sach laySachTheoId(int id) {
                                                                            return danhSach.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
                                                                                }

                                                                                    public void capNhatSach(Sach sachDaSua) {
                                                                                            for (int i = 0; i < danhSach.size(); i++) {
                                                                                                        if (danhSach.get(i).getId() == sachDaSua.getId()) {
                                                                                                                        danhSach.set(i, sachDaSua);
                                                                                                                                        break;
                                                                                                                                                    }
                                                                                                                                                            }
                                                                                                                                                                }
                                                                                                                                                                }
                                                                                                                                                                