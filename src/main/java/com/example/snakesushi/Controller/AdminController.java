package com.example.snakesushi.Controller;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import com.example.snakesushi.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
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


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Admin admin) {
        boolean success = adminService.register(admin);
        if (success) {
            return ResponseEntity.ok("Registration successful ✅");
        }
        return ResponseEntity.badRequest().body("Registration failed ❌");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Sushi> findAll( ) {
        return adminService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Sushi addSushi(@RequestPart("sushi") Sushi sushi,
                          @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return adminService.addSushi(sushi, imageFile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public boolean deleteById(@RequestParam Long id) {
        return adminService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Sushi uptadeSushi(@RequestPart(value = "sushi", required = false) Sushi sushi,
                             @RequestPart(value = "image", required = false) MultipartFile imageFile,
                             @RequestPart("id") Long id) {
        return adminService.uptadeSushi(sushi, imageFile, id);
    }

}
