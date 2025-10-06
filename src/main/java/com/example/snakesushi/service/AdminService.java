package com.example.snakesushi.service;

import com.example.snakesushi.ImageException;
import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.Repository.ImageRepository;
import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.model.Sushi;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final SushiRepository sushiRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig-dən inject olunur
    private final ImageRepository imageRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    /*
    // 🔐 register
    public boolean register(Admin admin) {
        if (adminRepository.findAdminByNick(admin.getNick()) != null) {
            throw new RuntimeException("Admin with this nick already exists!");
        }

        // Password-u BCrypt ilə hash et
        String hashedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashedPassword);
        admin.setRole(Role.USER);
        adminRepository.save(admin);
        // Admin-i DB-yə save et
        return true;
    }
     */

    // 🔓 Logout
    public boolean logout(HttpSession session) {
        session.invalidate();
        return true;
    }

    // 🔒 Session yoxlamalı CRUD
    public List<Sushi> findAll() {
        return sushiRepository.findAll();
    }

    public Sushi addSushi(Sushi sushi, MultipartFile imageFile, HttpSession session) {
        /*if (imageFile != null && !imageFile.isEmpty()) {
            String imgName = add(imageFile);
            sushi.setImagePath("/images/" + imgName);
        }
        return sushiRepository.save(sushi);*/
        if (sushi.getImagePath() != null) throw new ImageException("Please leave path null", 400);
        Sushi createdSushi = sushiRepository.save(sushi);
        if (imageFile != null && !imageFile.isEmpty()) {
            String extension = imageFile.getOriginalFilename();
            extension = extension.substring(extension.indexOf('.') + 1);
            try {
                imageRepository.add(
                        createdSushi.getId(),
                        imageRepository.mapType(extension),
                        imageFile.getBytes()
                );
                createdSushi.setImagePath(imageRepository.getImageNameById(createdSushi.getId()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return createdSushi;
    }

    public boolean deleteById(Long id) {
        if (sushiRepository.existsById(id)) {
            Sushi sushi = sushiRepository.findById(id).get();
            //deleteImg(sushi.getImagePath());
            sushiRepository.deleteById(id);
            if (imageRepository.exists(id)) {
                imageRepository.delete(id);
            }

            return true;
        }
        return false;
    }

    public Sushi uptadeSushi(Sushi sushi, MultipartFile imageFile,
                             Long id, HttpSession session) {

        if (sushi.getImagePath() != null) throw new ImageException("Please leave path null", 400);
        if (sushiRepository.existsById(id)) {
            Sushi nSushi = sushiRepository.findById(id).orElse(null);
            if (nSushi == null) return null;

            if (sushi.getName() != null && !sushi.getName().isBlank()) nSushi.setName(sushi.getName());
            if (sushi.getPrice() != null) nSushi.setPrice(sushi.getPrice());
            if (sushi.getSeasoning() != null && !sushi.getSeasoning().isBlank())
                nSushi.setSeasoning(sushi.getSeasoning());
            if (sushi.getType() != null && !sushi.getType().isBlank()) nSushi.setType(sushi.getType());

            if (imageFile != null && !imageFile.isEmpty()) {
                /*String imgName = add(imageFile);
                if (imgName != null) {
                    deleteImg(nSushi.getImagePath());
                    nSushi.setImagePath("/images/" + imgName);
                }*/
                if (imageRepository.exists(id)) {
                    imageRepository.delete(id);
                }
                String extension = imageFile.getOriginalFilename();
                extension = extension.substring(extension.indexOf('.') + 1);
                try {
                    imageRepository.add(
                            id,
                            imageRepository.mapType(extension),
                            imageFile.getBytes()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                nSushi.setImagePath(imageRepository.getImageNameById(id));
            }
            return sushiRepository.save(nSushi);
        }
        return null;
    }

    // 📂 Helper funksiyalar
    private void deleteImg(@NonNull String imgName) {
        Path filePath = Paths.get(uploadDir).resolve(Paths.get(imgName).getFileName().toString());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String addImage(MultipartFile imageFile) {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        try {
            Files.write(filePath, imageFile.getBytes());
        } catch (IOException e) {
            return null;
        }
        return fileName;
    }
}

