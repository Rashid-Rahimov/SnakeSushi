package com.example.snakesushi.service;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.enums.Role;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final PasswordEncoder passwordEncoder; // SecurityConfig-dən inject olunur

    @Value("${app.upload.dir}")
    private String uploadDir;


    public List<Sushi> findAll() {

        return sushiRepository.findAll();


    }

    public Sushi addSushi(Sushi sushi, MultipartFile imageFile) {

        if (imageFile != null && !imageFile.isEmpty()) {
            String imgName = addImage(imageFile);
            sushi.setImagePath("/images/" + imgName);
        }
        return sushiRepository.save(sushi);

    }

    public boolean deleteById(Long id) {
        if (sushiRepository.existsById(id)) {
            Sushi sushi = sushiRepository.findById(id).get();
            deleteImg(sushi.getImagePath());
            sushiRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Sushi uptadeSushi(Sushi sushi, MultipartFile imageFile,
                             Long id) {

        if (sushiRepository.existsById(id)) {

            Sushi nSushi = sushiRepository.findById(id).orElse(null);
            if (nSushi == null) return null;
            if (sushi != null) {
                if (sushi.getName() != null && !sushi.getName().isBlank()) nSushi.setName(sushi.getName());
                if (sushi.getPrice() != null) nSushi.setPrice(sushi.getPrice());
                if (sushi.getSeasoning() != null && !sushi.getSeasoning().isBlank())
                    nSushi.setSeasoning(sushi.getSeasoning());
                if (sushi.getType() != null && !sushi.getType().isBlank()) nSushi.setType(sushi.getType());
            }

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

    private void deleteImg(String img) {
        if (img == null) return;
        Path filePath = Paths.get(uploadDir).resolve(Paths.get(img).getFileName().toString());
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

    public boolean register(Admin admin) {
        if (adminRepository.findByNick(admin.getNick()).isPresent()) {
            throw new RuntimeException("Admin with this nick already exists!");
        }

        // Password-u hash et
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        // Default role
        if (admin.getRole() == null) {
            admin.setRole(Role.ADMIN);
        }

        adminRepository.save(admin);
        return true;
    }
}


