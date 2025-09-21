package com.example.snakesushi.Controller;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.model.Sushi;
import com.example.snakesushi.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        return adminService.login(username, password, session);

    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        return adminService.logout(session);
    }

    @GetMapping
    public List<Sushi> findAll(HttpSession session) {
        return adminService.findAll(session);
    }

    @PostMapping
    public Sushi addCustomer(@RequestBody Sushi sushi, HttpSession session) {
        return adminService.addSushi(sushi, session);
    }

    @DeleteMapping
    public void deleteById(@RequestParam Long id, HttpSession session) {
        adminService.deleteById(id, session);
    }

    @PutMapping
    public Sushi uptadeSushi(@RequestBody Sushi sushi, @RequestParam Long id, HttpSession session) {
        return adminService.uptadeSushi(sushi, id, session);
    }


}
