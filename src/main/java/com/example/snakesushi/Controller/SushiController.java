package com.example.snakesushi.Controller;

import com.example.snakesushi.model.Sushi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.snakesushi.service.SushiService;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501", allowCredentials = "true")
public class SushiController {
    private final SushiService service;

    @GetMapping
    public List<Sushi> getMenu() {
        return service.findAll();
    }

}

