package com.example.snakesushi.Controller;

import com.example.snakesushi.model.Sushi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.snakesushi.service.SushiService;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class SushiController {

    private final SushiService service;

    @GetMapping
    public List<Sushi> getMenu() {
        return service.findAll();
    }

}

