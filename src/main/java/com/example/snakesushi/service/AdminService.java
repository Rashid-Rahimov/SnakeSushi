package com.example.snakesushi.service;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final SushiRepository sushiRepository;
    @Value("${app.upload.dir}")
    private String uploadDir;


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


    public Sushi addSushi(Sushi sushi, MultipartFile imageFile, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin != null) {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imgName = addImage(imageFile);

                sushi.setImagePath("/images/" + imgName);
            }
            return sushiRepository.save(sushi);
        }
        return null;
    }


    public boolean deleteById(Long id, HttpSession session) {
        String admin = (String) session.getAttribute("admin");

        if (admin != null && sushiRepository.existsById(id)) {
            Sushi sushi = sushiRepository.findById(id).get();
            deleteImg(sushi.getImagePath());
            sushiRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Sushi uptadeSushi(Sushi sushi, MultipartFile imageFile,
                             Long id, HttpSession session) {

        String admin = (String) session.getAttribute("admin");
        if (admin != null && sushiRepository.existsById(id)) {
            Optional<Sushi> optionalSushi = sushiRepository.findById(id);
            if (optionalSushi.isEmpty()) {
                return null;
            }
            Sushi nSushi = optionalSushi.get();

            if (sushi.getName() != null && !sushi.getName().isBlank()) {
                nSushi.setName(sushi.getName());
            }
            if (sushi.getPrice() != null) {
                nSushi.setPrice(sushi.getPrice());
            }
            if (sushi.getSeasoning() != null && !sushi.getSeasoning().isBlank()) {
                nSushi.setSeasoning(sushi.getSeasoning());
            }
            if (sushi.getType() != null && !sushi.getType().isBlank()) {
                nSushi.setType(sushi.getType());
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                String imgName = addImage(imageFile);
                if (imgName != null) {
                    try {
                        deleteImg(nSushi.getImagePath());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    nSushi.setImagePath("/images/" + imgName);
                }
            }
            return sushiRepository.save(nSushi);
        }
        return null;
    }


    private void deleteImg(String img) {
        String fileName = Paths.get(img).getFileName().toString(); // "sushi.jpg"
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String addImage(MultipartFile imageFile) {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.write(filePath, imageFile.getBytes());
            return fileName;
        } catch (IOException e) {
            return null;
        }

    }


}
