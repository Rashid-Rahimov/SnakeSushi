package com.example.snakesushi.service;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder; // SecurityConfig-dən inject olunur

    @Value("${app.upload.dir}")
    private String uploadDir;

    // 🔐 register
    public boolean login(Admin admin) {
        if (adminRepository.findAdminByNick(admin.getNick()) != null) {
            throw new RuntimeException("Admin with this nick already exists!");
        }

        // Password-u BCrypt ilə hash et
        String hashedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashedPassword);
        adminRepository.save(admin);
        // Admin-i DB-yə save et
        return true;
    }

    // 🔓 Logout
    public boolean logout(HttpSession session) {
        session.invalidate();
        return true;
    }

    // 🔒 Session yoxlamalı CRUD
    public List<Sushi> findAll(HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return sushiRepository.findAll();
        }
        return Collections.emptyList();
    }

    public Sushi addSushi(Sushi sushi, MultipartFile imageFile, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imgName = addImage(imageFile);
                sushi.setImagePath("/images/" + imgName);
            }
            return sushiRepository.save(sushi);
        }
        return null;
    }

    public boolean deleteById(Long id, HttpSession session) {
        if (session.getAttribute("admin") != null && sushiRepository.existsById(id)) {
            Sushi sushi = sushiRepository.findById(id).get();
            deleteImg(sushi.getImagePath());
            sushiRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Sushi uptadeSushi(Sushi sushi, MultipartFile imageFile,
                             Long id, HttpSession session) {

        if (session.getAttribute("admin") != null && sushiRepository.existsById(id)) {
            Sushi nSushi = sushiRepository.findById(id).orElse(null);
            if (nSushi == null) return null;

            if (sushi.getName() != null && !sushi.getName().isBlank()) nSushi.setName(sushi.getName());
            if (sushi.getPrice() != null) nSushi.setPrice(sushi.getPrice());
            if (sushi.getSeasoning() != null && !sushi.getSeasoning().isBlank()) nSushi.setSeasoning(sushi.getSeasoning());
            if (sushi.getType() != null && !sushi.getType().isBlank()) nSushi.setType(sushi.getType());

            if (imageFile != null && !imageFile.isEmpty()) {
                String imgName = addImage(imageFile);
                if (imgName != null) {
                    deleteImg(nSushi.getImagePath());
                    nSushi.setImagePath("/images/" + imgName);
                }
            }
            return sushiRepository.save(nSushi);
        }
        return null;
    }

    // 📂 Helper funksiyalar
    private void deleteImg(String img) {
        if (img == null) return;
        Path filePath = Paths.get(uploadDir).resolve(Paths.get(img).getFileName().toString());
        try { Files.deleteIfExists(filePath); }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private String addImage(MultipartFile imageFile) {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        try { Files.write(filePath, imageFile.getBytes()); }
        catch (IOException e) { return null; }
        return fileName;
    }
}

