package com.example.snakesushi.service;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.model.Admin;
import com.example.snakesushi.model.Sushi;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final SushiRepository sushiRepository;
    private final HttpSession httpSession;


    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        Admin admin = adminRepository.findAdminByNick(username);

        if (admin == null) {
            return "Yanlis nick";
        }

        if (admin.getPassword().equals(password)) {
            session.setAttribute("admin", username);
            return "Succesful";
        } else {
            return "Yanlis mesaj ua da sifre";
        }

    }

    public String logout(HttpSession session) {
        session.invalidate();
        return "Hesabdan çıxıldı";
    }


    public List<Sushi> findAll(HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin != null) {
            sushiRepository.findAll();
        }
        return null;
    }


    public Sushi addSushi(@RequestBody Sushi sushi, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin != null) {
            sushiRepository.save(sushi);
        }
        return null;
    }


    public void deleteById(@RequestParam Long id, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin != null) {
            sushiRepository.deleteById(id);
        }
    }


    public Sushi uptadeSushi(@RequestBody Sushi sushi, @RequestParam Long id, HttpSession session) {
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
