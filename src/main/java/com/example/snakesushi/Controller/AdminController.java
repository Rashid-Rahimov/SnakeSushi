package com.example.snakesushi.Controller;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import com.example.snakesushi.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @PostMapping("/login")
    public boolean login(
            @RequestBody Admin admin,
            HttpSession session) {

        return adminService.login(admin, session);

    }

    @PostMapping("/logout")
    public boolean logout(HttpSession session) {
        return adminService.logout(session);
    }

    @GetMapping
    public List<Sushi> findAll(HttpSession session) {
        return adminService.findAll(session);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Sushi addSushi(@RequestPart("sushi") Sushi sushi,
                          @RequestPart(value = "image", required = false) MultipartFile imageFile,
                          HttpSession session) throws IOException {
        return adminService.addSushi(sushi, imageFile, session);
    }

    @DeleteMapping
    public boolean deleteById(@RequestParam Long id, HttpSession session) {
        return adminService.deleteById(id, session);
    }

    @PutMapping
    public Sushi uptadeSushi(@RequestBody Sushi sushi, @RequestParam Long id, HttpSession session) {
        return adminService.uptadeSushi(sushi, id, session);
    }


}
