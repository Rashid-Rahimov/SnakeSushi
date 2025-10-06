package com.example.snakesushi.Controller;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.model.Sushi;
import com.example.snakesushi.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminRepository adminRepository;

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
    public List<Sushi> findAll() {
        return adminService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Sushi addSushi(
            @RequestPart("sushi") Sushi sushi,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            HttpSession session
    ) {
        return adminService.addSushi(sushi, imageFile, session);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Long id) {
        return adminService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Sushi uptadeSushi(
            @RequestPart("sushi") Sushi sushi,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @PathVariable Long id,
            HttpSession session
    ) {
        return adminService.uptadeSushi(sushi, imageFile, id, session);
    }
}
