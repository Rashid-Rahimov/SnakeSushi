package com.example.snakesushi.service;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final SushiRepository sushiRepository;


    public boolean login(
            Admin admin,
            HttpSession session) {

        Admin adminDB = adminRepository.findAdminByNick(admin.getNick());

        if (adminDB == null) {
            return false;
        }

        if (admin.getPassword().equals(adminDB.getPassword())) {
            session.setAttribute("admin", adminDB.getNick());
            return true;
        } else {
            return false;
        }

    }

    public boolean logout(HttpSession session) {
        session.invalidate();
        return true;
    }


    public List<Sushi> findAll(HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin != null) {
            return sushiRepository.findAll();
        }
        return Collections.emptyList();
    }


    public Sushi addSushi(Sushi sushi, MultipartFile imageFile, HttpSession session) throws IOException {
//        String admin = (String) session.getAttribute("admin");
//        if (admin != null) {
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("C:\\Users\\user\\IdeaProjects\\SnakeSushi\\images");
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());
                sushi.setImagePath(filePath.toString());
//        }
        return sushiRepository.save(sushi);
        }
        return null;
    }


    public boolean deleteById(Long id, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin != null && sushiRepository.existsById(id)) {
            sushiRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Sushi uptadeSushi(Sushi sushi, Long id, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin != null && sushiRepository.existsById(id)) {
            Sushi nSushi = sushiRepository.findById(id).get();
            if (sushi.getName() != null) {
                nSushi.setName(sushi.getName());
            }
            if (sushi.getPrice() != null) {
                nSushi.setPrice(sushi.getPrice());
            }
            if (sushi.getSeasoning() != null) {
                nSushi.setSeasoning(sushi.getSeasoning());
            }

            return sushiRepository.save(nSushi);
        }
        return null;
    }


}
