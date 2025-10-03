package com.example.snakesushi.Controller;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import com.example.snakesushi.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501", allowCredentials = "true")
public class AdminController {

    private final AdminService adminService;
    private final AdminRepository adminRepository;

    @PostMapping("/register")
    public boolean register(@RequestBody Admin admin) {

     return adminService.login(admin);


    }
    @PostMapping("/register")
    public boolean register(@RequestBody Admin admin) {
        return adminService.register(admin);
    }

    // 🔓 logout açıq
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        boolean success = adminService.logout(session);
        if (success) {
            return ResponseEntity.ok("Logout successful ✅");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not logged in ❌");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Sushi> findAll(HttpSession session) {
        return adminService.findAll(session);
    }

    // 🔒 yalnız ADMIN əlavə edə bilər
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Sushi addSushi(@RequestPart("sushi") Sushi sushi,
                          @RequestPart(value = "image", required = false) MultipartFile imageFile,
                          HttpSession session) {
        return adminService.addSushi(sushi, imageFile, session);
    }

    // 🔒 yalnız ADMIN silə bilər
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public boolean deleteById(@RequestParam Long id, HttpSession session) {
        return adminService.deleteById(id, session);
    }

    // 🔒 yalnız ADMIN update edə bilər
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Sushi uptadeSushi(@RequestPart("sushi") Sushi sushi,
                             @RequestPart(value = "image", required = false) MultipartFile imageFile,
                             @RequestPart("id") Long id,
                             HttpSession session) {
        return adminService.uptadeSushi(sushi, imageFile, id, session);
    }

}
